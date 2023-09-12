package com.example.demo.controller;

import com.example.demo.dto.PostDTO;
import com.example.demo.dto.UpdatePostBody;
import com.example.demo.model.Post;
import com.example.demo.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
public class PostController {

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    //🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢( POST )🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢
    @PostMapping("create-post/{userId}")
    public ResponseEntity<Void> createPost(@PathVariable UUID userId, @RequestBody Post post) {
        postService.createPost(userId,post);
        logger.info("Successfully created post.");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵( GET )🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵
    @GetMapping("/all-posts")
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        List<PostDTO> postDTOs = postService.getAllPosts();

        if (postDTOs.isEmpty()) {
            logger.warn("No posts found in the database.");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        logger.info("Fetched {} posts from the database.", postDTOs.size());
        return new ResponseEntity<>(postDTOs, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/posts")
    public ResponseEntity<List<PostDTO>> getPostsByUserId(@PathVariable UUID userId) {
        List<PostDTO> postDTOs = postService.getPostsByUserId(userId);

        if (postDTOs.isEmpty()) {
            logger.warn("No posts found for user ID: {}", userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        logger.info("Fetched {} posts for user ID: {}", postDTOs.size(), userId);
        return new ResponseEntity<>(postDTOs, HttpStatus.OK);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable UUID postId) {
        try {
            PostDTO postDTO = postService.getPostById(postId);
            logger.info("Fetched post with ID: {}", postId);
            return new ResponseEntity<>(postDTO, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/liked-posts/{userId}")
    public ResponseEntity<List<PostDTO>> getLikedPostsByUser(@PathVariable UUID userId) {
        List<PostDTO> likedPosts = postService.getLikedPostsByUserId(userId);
        return ResponseEntity.ok(likedPosts);
    }

    //🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡( PUT )🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡

    @PutMapping("/update-post/{postId}")
    public ResponseEntity<Void> updatePostById(@PathVariable UUID postId, @RequestBody UpdatePostBody body) {
        try {
            postService.updatePost(postId, body.getContent(), body.getImage());
            logger.info("Updated post with ID: {}", postId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴( DELETE )🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴
    @DeleteMapping("/delete-post/{postId}")
    public ResponseEntity<Void> deletePostById(@PathVariable UUID postId) {
        try {
            postService.deletePost(postId);
            logger.info("Successfully deleted post with ID: {}", postId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); //204 No Content is appropriate for delete success
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
