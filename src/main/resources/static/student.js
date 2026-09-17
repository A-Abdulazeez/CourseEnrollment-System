if (requireRole("STUDENT")) {
    initializeStudentDashboard();
}

let student = null;
let courses = [];
let enrollments = [];
let activeEnrollment = null;
const selectedStartingCourses = new Set();

async function initializeStudentDashboard() {
    bindStudentEvents();

    try {
        const email = localStorage.getItem("email");
        student = await apiRequest(`/get-student/${encodeURIComponent(email)}`);
        localStorage.setItem("studentId", student.studentId);
        renderStudentProfile();

        await Promise.all([loadCourses(), loadEnrollments()]);
        renderAllStudentData();
    } catch (error) {
        showToast(error.message || "Could not load student dashboard", "error");
    }
}

function bindStudentEvents() {
    document.getElementById("createEnrollmentForm").addEventListener("submit", createEnrollment);
    document.getElementById("studentCourseSearch").addEventListener("input", renderAvailableCourses);
    document.getElementById("studentLogout").addEventListener("click", logoutStudent);
}

async function loadCourses() {
    try {
        const data = await apiRequest("/get-courses");
        courses = Array.isArray(data) ? data : [];
    } catch (_) {
        courses = [];
    }
}

async function loadEnrollments() {
    if (!student?.studentId) return;

    try {
        const data = await apiRequest(`/get-student-enrollments/${encodeURIComponent(student.studentId)}`);
        enrollments = Array.isArray(data) ? data : [];
    } catch (_) {
        enrollments = [];
    }

    activeEnrollment = getLatestEnrollment(enrollments);
}

function getLatestEnrollment(items) {
    if (!items.length) return null;

    return [...items].sort((a, b) => {
        const sessionDifference = Number(b.session || 0) - Number(a.session || 0);
        if (sessionDifference !== 0) return sessionDifference;

        const semesterRank = value => value === "SECOND_SEMESTER" ? 2 : 1;
        return semesterRank(b.semester) - semesterRank(a.semester);
    })[0];
}

function renderStudentProfile() {
    const name = student?.name || "Student";
    const firstName = name.trim().split(/\s+/)[0] || "Student";
    const initials = name.trim().split(/\s+/).filter(Boolean).slice(0, 2).map(part => part[0].toUpperCase()).join("") || "ST";

    document.getElementById("welcomeName").textContent = `Welcome back, ${firstName}`;
    document.getElementById("profileName").textContent = name;
    document.getElementById("profileDepartment").textContent = student?.department || "Department";
    document.getElementById("profileAvatar").textContent = initials;
}

function renderAllStudentData() {
    renderEnrollmentCourseChoices();
    renderAvailableCourses();
    renderCurrentEnrollment();
    renderEnrollmentHistory();
    renderStudentStats();
}

function renderEnrollmentCourseChoices() {
    const container = document.getElementById("enrollmentCourseChoices");

    if (!courses.length) {
        container.innerHTML = '<span class="muted-text">No courses are available yet.</span>';
        return;
    }

    container.innerHTML = courses.map(course => {
        const checked = selectedStartingCourses.has(course.courseCode) ? "checked" : "";
        return `
            <label class="course-choice">
                <input type="checkbox" value="${escapeHtml(course.courseCode)}" ${checked}>
                <span><strong>${escapeHtml(course.courseCode)}</strong> · ${escapeHtml(course.title)}</span>
            </label>`;
    }).join("");

    container.querySelectorAll("input[type='checkbox']").forEach(input => {
        input.addEventListener("change", () => {
            if (input.checked) selectedStartingCourses.add(input.value);
            else selectedStartingCourses.delete(input.value);
            renderAvailableCourses();
        });
    });
}

function renderAvailableCourses() {
    const grid = document.getElementById("availableCoursesGrid");
    const query = (document.getElementById("studentCourseSearch")?.value || "").trim().toLowerCase();
    const filtered = courses.filter(course =>
        [course.courseCode, course.title, course.department].some(value => String(value || "").toLowerCase().includes(query))
    );

    if (!filtered.length) {
        grid.innerHTML = '<p class="empty-state">No matching courses found.</p>';
        return;
    }

    const enrolledCodes = new Set(activeEnrollment?.courseCodes || []);

    grid.innerHTML = filtered.map(course => {
        const enrolled = enrolledCodes.has(course.courseCode);
        const selected = selectedStartingCourses.has(course.courseCode);
        let buttonText = "+ Add";
        let buttonClass = "add-course-button";
        let disabled = "";

        if (enrolled) {
            buttonText = "Added";
            buttonClass = "added-course-button";
            disabled = "disabled";
        } else if (!activeEnrollment && selected) {
            buttonText = "Selected";
            buttonClass = "added-course-button";
        }

        return `
            <div class="course-card">
                <div class="course-top">
                    <span class="course-code">${escapeHtml(course.courseCode)}</span>
                    <span class="course-unit">${escapeHtml(course.creditUnit)} Units</span>
                </div>
                <h3>${escapeHtml(course.title)}</h3>
                <p>${escapeHtml(course.department)}</p>
                <div class="course-card-footer">
                    <span class="${enrolled ? "enrolled-label" : "available-label"}">${enrolled ? "Enrolled" : "Available"}</span>
                    <button class="${buttonClass}" data-course-code="${escapeHtml(course.courseCode)}" ${disabled}>${buttonText}</button>
                </div>
            </div>`;
    }).join("");

    grid.querySelectorAll("button[data-course-code]:not([disabled])").forEach(button => {
        button.addEventListener("click", () => handleCourseAdd(button.dataset.courseCode));
    });
}

async function handleCourseAdd(courseCode) {
    if (!activeEnrollment) {
        if (selectedStartingCourses.has(courseCode)) selectedStartingCourses.delete(courseCode);
        else selectedStartingCourses.add(courseCode);
        renderEnrollmentCourseChoices();
        renderAvailableCourses();
        showToast(selectedStartingCourses.has(courseCode)
            ? `${courseCode} selected for your new enrollment`
            : `${courseCode} removed from selection`);
        return;
    }

    try {
        await apiRequest(`/add-course/${encodeURIComponent(activeEnrollment.enrollmentId)}/${encodeURIComponent(courseCode)}`, {
            method: "PUT",
            headers: { email: localStorage.getItem("email") }
        });
        showToast(`${courseCode} added successfully`);
        await loadEnrollments();
        renderAllStudentData();
    } catch (error) {
        showToast(error.message || "Could not add course", "error");
    }
}

async function createEnrollment(event) {
    event.preventDefault();

    const session = Number(document.getElementById("session").value);
    const semester = document.getElementById("semester").value;
    const courseCodes = [...selectedStartingCourses];
    const button = event.currentTarget.querySelector("button[type='submit']");
    const originalText = button.textContent;

    if (!courseCodes.length) {
        showToast("Select at least one starting course", "error");
        return;
    }

    try {
        button.disabled = true;
        button.textContent = "Creating...";

        await apiRequest("/create-enrollment", {
            method: "POST",
            body: JSON.stringify({
                session,
                semester,
                courseCodes,
                studentId: student.studentId
            })
        });

        selectedStartingCourses.clear();
        event.currentTarget.reset();
        showToast("Enrollment created successfully");
        await loadEnrollments();
        renderAllStudentData();
    } catch (error) {
        showToast(error.message || "Could not create enrollment", "error");
    } finally {
        button.disabled = false;
        button.textContent = originalText;
    }
}

function renderCurrentEnrollment() {
    const body = document.getElementById("currentEnrollmentTableBody");
    const title = document.getElementById("activeEnrollmentTitle");
    const status = document.getElementById("enrollmentStatus");
    const text = document.getElementById("activeEnrollmentText");
    const subtitle = document.getElementById("currentEnrollmentSubtitle");

    if (!activeEnrollment) {
        status.textContent = "● NO ACTIVE ENROLLMENT";
        title.textContent = "Create an enrollment to begin";
        text.textContent = "Choose a session, semester and starting courses above.";
        subtitle.textContent = "Your current enrolled courses will appear here.";
        body.innerHTML = '<tr><td colspan="5" class="empty-cell">No active enrollment.</td></tr>';
        return;
    }

    const semesterText = formatSemester(activeEnrollment.semester);
    status.textContent = "● ACTIVE ENROLLMENT";
    title.textContent = `${activeEnrollment.session} — ${semesterText}`;
    text.textContent = "You can add or remove courses from this enrollment.";
    subtitle.textContent = `Courses registered for ${activeEnrollment.session} ${semesterText}.`;

    const rows = (activeEnrollment.courseCodes || []).map(code => {
        const course = courses.find(item => item.courseCode === code);
        return `
            <tr>
                <td><strong>${escapeHtml(code)}</strong></td>
                <td>${escapeHtml(course?.title || "Course")}</td>
                <td>${escapeHtml(course?.department || "—")}</td>
                <td>${escapeHtml(course?.creditUnit ?? "—")}</td>
                <td><button class="remove-course-button" data-remove-code="${escapeHtml(code)}">Remove</button></td>
            </tr>`;
    }).join("");

    body.innerHTML = rows || '<tr><td colspan="5" class="empty-cell">No courses in this enrollment.</td></tr>';

    body.querySelectorAll("button[data-remove-code]").forEach(button => {
        button.addEventListener("click", () => removeCourse(button.dataset.removeCode));
    });
}

async function removeCourse(courseCode) {
    if (!activeEnrollment) return;

    try {
        await apiRequest(`/remove-course/${encodeURIComponent(activeEnrollment.enrollmentId)}/${encodeURIComponent(courseCode)}`, {
            method: "PUT",
            headers: { email: localStorage.getItem("email") }
        });
        showToast(`${courseCode} removed successfully`);
        await loadEnrollments();
        renderAllStudentData();
    } catch (error) {
        showToast(error.message || "Could not remove course", "error");
    }
}

function renderEnrollmentHistory() {
    const container = document.getElementById("studentHistoryList");

    if (!enrollments.length) {
        container.innerHTML = '<p class="empty-state">No enrollment history yet.</p>';
        return;
    }

    const sorted = [...enrollments].sort((a, b) => Number(b.session || 0) - Number(a.session || 0));

    container.innerHTML = sorted.map(enrollment => `
        <div class="history-card">
            <div class="history-main">
                <div class="history-icon">📚</div>
                <div>
                    <span class="history-session">${escapeHtml(enrollment.session)}</span>
                    <h3>${escapeHtml(formatSemester(enrollment.semester).toUpperCase())}</h3>
                    <p>${escapeHtml((enrollment.courseCodes || []).join(", ") || "No courses")}</p>
                </div>
            </div>
            <span class="history-count">${(enrollment.courseCodes || []).length} Courses</span>
        </div>`).join("");
}

function renderStudentStats() {
    const enrolledCodes = activeEnrollment?.courseCodes || [];
    const totalUnits = enrolledCodes.reduce((total, code) => {
        const course = courses.find(item => item.courseCode === code);
        return total + Number(course?.creditUnit || 0);
    }, 0);

    document.getElementById("currentSession").textContent = activeEnrollment?.session ?? "—";
    document.getElementById("currentSemester").textContent = activeEnrollment ? activeEnrollment.semester : "NO ENROLLMENT";
    document.getElementById("enrolledCourseCount").textContent = enrolledCodes.length;
    document.getElementById("availableCourseCount").textContent = Math.max(0, courses.length - enrolledCodes.length);
    document.getElementById("summaryCourseCount").textContent = enrolledCodes.length;
    document.getElementById("summaryUnits").textContent = totalUnits;
}

function formatSemester(value) {
    if (value === "FIRST_SEMESTER") return "First Semester";
    if (value === "SECOND_SEMESTER") return "Second Semester";
    return value || "Semester";
}

async function logoutStudent() {
    const email = localStorage.getItem("email");

    try {
        if (email) {
            await apiRequest(`/logout/${encodeURIComponent(email)}`, { method: "POST" });
        }
    } catch (_) {
        // Still clear the local browser session even if the backend is unavailable.
    } finally {
        clearSession();
        window.location.href = "login.html";
    }
}
