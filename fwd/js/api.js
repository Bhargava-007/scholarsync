const BASE = "http://localhost:8080";

async function request(path, options = {}) {
    const res = await fetch(`${BASE}${path}`, {
        headers: { "Content-Type": "application/json" },
        ...options,
    });

    let data = null;
    try {
        data = await res.json();
    } catch (err) {
        data = null;
    }

    if (!res.ok) {
        throw new Error((data && data.error) || "Request failed");
    }

    return data || {};
}

export const api = {
    getTasks: () => request("/api/tasks"),
    addTask: (task) => request("/api/tasks", {
        method: "POST",
        body: JSON.stringify(task),
    }),
    moveTask: (id, to) => request(`/api/tasks/${encodeURIComponent(id)}/move?to=${encodeURIComponent(to)}`, {
        method: "PUT",
    }),
    deleteTask: (id) => request(`/api/tasks/${encodeURIComponent(id)}`, { method: "DELETE" }),
    getTopTasks: (k) => request(`/api/tasks/top?k=${encodeURIComponent(k)}`),
    login: (credentials) => request("/api/login", {
        method: "POST",
        body: JSON.stringify(credentials),
    }),
    register: (credentials) => request("/api/register", {
        method: "POST",
        body: JSON.stringify(credentials),
    }),
};
