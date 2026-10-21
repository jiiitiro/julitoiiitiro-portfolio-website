package com.julitoiiitiro.portfolio.app.controller;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import com.julitoiiitiro.portfolio.app.content.GitHubReadme;
import com.julitoiiitiro.portfolio.app.content.Portfolio;
import com.julitoiiitiro.portfolio.app.resume.ResumePdfRenderer;

@Controller
public class PortfolioController {

    private final Portfolio portfolio;
    private final ResumePdfRenderer resumePdfRenderer;
    private final GitHubReadme gitHubReadme;

    public PortfolioController(Portfolio portfolio, ResumePdfRenderer resumePdfRenderer, GitHubReadme gitHubReadme) {
        this.portfolio = portfolio;
        this.resumePdfRenderer = resumePdfRenderer;
        this.gitHubReadme = gitHubReadme;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("portfolio", portfolio);
        return "index";
    }

    @GetMapping("/projects/{slug}")
    public String project(@PathVariable String slug, Model model) {
        Portfolio.Project project = portfolio.findProject(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No project named " + slug));
        model.addAttribute("portfolio", portfolio);
        model.addAttribute("project", project);
        // Live README from GitHub; null (fall back to portfolio.yml text) if not set or unreachable.
        model.addAttribute("readmeHtml", project.readme() == null ? null
                : gitHubReadme.html(project.readme()).orElse(null));
        return "project-detail";
    }

    @GetMapping("/resume")
    public String resume(Model model) {
        model.addAttribute("portfolio", portfolio);
        return "resume";
    }

    @GetMapping("/resume.pdf")
    public ResponseEntity<byte[]> resumePdf() {
        String fileName = portfolio.profile().name().replaceAll("[^A-Za-z0-9]+", "-") + "-Resume.pdf";
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.inline().filename(fileName).build().toString())
                .body(resumePdfRenderer.render(portfolio));
    }
}
