import { deleteTask, getTasks, getTopTasks, moveTask } from "../api/tasks-api.js";
import { setStatus } from "../ui/status.js";
import { renderBoard, renderTop } from "../ui/tasks-view.js";

async function refreshBoard() {
  const data = await getTasks();
  renderBoard(data.tasks || []);
}

async function refreshTop() {
  const count = Number(document.getElementById("topKInput").value || 3);
  const data = await getTopTasks(count);
  renderTop(data.tasks || []);
}

async function onBoardClick(event) {
  const id = event.target.getAttribute("data-id") || event.target.getAttribute("data-delete");
  const nextStatus = event.target.getAttribute("data-move");
  const deleteId = event.target.getAttribute("data-delete");

  if (!id) {
    return;
  }

  try {
    if (nextStatus) {
      await moveTask(id, nextStatus);
    } else if (deleteId) {
      await deleteTask(deleteId);
    }

    await refreshBoard();
    await refreshTop();
    setStatus("statusText", "", false);
  } catch (err) {
    setStatus("statusText", err.message, true);
  }
}

function bindBoardEvents() {
  document.getElementById("todoList").addEventListener("click", onBoardClick);
  document.getElementById("doingList").addEventListener("click", onBoardClick);
  document.getElementById("doneList").addEventListener("click", onBoardClick);
  document.getElementById("loadTopBtn").addEventListener("click", refreshTop);
}

export async function initTasksBoard() {
  bindBoardEvents();
  try {
    await refreshBoard();
    await refreshTop();
  } catch (err) {
    setStatus("statusText", "Start Java server first: " + err.message, true);
  }
}
