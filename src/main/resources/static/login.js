const loginForm = document.getElementById("loginForm");
const message = document.getElementById("message");

loginForm.addEventListener("submit", async function (event) {
    event.preventDefault();

    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value;
    const button = loginForm.querySelector("button[type='submit']");
    const originalText = button.textContent;

    try {
        button.disabled = true;
        button.textContent = "Logging in...";
        message.textContent = "";

        const data = await apiRequest("/login", {
            method: "POST",
            body: JSON.stringify({ email, password })
        });

        clearSession();
        localStorage.setItem("email", data.email || email);
        localStorage.setItem("role", data.role);

        message.textContent = data.message || "Login successful";
        message.className = "success-message";

        if (data.role === "ADMIN") {
            window.location.href = "admin-dashboard.html";
            return;
        }

        if (data.role === "STUDENT") {
            try {
                const student = await apiRequest(`/get-student/${encodeURIComponent(data.email || email)}`);
                if (student.studentId) {
                    localStorage.setItem("studentId", student.studentId);
                }
            } catch (_) {
                // The dashboard will fetch the profile again.
            }

            window.location.href = "student-dashboard.html";
            return;
        }

        throw new Error("Unknown account role");
    } catch (error) {
        clearSession();
        message.textContent = error.message || "Login failed";
        message.className = "error-message";
    } finally {
        button.disabled = false;
        button.textContent = originalText;
    }
});
