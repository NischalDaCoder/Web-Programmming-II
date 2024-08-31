package com.anime.WatchAnime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class MyController {

    @GetMapping("/")
    public String home(Model model){
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
    
}
