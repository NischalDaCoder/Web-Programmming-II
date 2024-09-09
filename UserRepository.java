package com.anime.WatchAnime.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anime.WatchAnime.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    
}
