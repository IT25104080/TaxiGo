package com.taxi.taxibookingplatform.controller;

import com.taxi.taxibookingplatform.model.ContactMessage;
import com.taxi.taxibookingplatform.service.ContactFileHandler;
import com.taxi.taxibookingplatform.service.TaxiFileHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/taxis")
    public String taxis(Model model) throws IOException {
        model.addAttribute("taxis", TaxiFileHandler.getAllTaxis());
        return "taxis";
    }

    @GetMapping("/taxi")
    public String taxiAlias(Model model) throws IOException {
        return taxis(model);
    }

    @GetMapping("/services")
    public String services() {
        return "services";
    }


    @GetMapping("/faq")
    public String faq() {
        return "faq";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @PostMapping("/contact")
    public String submitContact(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String subject,
            @RequestParam String message,
            Model model) throws IOException {

        if (!com.taxi.taxibookingplatform.util.ValidationUtils.isValidName(name)) {
            model.addAttribute("error", "Name must contain only alphabetic characters and spaces (2 to 50 characters).");
            return "contact";
        }
        if (!com.taxi.taxibookingplatform.util.ValidationUtils.isValidEmail(email)) {
            model.addAttribute("error", "Please provide a valid email address.");
            return "contact";
        }
        if (subject == null || subject.isBlank() || message == null || message.isBlank()) {
            model.addAttribute("error", "Subject and message are required fields.");
            return "contact";
        }

        ContactMessage msg = new ContactMessage(
                "MSG" + UUID.randomUUID().toString().replace("-", "").substring(0, 8),
                name, email, subject, message, LocalDateTime.now()
        );
        ContactFileHandler.save(msg);
        model.addAttribute("success", true);
        return "contact";
    }

    @GetMapping("/privacy")
    public String privacy() {
        return "privacy";
    }

    @GetMapping("/terms")
    public String terms() {
        return "terms";
    }
}
