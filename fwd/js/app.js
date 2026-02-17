import { getRouteEntries, initRouter } from "./router.js";
import { THEMES, getSavedTheme, setSavedTheme } from "./store.js";

function applyTheme(theme) {
  document.documentElement.setAttribute("data-theme", theme);
}

function renderShell() {
  const root = document.getElementById("app");
  const navItems = getRouteEntries()
    .map((route) => `<a class="nav-item" href="${route.path}" data-path="${route.path}">${route.label}</a>`)
    .join("");

  root.innerHTML = `
    <div class="app-shell">
      <aside class="sidebar">
        <div class="brand">ScholarSync</div>
        <nav class="nav-list" aria-label="Primary navigation">
          ${navItems}
        </nav>
      </aside>
      <div class="main-column">
        <header class="topbar">
          <h1 class="topbar-title" id="topbar-title">Dashboard</h1>
          <button class="btn" id="theme-toggle" type="button" aria-label="Toggle theme">Toggle Theme</button>
        </header>
        <main class="page-wrap" id="page-content" tabindex="-1"></main>
      </div>
    </div>
  `;
}

function setActiveNav(path) {
  const items = document.querySelectorAll(".nav-item");
  items.forEach((item) => {
    const isActive = item.getAttribute("data-path") === path;
    item.classList.toggle("active", isActive);
    item.setAttribute("aria-current", isActive ? "page" : "false");
  });
}

function updateThemeButton(theme) {
  const button = document.getElementById("theme-toggle");
  if (!button) {
    return;
  }
  button.textContent = theme === THEMES.DARK ? "Switch to Light" : "Switch to Dark";
}

function initTheme() {
  const theme = getSavedTheme();
  applyTheme(theme);
  updateThemeButton(theme);

  const button = document.getElementById("theme-toggle");
  button.addEventListener("click", () => {
    const nextTheme =
      document.documentElement.getAttribute("data-theme") === THEMES.DARK ? THEMES.LIGHT : THEMES.DARK;
    applyTheme(nextTheme);
    setSavedTheme(nextTheme);
    updateThemeButton(nextTheme);
  });
}

function initApp() {
  renderShell();
  initTheme();

  initRouter((route) => {
    const title = document.getElementById("topbar-title");
    const content = document.getElementById("page-content");
    title.textContent = route.title;
    content.innerHTML = route.render();
    setActiveNav(route.path);
    content.focus();
  });
}

initApp();
