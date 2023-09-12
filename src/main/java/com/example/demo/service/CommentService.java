package com.example.demo.service;

import com.example.demo.dto.CommentDTO;
import com.example.demo.model.Comment;
import com.example.demo.model.Post;
import com.example.demo.model.User;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, UserRepository userRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    //🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢( POST )🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢

    public void createComment(String content, UUID userId, UUID postId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found."));
        Post post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("Post not found."));

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUser(user);
        comment.setPost(post);

        commentRepository.save(comment);

        // Increment the comment count for the post and save it
        post.setCommentsCount(post.getCommentsCount() + 1);
        postRepository.save(post);
    }

    //🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵( GET )🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵

    private CommentDTO toDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUserId(comment.getUser().getId());
        dto.setUsername(comment.getUser().getUsername());
        dto.setPostId(comment.getPost().getId());
        dto.setPicture(comment.getUser().getPicture()); // Assuming the User entity has a picture attribute.
        return dto;
    }

    public List<CommentDTO> getCommentsByPost(UUID postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream().map(this::toDTO).collect(Collectors.toList());
    }

    //🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡( PUT )🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡

    public void updateComment(UUID commentId, String newContent) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found."));

        comment.setContent(newContent);
        commentRepository.save(comment);
    }

    //🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴( DELETE )🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴

    public void deleteCommentById(UUID commentId) {
        // Find the comment by its ID.
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found!"));

        // Get the associated post.
        Post post = comment.getPost();

        // Delete the comment.
        commentRepository.delete(comment);

        // Decrement the comments count for the associated post.
        post.setCommentsCount(post.getCommentsCount() - 1);
        postRepository.save(post);
    }
}
