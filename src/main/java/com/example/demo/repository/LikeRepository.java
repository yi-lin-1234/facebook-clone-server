package com.example.demo.repository;

import com.example.demo.model.Like;
import com.example.demo.model.Post;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LikeRepository extends JpaRepository<Like, UUID> {
    Like findByPostAndUser(Post post, User user);
    Optional<Like> findByPostIdAndUserId(UUID postId, UUID userId);

    List<Like> findByUserId(UUID userId);
}
