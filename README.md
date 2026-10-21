# Julito III T. Tiro – Portfolio Website

My personal portfolio site: a single-page Spring Boot app that shows my skills, projects, work experience, education and certifications, plus a downloadable resume generated from the same content.

**Live site:** https://julitoiiitiro-portfolio.up.railway.app/

## Site sections

- **About:** name, headline, short summary and contact links
- **Skills:** grouped into Backend, Frontend, Databases & Data, Low-Code & Automation, and DevOps & Tools
- **Projects:** personal and capstone projects. Each card links to its own detail page at `/projects/<slug>` with an overview, a screenshot carousel (arrows, thumbnails, swipe and arrow keys; click to view fullscreen), key features, tech stack, challenges, lessons learned, and GitHub / Live Demo buttons when links exist
- **Experience:** work history
- **Education & Certifications:** degree, plus certifications split into Completed and In Progress
- **Resume:** an ATS-friendly resume at `/resume` (web page) and `/resume.pdf` (download), linked from the navbar and hero

## Tech stack

- Java 17, Spring Boot 4 (Spring Web)
- Thymeleaf for server-side templates
- Tailwind CSS and Font Awesome (loaded from CDN)
- OpenHTMLtoPDF and jsoup to turn the resume template into a PDF
- Docker, deployed on Railway

## Project structure

```
src/main/java/com/julitoiiitiro/portfolio/app/
├── PortfolioApplication.java            # Spring Boot entry point
├── content/Portfolio.java               # Java records bound to portfolio.yml
├── controller/PortfolioController.java  # Serves /, /resume and /resume.pdf
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
    └── projects/<slug>/                 # Screenshots for each project
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

- **Profile:** name, title, tagline, summary, contact details
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
      demo: "https://<app>.up.railway.app"
      youtube-id: ""            # unlisted demo video ID (optional)
      source-note: ""           # e.g. "Source code private (capstone project)" when there's no GitHub link
```

Screenshots appear in the carousel in the order listed. Empty fields are hidden on the page. Until `features` is filled in, the page shows the card's `highlights` as key features. A project's GitHub link also appears on the resume.

To check the resume, open http://localhost:8080/resume.pdf. It should fit on 1–2 A4 pages.

## Tests

```bash
./mvnw test
```

The tests check that `portfolio.yml` loads, that every project has a unique URL slug, and that the resume renders to a PDF.

## Contact

- GitHub: [github.com/jiiitiro](https://github.com/jiiitiro)
- Email: julitoiiitiro@gmail.com
