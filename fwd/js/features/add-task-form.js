import { addTask } from "../api/tasks-api.js";
import { setStatus } from "../ui/status.js";

function readTaskFromForm() {
  return {
    id: document.getElementById("taskId").value.trim(),
    title: document.getElementById("taskTitle").value.trim(),
    subject: document.getElementById("taskSubject").value.trim(),
    priority: Number(document.getElementById("taskPriority").value),
    deadline: document.getElementById("taskDeadline").value,
  };
}

async function onSubmit(event) {
  event.preventDefault();

  try {
    const task = readTaskFromForm();
    await addTask(task);
    setStatus("statusText", "Task added successfully.", false);
    event.target.reset();
  } catch (err) {
    setStatus("statusText", err.message, true);
  }
}

export function initAddTaskForm() {
  const form = document.getElementById("taskForm");
  if (!form) {
    return;
  }

  form.addEventListener("submit", onSubmit);
}
