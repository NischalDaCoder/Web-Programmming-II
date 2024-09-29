package com.example.demo;

import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name="comments")

public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	@Column(nullable=false)
	private String content;
	
	@Column(nullable=false)
	private String animeName;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@Column(nullable = false)
	private LocalDateTime createdAt;
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id=id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content=content;
	}
	public String getAnimeName() {
		return animeName;
	}
	public void setAnimeName(String animeName) {
		this.animeName=animeName;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user=user;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt=createdAt;
	}

	
	}


