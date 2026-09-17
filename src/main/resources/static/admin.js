if (requireRole("ADMIN")) {
    initializeAdminDashboard();
}

let adminCourses = [];
let adminStudents = [];
let adminEnrollments = [];

function initializeAdminDashboard() {
    bindAdminEvents();
    loadAdminDashboard();
}

function bindAdminEvents() {
    document.getElementById("createCourseForm").addEventListener("submit", createCourse);
    document.getElementById("adminCourseSearch").addEventListener("input", renderAdminCourses);
    document.getElementById("adminStudentSearch").addEventListener("input", renderAdminStudents);
    document.getElementById("adminLogout").addEventListener("click", logoutAdmin);
    document.getElementById("editCourseForm").addEventListener("submit", updateCourse);
    document.getElementById("closeEditModal").addEventListener("click", closeEditCourseModal);
    document.getElementById("cancelEditCourse").addEventListener("click", closeEditCourseModal);
    document.getElementById("editCourseModal").addEventListener("click", event => {
        if (event.target.id === "editCourseModal") closeEditCourseModal();
    });
}

async function loadAdminDashboard() {
    await Promise.all([loadAdminCourses(), loadAdminStudents(), loadAdminEnrollments()]);
    renderAdminStats();
}

async function loadAdminCourses() {
    try {
        const data = await apiRequest("/get-courses");
        adminCourses = Array.isArray(data) ? data : [];
    } catch (_) {
        adminCourses = [];
    }
    renderAdminCourses();
}

async function loadAdminStudents() {
    try {
        const data = await apiRequest("/get-all-students", {
            headers: { email: localStorage.getItem("email") }
        });
        adminStudents = Array.isArray(data) ? data : [];
    } catch (_) {
        adminStudents = [];
    }
    renderAdminStudents();
}

async function loadAdminEnrollments() {
    try {
        const data = await apiRequest("/get-all-enrollments", {
            headers: { email: localStorage.getItem("email") }
        });
        adminEnrollments = Array.isArray(data) ? data : [];
    } catch (_) {
        adminEnrollments = [];
    }
    renderAdminEnrollments();
}

function renderAdminStats() {
    document.getElementById("adminCourseCount").textContent = adminCourses.length;
    document.getElementById("adminStudentCount").textContent = adminStudents.length;
    document.getElementById("adminEnrollmentCount").textContent = adminEnrollments.length;
}

async function createCourse(event) {
    event.preventDefault();
    const form = event.currentTarget;
    const button = form.querySelector("button[type='submit']");
    const originalText = button.textContent;

    const payload = {
        courseCode: document.getElementById("courseCode").value.trim(),
        title: document.getElementById("courseTitle").value.trim(),
        creditUnit: Number(document.getElementById("creditUnit").value),
        department: document.getElementById("courseDepartment").value.trim()
    };

    try {
        button.disabled = true;
        button.textContent = "Creating...";
        const data = await apiRequest("/create-course", {
            method: "POST",
            headers: { email: localStorage.getItem("email") },
            body: JSON.stringify(payload)
        });
        showToast(data.message || "Course created successfully");
        form.reset();
        await loadAdminCourses();
        renderAdminStats();
    } catch (error) {
        showToast(error.message || "Could not create course", "error");
    } finally {
        button.disabled = false;
        button.textContent = originalText;
    }
}

function renderAdminCourses() {
    const body = document.getElementById("adminCoursesTableBody");
    const query = (document.getElementById("adminCourseSearch")?.value || "").trim().toLowerCase();
    const filtered = adminCourses.filter(course =>
        [course.courseCode, course.title, course.department].some(value => String(value || "").toLowerCase().includes(query))
    );

    if (!filtered.length) {
        body.innerHTML = '<tr><td colspan="5" class="empty-cell">No courses found.</td></tr>';
        return;
    }

    body.innerHTML = filtered.map(course => `
        <tr>
            <td><strong>${escapeHtml(course.courseCode)}</strong></td>
            <td>${escapeHtml(course.title)}</td>
            <td>${escapeHtml(course.department)}</td>
            <td>${escapeHtml(course.creditUnit)}</td>
            <td class="action-buttons">
                <button class="edit-button" data-edit-course="${escapeHtml(course.courseCode)}">Edit</button>
                <button class="delete-button" data-delete-course="${escapeHtml(course.courseCode)}">Delete</button>
            </td>
        </tr>`).join("");

    body.querySelectorAll("button[data-edit-course]").forEach(button => {
        button.addEventListener("click", () => openEditCourseModal(button.dataset.editCourse));
    });
    body.querySelectorAll("button[data-delete-course]").forEach(button => {
        button.addEventListener("click", () => deleteCourse(button.dataset.deleteCourse));
    });
}

function openEditCourseModal(courseCode) {
    const course = adminCourses.find(item => item.courseCode === courseCode);
    if (!course) return;

    document.getElementById("editCourseCode").value = course.courseCode;
    document.getElementById("editCourseTitle").value = course.title || "";
    document.getElementById("editCreditUnit").value = course.creditUnit || "";
    document.getElementById("editCourseDepartment").value = course.department || "";
    document.getElementById("editCourseHeading").textContent = `Update ${course.courseCode}`;
    document.getElementById("editCourseModal").hidden = false;
}

function closeEditCourseModal() {
    document.getElementById("editCourseModal").hidden = true;
}

async function updateCourse(event) {
    event.preventDefault();
    const courseCode = document.getElementById("editCourseCode").value;
    const button = event.currentTarget.querySelector("button[type='submit']");
    const originalText = button.textContent;

    const payload = {
        title: document.getElementById("editCourseTitle").value.trim(),
        creditUnit: Number(document.getElementById("editCreditUnit").value),
        department: document.getElementById("editCourseDepartment").value.trim()
    };

    try {
        button.disabled = true;
        button.textContent = "Saving...";
        const data = await apiRequest(`/update-course/${encodeURIComponent(courseCode)}`, {
            method: "PUT",
            headers: { email: localStorage.getItem("email") },
            body: JSON.stringify(payload)
        });
        showToast(data.message || "Course updated successfully");
        closeEditCourseModal();
        await loadAdminCourses();
    } catch (error) {
        showToast(error.message || "Could not update course", "error");
    } finally {
        button.disabled = false;
        button.textContent = originalText;
    }
}

async function deleteCourse(courseCode) {
    if (!window.confirm(`Delete ${courseCode}?`)) return;

    try {
        const data = await apiRequest(`/delete-course/${encodeURIComponent(courseCode)}`, {
            method: "DELETE",
            headers: { email: localStorage.getItem("email") }
        });
        showToast(typeof data === "string" ? data : "Course deleted");
        await loadAdminCourses();
        renderAdminStats();
    } catch (error) {
        showToast(error.message || "Could not delete course", "error");
    }
}

function renderAdminStudents() {
    const body = document.getElementById("adminStudentsTableBody");
    const query = (document.getElementById("adminStudentSearch")?.value || "").trim().toLowerCase();
    const filtered = adminStudents.filter(student =>
        [student.studentId, student.name, student.email, student.department].some(value => String(value || "").toLowerCase().includes(query))
    );

    if (!filtered.length) {
        body.innerHTML = '<tr><td colspan="6" class="empty-cell">No students found.</td></tr>';
        return;
    }

    body.innerHTML = filtered.map(student => `
        <tr>
            <td class="mono-cell">${escapeHtml(student.studentId)}</td>
            <td>${escapeHtml(student.name)}</td>
            <td>${escapeHtml(student.email)}</td>
            <td>${escapeHtml(student.department)}</td>
            <td><span class="${student.active ? "status-active" : "status-inactive"}">${student.active ? "Active" : "Inactive"}</span></td>
            <td><button class="view-enrollment-button" data-student-id="${escapeHtml(student.studentId)}">View Enrollments</button></td>
        </tr>`).join("");

    body.querySelectorAll("button[data-student-id]").forEach(button => {
        button.addEventListener("click", () => viewStudentEnrollments(button.dataset.studentId));
    });
}

function renderAdminEnrollments() {
    const body = document.getElementById("adminEnrollmentsTableBody");

    if (!adminEnrollments.length) {
        body.innerHTML = '<tr><td colspan="5" class="empty-cell">No enrollments found.</td></tr>';
        return;
    }

    body.innerHTML = adminEnrollments.map(enrollment => `
        <tr>
            <td class="mono-cell">${escapeHtml(enrollment.enrollmentId)}</td>
            <td class="mono-cell">${escapeHtml(enrollment.studentId)}</td>
            <td>${escapeHtml(enrollment.session)}</td>
            <td>${escapeHtml(formatAdminSemester(enrollment.semester))}</td>
            <td>${escapeHtml((enrollment.courseCodes || []).join(", "))}</td>
        </tr>`).join("");
}

async function viewStudentEnrollments(studentId) {
    const selectedStudent = adminStudents.find(student => student.studentId === studentId);
    const section = document.getElementById("studentEnrollmentSection");
    const history = document.getElementById("selectedStudentHistory");

    document.getElementById("selectedStudentName").textContent = selectedStudent?.name || "Student";
    document.getElementById("selectedStudentDepartment").textContent = selectedStudent?.department || "Department";
    document.getElementById("selectedStudentId").textContent = studentId;
    section.hidden = false;
    history.innerHTML = '<p class="empty-state">Loading enrollments...</p>';
    section.scrollIntoView({ behavior: "smooth", block: "start" });

    try {
        const data = await apiRequest(`/get-student-enrollments/${encodeURIComponent(studentId)}`);
        const items = Array.isArray(data) ? data : [];

        if (!items.length) {
            history.innerHTML = '<p class="empty-state">This student has no enrollment history.</p>';
            return;
        }

        history.innerHTML = items.map(enrollment => `
            <div class="history-card">
                <div class="history-main">
                    <div class="history-icon">📚</div>
                    <div>
                        <span class="history-session">${escapeHtml(enrollment.session)}</span>
                        <h3>${escapeHtml(formatAdminSemester(enrollment.semester).toUpperCase())}</h3>
                        <p>${escapeHtml((enrollment.courseCodes || []).join(", ") || "No courses")}</p>
                    </div>
                </div>
                <span class="history-count">${(enrollment.courseCodes || []).length} Courses</span>
            </div>`).join("");
    } catch (_) {
        history.innerHTML = '<p class="empty-state">This student has no enrollment history.</p>';
    }
}

function formatAdminSemester(value) {
    if (value === "FIRST_SEMESTER") return "First Semester";
    if (value === "SECOND_SEMESTER") return "Second Semester";
    return value || "Semester";
}

function logoutAdmin() {
    // The current backend keeps login state only for students; admin logout is local.
    clearSession();
    window.location.href = "login.html";
}
