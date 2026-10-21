# Julito III T. Tiro – Portfolio Website

My personal portfolio site: a single-page Spring Boot app that shows my skills, projects, work experience, education and certifications, plus a downloadable resume generated from the same content.

**Live site:** https://julitoiiitiro-portfolio.up.railway.app/

## Site sections

- **About:** name, headline, short summary and contact links (GitHub, LinkedIn, email; the phone number is only on the resume)
- **Skills:** grouped into Backend, Frontend, Databases & Data, Low-Code & Automation, and DevOps & Tools
- **Projects:** personal and capstone projects. Each card links to its own detail page at `/projects/<slug>` with an overview (or the project's README, synced live from GitHub), a screenshot carousel (arrows, thumbnails, swipe and arrow keys; click to view fullscreen), key features, tech stack, challenges, lessons learned, and GitHub / Live Demo buttons when links exist
- **Experience:** work history
- **Education & Certifications:** degree, plus certifications split into Completed and In Progress
- **Resume:** an ATS-friendly resume at `/resume` (web page) and `/resume.pdf` (download), linked from the navbar and hero
- **Sharing and search:** a 1200×630 preview image, Open Graph and Twitter tags for link previews (Messenger, LinkedIn), a meta description and canonical URL on every page, plus `/robots.txt` and a `/sitemap.xml` that lists every project page

## Tech stack

- Java 17, Spring Boot 4 (Spring Web)
- Thymeleaf for server-side templates
- Tailwind CSS and Font Awesome (loaded from CDN)
- OpenHTMLtoPDF and jsoup to turn the resume template into a PDF
- commonmark-java to render project READMEs from GitHub, sanitized with jsoup
- Docker, deployed on Railway

## Project structure

```
src/main/java/com/julitoiiitiro/portfolio/app/
├── PortfolioApplication.java            # Spring Boot entry point
├── content/Portfolio.java               # Java records bound to portfolio.yml
├── content/GitHubReadme.java            # Fetches, caches and renders project READMEs from GitHub
├── controller/PortfolioController.java  # Serves /, /projects/{slug}, /resume, /resume.pdf, /robots.txt and /sitemap.xml
└── resume/ResumePdfRenderer.java        # Renders resume.html to PDF
src/main/resources/
├── portfolio.yml                        # ALL site and resume content lives here
├── application.properties               # Imports portfolio.yml
├── templates/index.html                 # The portfolio page
├── templates/project-detail.html        # Project detail page (/projects/<slug>)
├── templates/resume.html                # The resume (web page and PDF)
├── templates/error/404.html             # Not-found page
└── static/images/
    ├── portfolio_pic.png                # Profile photo
    ├── og-image.png                     # 1200×630 link-preview image
    └── projects/<slug>/                 # Screenshots for each project
.github/workflows/site-check.yml          # Monthly check of the live site
scripts/check-site.sh                     # Checks pages, links and the resume PDF
```

## Run locally

You need Java 17 or newer. Maven isn't required because the project includes the Maven wrapper.

```bash
./mvnw spring-boot:run
```

Then open http://localhost:8080.

## Run with Docker

```bash
docker compose up --build
```

The site runs at http://localhost:8080.

## Build a JAR

```bash
./mvnw clean package
java -jar target/julitoiiitiro.portfolio-0.0.1-SNAPSHOT.jar
```

## Updating content

All text lives in one file: `src/main/resources/portfolio.yml`. Edit it, then rebuild or redeploy, and both the website and the resume PDF update. You don't need to touch the HTML.

- **Profile:** name, title, tagline, summary, contact details. `phone` appears only on the resume. Set `linkedin` to your profile URL to show LinkedIn buttons in the navbar, hero, footer and resume; while it's blank they stay hidden.
- **Skills, projects, experience, education:** add, remove or reorder list entries
- **Certifications:** set `status: completed` or `status: in-progress`. When you finish one, change its status. Add `url: "https://..."` to link it to its certificate.
- `color` and `icon` only affect the website's accents; the resume ignores them.

### Adding project details and screenshots

1. Take 3–6 screenshots (login, dashboard, main features) and compress each to under ~300 KB (WebP or PNG).
2. Put them in `src/main/resources/static/images/projects/<slug>/`, e.g. `static/images/projects/foodiefleet/dashboard.webp`.
3. In `portfolio.yml`, fill in that project's fields:

```yaml
      overview: "What it is, who it's for and the problem it solves."
      features: ["Feature one", "Feature two"]
      tech-stack: ["Java", "Spring Boot", "PostgreSQL"]
      challenges: ["A problem you hit and how you solved it"]
      lessons: ["What you learned"]
      screenshots:
        - file: "dashboard.webp"
          caption: "Dashboard"
      github: "https://github.com/jiiitiro/<repo>"
      readme: "https://github.com/jiiitiro/<repo>/blob/main/README.md"   # optional, see below
      demo: "https://<app>.up.railway.app"
      youtube-id: ""            # unlisted demo video ID (optional)
      source-note: ""           # e.g. "Source code private (capstone project)" when there's no GitHub link
```

Screenshots appear in the carousel in the order listed. Empty fields are hidden on the page. Until `features` is filled in, the page shows the card's `highlights` as key features. A project's GitHub link also appears on the resume.

### Live README from GitHub

Set `readme:` to a project's README link on GitHub (`https://github.com/<user>/<repo>/blob/<branch>/README.md`) and its detail page shows that README, converted from Markdown, in place of `overview` and `features`. When you push a README change to that branch, the portfolio picks it up within 10 minutes, with no redeploy needed.

- Relative links and images in the README are pointed at GitHub, and unsafe HTML is stripped.
- If GitHub can't be reached, the page shows the last copy it fetched, or the `overview` and `features` from `portfolio.yml` if it has none yet. Keep those filled in as a fallback.
- The link must point at the branch visitors should see (usually `main`). Changes pushed to other branches don't show up.

To check the resume, open http://localhost:8080/resume.pdf. It should fit on 1–2 A4 pages.

### Share preview image

`static/images/og-image.png` is the picture shown when the site link is pasted into Messenger, LinkedIn and similar apps. It's a 1200×630 card with your name, headline and photo. If your name, headline or photo changes, replace it with a new 1200×630 image. Project pages use their first screenshot instead, when they have one. After deploying, you can check the preview with the [LinkedIn Post Inspector](https://www.linkedin.com/post-inspector/) or the [Facebook Sharing Debugger](https://developers.facebook.com/tools/debug/), which also covers Messenger and refreshes its cached copy.

## Tests

```bash
./mvnw test
```

The tests check that `portfolio.yml` loads, that every project has a unique URL slug, that the resume renders to a PDF, that the sitemap lists every project page, and that GitHub READMEs are rendered safely (links resolved, unsafe HTML removed).

## Keeping it current

The site and the resume PDF both come from `portfolio.yml`, so most updates are a one-file edit followed by a push to `main`, which Railway deploys.

**When you finish a certificate**
1. Change its `status` to `completed` and add `url:` with the certificate link.
2. Push, then open `/resume.pdf` on the live site to confirm it moved to Completed.

**When you finish a project**
1. Add it under `projects` with a new `slug`, and put screenshots in `static/images/projects/<slug>/`.
2. If the repo is public, set `github:` and `readme:` so the page follows its README.
3. Push, then open `/projects/<slug>` and check the card on the homepage.

**Every month (automatic)**

The [Monthly site check](.github/workflows/site-check.yml) workflow runs on the 1st of every month. It loads every page in the sitemap, checks every link and image on them (including GitHub repos and live demos), and confirms the resume PDF generates. GitHub emails you if a run fails; open the run in the Actions tab to see which URL broke. You can also start it any time from the Actions tab with **Run workflow**, or run the same check yourself:

```bash
scripts/check-site.sh                          # the live site
scripts/check-site.sh http://localhost:8080    # a local copy
```

GitHub pauses scheduled workflows in a repo with no commits for 60 days. If that happens, re-enable it from the Actions tab.

**Before each job application**

Tailor the `summary` (and, if useful, the order of `skills` and `projects`) to the role, then run the app locally and download http://localhost:8080/resume.pdf. That gives you a tailored PDF without changing the live site. Keep the live `summary` general.

## Contact

- GitHub: [github.com/jiiitiro](https://github.com/jiiitiro)
- Email: julitoiiitiro@gmail.com
