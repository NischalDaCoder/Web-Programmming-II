package com.anime.WatchAnime.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.anime.WatchAnime.model.User;
import com.anime.WatchAnime.service.UserService;

@Controller
public class MyController {

    @Autowired
    private UserService userService;

    // Home page with featured anime
    @GetMapping("/")
    public String home(Model model) {
        List<String> featuredAnime = Arrays.asList(
                "OnePiece",
                "Naruto",
                "Bleach",
                "Deathnote",
                "Rezero"
        );
        model.addAttribute("featuredAnime", featuredAnime);
        return "home";
    }

    // Show the registration form
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        System.out.println("KNVKDNVKDNVKD");
        model.addAttribute("user", new User());
        return "register";
    }

    // Process the registration form
    @PostMapping("/register")
    public String processRegistration(
        @RequestParam("username") String username,
        @RequestParam("password") String password
    ) {
        
        User user = new User(username, password);
        userService.saveUser(user);
        return "redirect:/login";
    }

    // Show the login form
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
}

