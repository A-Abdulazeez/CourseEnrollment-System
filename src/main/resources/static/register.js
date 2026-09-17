const registerForm = document.getElementById("registerForm");
const message = document.getElementById("message");

registerForm.addEventListener("submit", async function (event) {
    event.preventDefault();

    const button = registerForm.querySelector("button[type='submit']");
    const originalText = button.textContent;

    const student = {
        name: document.getElementById("name").value.trim(),
        email: document.getElementById("email").value.trim(),
        password: document.getElementById("password").value,
        department: document.getElementById("department").value.trim()
    };

    try {
        button.disabled = true;
        button.textContent = "Creating account...";
        message.textContent = "";

        const data = await apiRequest("/register", {
            method: "POST",
            body: JSON.stringify(student)
        });

        message.textContent = "Registration successful. Redirecting to login...";
        message.className = "success-message";
        registerForm.reset();

        setTimeout(() => {
            window.location.href = "login.html";
        }, 700);
    } catch (error) {
        message.textContent = error.message || "Registration failed";
        message.className = "error-message";
    } finally {
        button.disabled = false;
        button.textContent = originalText;
    }
});
