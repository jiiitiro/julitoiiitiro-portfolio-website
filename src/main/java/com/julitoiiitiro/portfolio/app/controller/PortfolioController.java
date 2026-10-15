package com.julitoiiitiro.portfolio.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PortfolioController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("name", "JULITO III T. TIRO");
                model.addAttribute("title", "Full-Stack Java & Software Developer");
                model.addAttribute("location", "Quezon City, Philippines");
                model.addAttribute("email", "julitoiiitiro@gmail.com");
                model.addAttribute("phone", "+63 961 604 9255");
        return "index";
    }
}
