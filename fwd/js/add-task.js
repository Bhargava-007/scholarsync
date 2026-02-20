import { api } from "./api.js";
import { setStatus } from "./utils.js";

document.getElementById("taskForm")?.addEventListener("submit", async (e) => {
    e.preventDefault();
    const task = {
        id: document.getElementById("taskId").value,
        title: document.getElementById("taskTitle").value,
        subject: document.getElementById("taskSubject").value,
        priority: Number(document.getElementById("taskPriority").value),
        deadline: document.getElementById("taskDeadline").value,
    };

    try {
        await api.addTask(task);
        setStatus("statusText", "Task added!", false);
        e.target.reset();
    } catch (err) {
        setStatus("statusText", err.message, true);
    }
});
