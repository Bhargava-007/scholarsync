export function updateNav() {
    const nav = document.getElementById("mainNav");
    if (!nav) return;

    const user = localStorage.getItem("user");
    const isHome = window.location.pathname.endsWith("index.html") || window.location.pathname === "/";
    const prefix = isHome ? "pages/" : "";
    const homePrefix = isHome ? "" : "../";

    let html = `<a class="nav-link ${isHome ? 'active' : ''}" href="${homePrefix}index.html">Home</a>`;
    html += `<a class="nav-link ${window.location.pathname.includes('add-task') ? 'active' : ''}" href="${prefix}add-task.html">Add Task</a>`;
    html += `<a class="nav-link ${window.location.pathname.includes('tasks.html') ? 'active' : ''}" href="${prefix}tasks.html">Tasks Board</a>`;
    html += `<a class="nav-link ${window.location.pathname.includes('resources') ? 'active' : ''}" href="${prefix}resources.html">Resources</a>`;

    if (user) {
        html += `<span class="nav-user" style="margin-left:auto; font-weight:600; color:var(--brand); display:flex; align-items:center; gap:var(--space-md);">
                    ${user}
                    <button id="logoutBtn" class="btn-link ghost" style="padding:4px 12px; font-size:0.8rem;">Logout</button>
                 </span>`;
    } else {
        html += `<a class="nav-link ${window.location.pathname.includes('login') ? 'active' : ''}" href="${prefix}login.html">Login</a>`;
        html += `<a class="nav-link ${window.location.pathname.includes('register') ? 'active' : ''}" href="${prefix}register.html">Register</a>`;
    }

    nav.innerHTML = html;

    document.getElementById("logoutBtn")?.addEventListener("click", () => {
        localStorage.removeItem("user");
        window.location.href = homePrefix + "index.html";
    });
}


updateNav();
