const API_BASE = window.location.port === "2002" ? "" : "/api";

async function apiRequest(path, options = {}) {
    const requestOptions = {
        ...options,
        headers: {
            ...(options.body ? { "Content-Type": "application/json" } : {}),
            ...(options.headers || {})
        }
    };

    const response = await fetch(`${API_BASE}${path}`, requestOptions);
    const contentType = response.headers.get("content-type") || "";
    let data;

    if (contentType.includes("application/json")) {
        data = await response.json();
    } else {
        data = await response.text();
    }

    if (!response.ok) {
        let message = "Request failed";

        if (typeof data === "string" && data.trim()) {
            message = data;
        } else if (data) {
            message = data.message || data.detail || data.error || message;
        }

        const error = new Error(message);
        error.status = response.status;
        error.data = data;
        throw error;
    }

    return data;
}

function showToast(message, type = "success") {
    let container = document.getElementById("toastContainer");

    if (!container) {
        container = document.createElement("div");
        container.id = "toastContainer";
        container.className = "toast-container";
        document.body.appendChild(container);
    }

    const toast = document.createElement("div");
    toast.className = `toast ${type === "error" ? "toast-error" : "toast-success"}`;
    toast.textContent = message;
    container.appendChild(toast);

    setTimeout(() => toast.classList.add("toast-visible"), 10);
    setTimeout(() => {
        toast.classList.remove("toast-visible");
        setTimeout(() => toast.remove(), 250);
    }, 3000);
}

function clearSession() {
    localStorage.removeItem("email");
    localStorage.removeItem("role");
    localStorage.removeItem("studentId");
}

function requireRole(role) {
    const currentRole = localStorage.getItem("role");
    const email = localStorage.getItem("email");

    if (!email || currentRole !== role) {
        window.location.href = "login.html";
        return false;
    }

    return true;
}

function escapeHtml(value) {
    return String(value ?? "")
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}
