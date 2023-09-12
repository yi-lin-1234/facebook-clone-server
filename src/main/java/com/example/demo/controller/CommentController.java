package com.example.demo.controller;

import com.example.demo.dto.CommentDTO;
import com.example.demo.dto.UpdateCommentBody;
import com.example.demo.service.CommentService;
import jakarta.persistence.EntityNotFoundException;
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
public class CommentController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
    private final CommentService commentService;
    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }


    //🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢( POST )🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢
    @PostMapping("/create-comment")
    public ResponseEntity<Void> createComment(
            @RequestParam String content,
            @RequestParam UUID userId,
            @RequestParam UUID postId) {
        logger.info("Received request to create comment for user ID: {} and post ID: {}", userId, postId);
        try {
            commentService.createComment(content, userId, postId);
            logger.info("Comment successfully created for user ID: {} and post ID: {}", userId, postId);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            logger.error("Entity not found error while creating comment: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error while creating comment: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵( GET )🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵
    @GetMapping("/comments/{postId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByPost(@PathVariable UUID postId) {
        logger.info("Received request to fetch comments for post ID: {}", postId);
        try {
            List<CommentDTO> commentDTOs = commentService.getCommentsByPost(postId);
            logger.info("Fetched {} comments for post ID: {}", commentDTOs.size(), postId);
            return new ResponseEntity<>(commentDTOs, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            logger.error("Entity not found error while fetching comments: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error while fetching comments for post ID {}: {}", postId, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡( PUT )🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡
    @PutMapping("/comment/{commentId}")
    public ResponseEntity<Void> updateComment(
            @PathVariable UUID commentId,
            @RequestBody UpdateCommentBody body) {
        logger.info("Received request to update comment ID: {}", commentId);
        try {
            commentService.updateComment(commentId, body.getContent());
            logger.info("Comment with ID: {} successfully updated.", commentId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            logger.error("Error updating comment, comment not found: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error updating comment: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴( DELETE )🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<Void> deleteCommentById(@PathVariable UUID commentId) {
        logger.info("Received request to delete comment with ID: {}", commentId);
        try {
            commentService.deleteCommentById(commentId);
            logger.info("Successfully deleted comment with ID: {}", commentId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 status code for successful deletion with no content to send back
        } catch (EntityNotFoundException e) {
            logger.error("Entity not found error while deleting comment: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error while deleting comment with ID {}: {}", commentId, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
