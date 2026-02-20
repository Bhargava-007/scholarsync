import { api } from "./api.js";
import { setStatus } from "./utils.js";

document.getElementById("registerForm")?.addEventListener("submit", async (e) => {
    e.preventDefault();
    const username = document.getElementById("username").value.trim();
    const password = document.getElementById("password").value.trim();

    try {
        const res = await api.register({ username, password });
        if (res.success) {
            setStatus("statusText", "Registration successful! Redirecting to login...", false);
            setTimeout(() => {
                window.location.href = "login.html";
            }, 1500);
        }
    } catch (err) {
        setStatus("statusText", err.message, true);
    }
});
