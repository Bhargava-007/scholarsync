import { api } from "./api.js";
import { setStatus } from "./utils.js";



document.getElementById("loginForm")?.addEventListener("submit", async (e) => {
    e.preventDefault();
    const username = document.getElementById("username").value.trim();
    const password = document.getElementById("password").value.trim();

    try {
        const res = await api.login({ username, password });
        if (res.success) {
            localStorage.setItem("user", username);

            window.location.href = "../index.html";
        }
    } catch (err) {
        setStatus("statusText", err.message, true);
    }
});
