package com.julitoiiitiro.portfolio.app.controller;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.julitoiiitiro.portfolio.app.content.Portfolio;
import com.julitoiiitiro.portfolio.app.resume.ResumePdfRenderer;

@Controller
public class PortfolioController {

    private final Portfolio portfolio;
    private final ResumePdfRenderer resumePdfRenderer;

    public PortfolioController(Portfolio portfolio, ResumePdfRenderer resumePdfRenderer) {
        this.portfolio = portfolio;
        this.resumePdfRenderer = resumePdfRenderer;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("portfolio", portfolio);
        return "index";
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
