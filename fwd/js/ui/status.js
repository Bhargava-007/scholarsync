export function setStatus(elementId, message, isError) {
  const statusElement = document.getElementById(elementId);
  if (!statusElement) {
    return;
  }

  statusElement.textContent = message;
  statusElement.style.color = isError ? "#b42318" : "#1d7c4d";
}
