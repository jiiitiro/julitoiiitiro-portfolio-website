# Handoff: portfolio work, for Claude on a new machine

Read this first, then `README.md` (it documents structure, content editing and run steps).
Last updated 2026-11-01.

## Setup on the new machine

```bash
git clone git@github.com:jiiitiro/julitoiiitiro-portfolio-website.git
cd julitoiiitiro-portfolio-website
./mvnw test              # Java 17+
./mvnw spring-boot:run   # http://localhost:8080
# or: docker compose up --build
```

Live site: https://julitoiiitiro-portfolio.up.railway.app/ (Railway deploys pushes to `main`).

## Rules the user set (follow every time)

- **No Claude attribution in commits.** No `Co-Authored-By: Claude` trailer. Short lowercase commit messages, like the existing history.
- **Update README.md before every commit and push**, in the same commit as the change.
- **Never credit the user with AGREEMO's mobile app or web dashboard.** Their role: project manager, backend API (Python, Flask REST API, PostgreSQL), helped build the hardware. Teammates built the Flutter app and the dashboard.
- **Never publish the AGREEMO manuscript** (it has team CVs with phones and addresses). It is kept outside the repo, on the old machine only.
- **Phone number:** only on `/resume` and `/resume.pdf`, never on the website.
- **Screenshots and videos are public and stay in git history.** Mask phone numbers, emails of third parties, balances and Chrome autofill or password popups, and strip metadata before committing. (History was already rewritten once, on 2026-10-21, to remove a screenshot with phone numbers.)
- Content decisions: headline "Full-Stack Java Developer"; frontend skills are only Thymeleaf, HTML & CSS and Tailwind CSS; the Quezon City Government job ended Sept 2026; Power BI is shown as in progress.

## Milestone plan: progress

1. **Quick content fixes**: done.
2. **Skills and certifications cleanup**: done.
3. **`portfolio.yml` content file and resume at `/resume` and `/resume.pdf`**: done.
4. **Project detail pages at `/projects/<slug>`**: structure, carousel, live GitHub README sync and local video support are done. Per project:
   | Project | State | Still open |
   |---|---|---|
   | FoodieFleet | 6 screenshots; content from its live README (`main-dev` branch) | nothing required |
   | Jcasheesh | 5 screenshots, demo video, live README | nothing required |
   | Employee Canvas App (Power Apps) | 5 screenshots, demo video, filled in; no public source | `lessons` empty |
   | AGREEMO | filled in, 8 photos, user manual PDF; source not public | `lessons` empty; whether source can be public; `readme` link skipped until the user finds its docs |
   | Busy Hands | overview, features, challenges, Flask stack, role filled | **no screenshots**, `lessons` empty, no GitHub or demo link, `source-note` empty |
5. **Contact, privacy, SEO**: code done (LinkedIn and Facebook links added, share image, meta tags, robots.txt, sitemap). Left for the user: test the link preview after deploy (LinkedIn Post Inspector, Facebook Sharing Debugger), optional custom domain, a friend or mentor review.
6. **Keep it current**: `scripts/check-site.sh` and the monthly GitHub Actions site check are done, plus the README checklist.

## Still to do

- [ ] Busy Hands: screenshots in `static/images/projects/busy-hands/`, lessons, GitHub or demo link, or a `source-note` if the code is private.
- [ ] `lessons` for Employee Canvas App, AGREEMO and Busy Hands (the user writes these; don't invent them).
- [ ] Certificate proof links: 100 Days of Code linked (`100_DAYS_OF_CODE_PYTHON_CERTIFICATE.pdf`); other certifications pending.
- [ ] Confirm Railway is deploying the latest `main`. On 2026-10-21 it was found serving an old commit, though the Docker image built and ran fine locally. The user needs to check the Railway dashboard.
- [ ] After deploy, test the share preview, and optionally set up a custom domain.

## How to work with the user

- Project content (overview, challenges, numbers, links, screenshots) comes from the user or from their repos. Wait for it rather than making it up.
- Check this file and the README for progress before suggesting the next step.
- When this file goes out of date, update it (or delete it once everything is done).
