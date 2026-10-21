# Julito III T. Tiro – Portfolio Website

My personal portfolio site: a single-page Spring Boot app that shows my skills, projects, work experience, education and certifications.

**Live site:** https://julitoiiitiro-portfolio.up.railway.app/

## Tech stack

- Java 17, Spring Boot 4 (Spring Web)
- Thymeleaf for server-side templates
- Tailwind CSS and Font Awesome (loaded from CDN)
- Docker, deployed on Railway

## Project structure

```
src/main/java/com/julitoiiitiro/portfolio/app/
├── PortfolioApplication.java          # Spring Boot entry point
└── controller/PortfolioController.java  # Serves "/" and sets profile details (name, title, contact)
src/main/resources/
├── templates/index.html               # The portfolio page
└── static/images/                     # Profile photo
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

- Name, title, location and contact details: `PortfolioController.java`
- Everything else (skills, projects, experience, certifications): `src/main/resources/templates/index.html`

## Contact

- GitHub: [github.com/jiiitiro](https://github.com/jiiitiro)
- Email: julitoiiitiro@gmail.com
