    # ScholarSync Frontend Structure

    ## Folders
    - `fwd/index.html`: Main landing page.
    - `fwd/pages/`: Feature pages (`add-task.html`, `tasks.html`).
    - `fwd/css/`: Layered styles.
    - `fwd/js/api/`: API client modules.
    - `fwd/js/ui/`: Reusable UI rendering and helpers.
    - `fwd/js/features/`: Feature-level logic.
    - `fwd/js/pages/`: Page entry scripts only.

    ## CSS layering
    - `variables.css`: Color/system tokens.
    - `base.css`: Element defaults and typography.
    - `layout.css`: Grid/layout and responsive behavior.
    - `components.css`: Reusable visual components.
    - `pages.css`: Page-specific overrides.
    - `main.css`: Single stylesheet imported by HTML.

    ## JS pattern for new pages
    1. Add HTML in `fwd/pages/`.
    2. Add feature logic in `fwd/js/features/`.
    3. Reuse helpers from `fwd/js/ui/` and API from `fwd/js/api/`.
    4. Create a tiny page entry file in `fwd/js/pages/`.
    5. Link that entry file in the page HTML.
