package com.example.demo;

import org.springframework.context.annotation.Bean;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
 
@Configuration
public class WebSecurityConfig   {
	@Bean
	UserDetailsService userDetailsService() {
		return new CustomUserDetailsService();
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


@Bean
  DaoAuthenticationProvider authenticationProvider() {
	DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	authProvider.setUserDetailsService(userDetailsService());
	authProvider.setPasswordEncoder(passwordEncoder());
	
	return authProvider;
}

@Bean
SecurityFilterChain configure(HttpSecurity http) throws Exception{
	http.authenticationProvider(authenticationProvider());
	
	 http.authorizeHttpRequests(auth ->
     auth.requestMatchers("/videos").permitAll()
         .requestMatchers("/users").authenticated()
         .requestMatchers("/add-comment").authenticated()
         .requestMatchers("/comments/**").permitAll()
         .anyRequest().permitAll()
	
	)
	
	.formLogin(login ->
	login.usernameParameter("email")
	.defaultSuccessUrl("/", true)
	.permitAll()
	)
	.logout(logout -> logout.logoutSuccessUrl("/").permitAll());
	
	
	return http.build();
}
public void addCorsMappings(CorsRegistry registry) {
	registry.addMapping("/**").allowedOrigins("http://localhost:8080");
}

}
