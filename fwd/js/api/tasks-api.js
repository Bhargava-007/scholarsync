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

export function getTasks() {
  return request("/api/tasks");
}

export function addTask(task) {
  return request("/api/tasks", {
    method: "POST",
    body: JSON.stringify(task),
  });
}

export function moveTask(id, to) {
  return request(`/api/tasks/${encodeURIComponent(id)}/move?to=${encodeURIComponent(to)}`, {
    method: "PUT",
  });
}

export function deleteTask(id) {
  return request(`/api/tasks/${encodeURIComponent(id)}`, { method: "DELETE" });
}

export function getTopTasks(k) {
  return request(`/api/tasks/top?k=${encodeURIComponent(k)}`);
}
