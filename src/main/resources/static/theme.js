// Dark/light theme. Load right after the Tailwind CDN script, in <head>, so the saved theme is applied
// before the page paints. <html class="dark"> is the default; the light theme just removes that class.
(function () {
    const families = ['slate', 'fuchsia', 'pink', 'rose', 'amber', 'emerald', 'cyan', 'sky', 'blue', 'purple'];
    const shades = [50, 100, 200, 300, 400, 500, 600, 700, 800, 900, 950];
    const colors = { white: 'rgb(var(--white) / <alpha-value>)' };
    families.forEach(family => {
        colors[family] = {};
        shades.forEach(shade => colors[family][shade] = `rgb(var(--${family}-${shade}) / <alpha-value>)`);
    });
    tailwind.config = { darkMode: 'class', theme: { extend: { colors } } };

    const root = document.documentElement;
    // Dark for everyone; light only after the visitor picks it with the toggle.
    root.classList.toggle('dark', localStorage.getItem('theme') !== 'light');

    document.addEventListener('DOMContentLoaded', () => {
        document.querySelectorAll('[data-theme-toggle]').forEach(button => {
            button.addEventListener('click', () => {
                const dark = root.classList.toggle('dark');
                localStorage.setItem('theme', dark ? 'dark' : 'light');
            });
        });
    });
})();
