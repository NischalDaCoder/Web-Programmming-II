package com.example.demo;

import java.security.Principal;



import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;


@Controller


public class AppController{
	
	@Autowired
	private UserRepository userRepo;
	
	@GetMapping("/")
    public String home(Model model, HttpSession session) {
        List<String> featuredAnime = Arrays.asList(
                "OnePiece",
                "Naruto",
                "Bleach",
                "Deathnote",
                "Rezero"
        );
        model.addAttribute("featuredAnime", featuredAnime);
        
        //To retrieve last visited page from session
        
        String lastVisited = (String) session.getAttribute("lastVisited");
        if (lastVisited != null) {
        	model.addAttribute("lastVisited", lastVisited);
        }
        
        //Set current page as last visited
        session.setAttribute("lastVisited", "/");
        	
        return "index";
    }
	
	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		model.addAttribute("user", new User());
		
		return "signup_form";
	}
	
	@PostMapping("/process_register")
	public String processRegister( User user) {
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		
		userRepo.save(user);
		
	
		
		return "register_success";
	}
	
	
	
	@Autowired
	private CommentRepository commentRepo;
	
	@PostMapping("/add-comment")
	public String addComment(@RequestParam String animeName, @RequestParam String content, Principal principal, HttpSession session) {
		User user = userRepo.findByEmail(principal.getName());
		Comment comment = new Comment();
		comment.setContent(content);
		comment.setAnimeName(animeName);
		comment.setUser(user);
		comment.setCreatedAt(LocalDateTime.now());
		commentRepo.save(comment);
		
		//store last commented anime in session
		session.setAttribute("lastCommentedAnime", animeName);
		return "redirect:/comments/" + animeName;
	}
	
	@GetMapping("/comments/{animeName}")
	public String showComments(@PathVariable String animeName, Model model, HttpSession session) {
		List<Comment> comments = commentRepo.findByAnimeNameOrderByCreatedAtDesc(animeName);
		model.addAttribute("comments", comments);
		model.addAttribute("animeName", animeName);
		
		// Retrieve and add last commented anime to model if it exists
        String lastCommentedAnime = (String) session.getAttribute("lastCommentedAnime");
        if (lastCommentedAnime != null && !lastCommentedAnime.equals(animeName)) {
            model.addAttribute("lastCommentedAnime", lastCommentedAnime);
        }

		
		return "comments";
	}
	
	@GetMapping("/profile")
	public String showProfile(Model model, HttpSession session, Principal principal) {
		
		User user = userRepo.findByEmail(principal.getName());
		model.addAttribute("user", user);
		
		//retrieve session data
		LocalDateTime lastAccessTime = (LocalDateTime) session.getAttribute("lastAccessTime");
		String lastCommentedAnime = (String) session.getAttribute("lastCommentedAnime");
		
		if (lastAccessTime != null) {
			model.addAttribute("lastAccessTime", lastAccessTime);
		}
		if (lastCommentedAnime != null) {
			model.addAttribute("lastCommentedAnime", lastCommentedAnime);
		}
		return "profile";
	}
	
	


}
