
package com.example.demo;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

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
SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
http
	.authenticationProvider(authenticationProvider())
	.authorizeHttpRequests(auth -> auth
			.requestMatchers("/register", "/process_register").permitAll()
			.requestMatchers("/videos").permitAll()
			.requestMatchers("/register_success").authenticated()
			.requestMatchers("/add-comment").authenticated()
			.requestMatchers("/profile").authenticated()

			.requestMatchers("/comments/**").permitAll()
			.anyRequest().permitAll()
			)
	.formLogin(login -> login
			.usernameParameter("email")
			.defaultSuccessUrl("/", true)
			.permitAll()
			)
	.logout(logout -> logout
			.logoutSuccessUrl("/")
			.invalidateHttpSession(true)
			.deleteCookies("JSESSIONID")
			.permitAll()
			)
	.sessionManagement(session -> session
			.maximumSessions(1)
			.maxSessionsPreventsLogin(true)
			);

	return http.build();
	}

@Bean
HttpSessionEventPublisher httpSessionEventPublisher() {
	return new HttpSessionEventPublisher();
	}
}