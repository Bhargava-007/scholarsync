import { api } from "./api.js";
import { setStatus, taskCard } from "./utils.js";

async function refresh() {
    try {
        const tasksData = await api.getTasks();
        const tasks = tasksData.tasks || [];

        const todo = tasks.filter(t => t.status === "todo");
        const doing = tasks.filter(t => t.status === "doing");
        const done = tasks.filter(t => t.status === "done");

        document.getElementById("todoList").innerHTML = todo.length ? todo.map(taskCard).join("") : '<p>No tasks</p>';
        document.getElementById("doingList").innerHTML = doing.length ? doing.map(taskCard).join("") : '<p>No tasks</p>';
        document.getElementById("doneList").innerHTML = done.length ? done.map(taskCard).join("") : '<p>No tasks</p>';

        const k = document.getElementById("topKInput").value || 3;
        const topData = await api.getTopTasks(k);
        const top = topData.tasks || [];

        document.getElementById("topList").innerHTML = top.map((t, i) =>
            `<div class="top-item">#${i + 1} ${t.title} (P${t.priority})</div>`
        ).join("") || '<p>No tasks</p>';

    } catch (err) {
        setStatus("statusText", "Error: " + err.message, true);
    }
}

document.addEventListener("click", async (e) => {
    const id = e.target.dataset.id;
    const move = e.target.dataset.move;
    const del = e.target.dataset.delete;

    if (move && id) await api.moveTask(id, move);
    if (del) await api.deleteTask(del);
    if (move || del) refresh();
});

document.getElementById("loadTopBtn")?.addEventListener("click", refresh);

refresh();
