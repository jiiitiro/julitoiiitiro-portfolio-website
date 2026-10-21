# Julito III T. Tiro – Portfolio Website

My personal portfolio site: a single-page Spring Boot app that shows my skills, projects, work experience, education and certifications, plus a downloadable resume generated from the same content.

**Live site:** https://julitoiiitiro-portfolio.up.railway.app/

## Site sections

- **About:** name, headline, short summary and contact links
- **Skills:** grouped into Backend, Frontend, Databases & Data, Low-Code & Automation, and DevOps & Tools
- **Projects:** personal and capstone projects
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
├── templates/resume.html                # The resume (web page and PDF)
└── static/images/                       # Profile photo
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

To check the resume, open http://localhost:8080/resume.pdf. It should fit on 1–2 A4 pages.

## Tests

```bash
./mvnw test
```

The tests check that `portfolio.yml` loads and that the resume renders to a PDF.

## Contact

- GitHub: [github.com/jiiitiro](https://github.com/jiiitiro)
- Email: julitoiiitiro@gmail.com
