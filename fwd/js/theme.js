// 1. Immediate initialization: Read and apply theme index to <html> to prevent flash.
// This should ideally be called in a script in the <head>.
(function() {
    const themes = ["lakeside", "ethereal", "forest", "misty", "wild", "golden"];
    const themeIndex = parseInt(localStorage.getItem('themeIndex')) || 0;
    const theme = themes[themeIndex];
    if (theme === "lakeside") {
        document.documentElement.removeAttribute("data-theme");
    } else {
        document.documentElement.setAttribute("data-theme", theme);
    }
})();

const themes = ["lakeside", "ethereal", "forest", "misty", "wild", "golden"];
const themeLabels = {
    "lakeside": "🌊 Lakeside Serenity",
    "ethereal": "🌅 Ethereal Morning",
    "forest": "🌲 The Forest Heart",
    "misty": "🌫️ Misty Journey",
    "wild": "🦋 Wild Burst",
    "golden": "🌾 Golden Harvest"
};

const themeImages = {
    "lakeside": "image_1.jpg",
    "ethereal": "image_0.jpg",
    "forest": "image_2.jpg",
    "misty": "image_3.jpg",
    "wild": "image_4.jpg",
    "golden": "image_5.jpg"
};

let currentThemeIndex = parseInt(localStorage.getItem('themeIndex')) || 0;

function applyTheme() {
    const theme = themes[currentThemeIndex];
    if (theme === "lakeside") {
        document.documentElement.removeAttribute("data-theme");
    } else {
        document.documentElement.setAttribute("data-theme", theme);
    }
    const toggleBtn = document.getElementById("themeToggleBtn");
    if (toggleBtn) {
        toggleBtn.textContent = themeLabels[theme];
    }
}

function toggleTheme() {
    currentThemeIndex = (currentThemeIndex + 1) % themes.length;
    localStorage.setItem('themeIndex', currentThemeIndex);
    applyTheme();
}

function preloadImages() {
    const isHome = window.location.pathname.endsWith("index.html") || window.location.pathname === "/";
    const imagePrefix = isHome ? "assets/" : "../assets/";
    Object.values(themeImages).forEach(img => {
        const i = new Image();
        i.src = `${imagePrefix}${img}`;
    });
}

preloadImages();
// We still call applyTheme once to handle the toggleBtn text when the script loads at page end
applyTheme();
