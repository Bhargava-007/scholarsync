function taskCard(task) {
  const priorityClass = task.priority >= 4 ? "priority-high" : task.priority >= 3 ? "priority-medium" : "priority-low";

  return `
    <div class="task">
      <h3>${task.title}</h3>
      <p>Subject: ${task.subject}</p>
      <div class="task-meta">
        <span class="priority-badge ${priorityClass}">P${task.priority}</span>
        <p>Due: ${task.deadline}</p>
      </div>
      <div class="actions">
        <button data-move="todo" data-id="${task.id}">To-Do</button>
        <button data-move="doing" data-id="${task.id}">Doing</button>
        <button data-move="done" data-id="${task.id}">Done</button>
        <button data-delete="${task.id}">Delete</button>
      </div>
    </div>
  `;
}

export function renderBoard(tasks) {
  const todo = tasks.filter((task) => task.status === "todo");
  const doing = tasks.filter((task) => task.status === "doing");
  const done = tasks.filter((task) => task.status === "done");

  document.getElementById("todoList").innerHTML = todo.length ? todo.map(taskCard).join("") : '<p class="empty">No tasks</p>';
  document.getElementById("doingList").innerHTML = doing.length ? doing.map(taskCard).join("") : '<p class="empty">No tasks</p>';
  document.getElementById("doneList").innerHTML = done.length ? done.map(taskCard).join("") : '<p class="empty">No tasks</p>';
}

export function renderTop(tasks) {
  const html = tasks
    .map((task, index) => `<div class="top-item">#${index + 1} ${task.title} (P${task.priority}, ${task.deadline})</div>`)
    .join("");

  document.getElementById("topList").innerHTML = html || '<p class="empty">No tasks</p>';
}
