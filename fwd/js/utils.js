export function setStatus(elementId, message, isError) {
    const el = document.getElementById(elementId);
    if (!el) return;
    el.textContent = message;
    el.style.color = isError ? "var(--danger-text)" : "var(--success-text)";
}

export function taskCard(task) {
    const pClass = task.priority >= 4 ? "priority-high" : task.priority >= 3 ? "priority-medium" : "priority-low";
    return `
    <div class="task ${pClass}">
      <h3>${task.title}</h3>
      <p>Subject: ${task.subject}</p>
      <div class="task-meta">
        <span class="priority-badge">P${task.priority}</span>
        <p>Due: ${task.deadline}</p>
      </div>
      <div class="actions">
        <button data-move="todo" data-id="${task.id}">To-Do</button>
        <button data-move="doing" data-id="${task.id}">Doing</button>
        <button data-move="done" data-id="${task.id}">Done</button>
        <button data-delete="${task.id}" style="background:var(--danger-bg); color:var(--danger-text)">Delete</button>
      </div>
    </div>
  `;
}
