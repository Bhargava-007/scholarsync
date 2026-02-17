const THEME_KEY = "scholarsync:theme";
const THEMES = {
  LIGHT: "light",
  DARK: "dark",
};

function getSavedTheme() {
  const value = localStorage.getItem(THEME_KEY);
  if (value === THEMES.DARK || value === THEMES.LIGHT) {
    return value;
  }
  return THEMES.LIGHT;
}

function setSavedTheme(theme) {
  const normalized = theme === THEMES.DARK ? THEMES.DARK : THEMES.LIGHT;
  localStorage.setItem(THEME_KEY, normalized);
}

export { THEMES, getSavedTheme, setSavedTheme };
