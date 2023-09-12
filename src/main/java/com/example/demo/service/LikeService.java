package com.example.demo.service;

import com.example.demo.model.Like;
import com.example.demo.model.Post;
import com.example.demo.model.User;
import com.example.demo.repository.LikeRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LikeService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    @Autowired
    public LikeService(PostRepository postRepository, UserRepository userRepository, LikeRepository likeRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
    }


    //🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢( POST )🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢
    public void likePost(UUID postId, UUID userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found!"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));

        // Check if the like already exists
        Like existingLike = likeRepository.findByPostAndUser(post, user);
        if(existingLike != null) {
            throw new IllegalArgumentException("User has already liked this post!");
        }

        Like newLike = new Like();
        newLike.setPost(post);
        newLike.setUser(user);
        likeRepository.save(newLike);

        // Increment the likes count for the post
        post.setLikesCount(post.getLikesCount() + 1);
        postRepository.save(post);
    }


    //🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵( GET )🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵
    public boolean isPostLikedByUser(UUID postId, UUID userId) {
        // This assumes you have a method in your repository to find a like by postId and userId
        return likeRepository.findByPostIdAndUserId(postId, userId).isPresent();
    }
    //🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴( DELETE )🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴

    public void unlikePost(UUID postId, UUID userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found!"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));

        Like existingLike = likeRepository.findByPostAndUser(post, user);
        if(existingLike == null) {
            throw new IllegalArgumentException("User hasn't liked this post!");
        }

        likeRepository.delete(existingLike);

        // Decrement the likes count for the post
        post.setLikesCount(post.getLikesCount() - 1);
        postRepository.save(post);
    }
}
