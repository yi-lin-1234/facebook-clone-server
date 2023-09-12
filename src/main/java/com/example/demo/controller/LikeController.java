package com.example.demo.controller;

import com.example.demo.model.Post;
import com.example.demo.service.LikeService;
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
public class LikeController {

    private static final Logger logger = LoggerFactory.getLogger(LikeController.class);
    private final LikeService likeService;
    @Autowired
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    //🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢( POST )🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢

    @PostMapping("/like-post/{postId}")
    public ResponseEntity<Void> likePost(@PathVariable UUID postId, @RequestParam UUID userId) {
        try {
            likeService.likePost(postId, userId);
            logger.info("User with ID: {} liked post with ID: {}", userId, postId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);  // 204 No Content response code
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);  // Handle predictable errors, like duplicates or missing entities
        } catch (Exception e) {
            logger.error("Error occurred while liking post with ID: {}", postId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Handle other unexpected errors
        }
    }

    //🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵( GET )🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵
    @GetMapping("/isLiked")
    public ResponseEntity<Boolean> isPostLikedByUser(@RequestParam UUID postId, @RequestParam UUID userId) {
        boolean isLiked = likeService.isPostLikedByUser(postId, userId);
        return new ResponseEntity<>(isLiked, HttpStatus.OK);
    }

    //🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴( DELETE )🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴
    @DeleteMapping("/unlike-post/{postId}")
    public ResponseEntity<Void> unlikePost(@PathVariable UUID postId, @RequestParam UUID userId) {
        try {
            likeService.unlikePost(postId, userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); //204 No Content for successful unliking
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }






}
