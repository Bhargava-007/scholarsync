import { renderDashboard } from "./pages/dashboard.js";
import { renderTasks } from "./pages/tasks.js";
import { renderSubjects } from "./pages/subjects.js";
import { renderResources } from "./pages/resources.js";
import { renderSmartplan } from "./pages/smartplan.js";
import { renderSettings } from "./pages/settings.js";

const routes = {
  dashboard: {
    path: "#/dashboard",
    navLabel: "Dashboard",
    title: "Dashboard",
    render: renderDashboard,
  },
  tasks: {
    path: "#/tasks",
    navLabel: "Tasks",
    title: "Tasks",
    render: renderTasks,
  },
  subjects: {
    path: "#/subjects",
    navLabel: "Subjects",
    title: "Subjects",
    render: renderSubjects,
  },
  resources: {
    path: "#/resources",
    navLabel: "Resources",
    title: "Resources",
    render: renderResources,
  },
  smartplan: {
    path: "#/smartplan",
    navLabel: "Smart Plan",
    title: "Smart Plan",
    render: renderSmartplan,
  },
  settings: {
    path: "#/settings",
    navLabel: "Settings",
    title: "Settings",
    render: renderSettings,
  },
};

function normalizeRouteKey(hash) {
  const key = hash.replace(/^#\//, "").trim().toLowerCase();
  return routes[key] ? key : "dashboard";
}

function getCurrentRoute() {
  return routes[normalizeRouteKey(window.location.hash)];
}

function ensureDefaultRoute() {
  if (!window.location.hash || normalizeRouteKey(window.location.hash) === "dashboard") {
    if (window.location.hash !== "#/dashboard") {
      window.location.hash = "#/dashboard";
    }
  }
}

function getRouteEntries() {
  return Object.values(routes).map((route) => ({
    path: route.path,
    label: route.navLabel,
  }));
}

function initRouter(onChange) {
  const handleRouteChange = () => {
    const route = getCurrentRoute();
    onChange(route);
  };

  window.addEventListener("hashchange", handleRouteChange);
  ensureDefaultRoute();
  handleRouteChange();
}

export { getRouteEntries, initRouter };
