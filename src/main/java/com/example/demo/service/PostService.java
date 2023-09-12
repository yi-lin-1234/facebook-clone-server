package com.example.demo.service;

import com.example.demo.dto.PostDTO;
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
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository,LikeRepository likeRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
    }

    //🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢( POST )🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢
    public void createPost(UUID userId, Post post) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No user found with ID: " + userId));
        post.setUser(user);
        postRepository.save(post);
    }


    //🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵( GET )🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵🔵

    private PostDTO convertToDto(Post post) {
        PostDTO dto = new PostDTO();
        dto.setId(post.getId());
        dto.setContent(post.getContent());
        dto.setImage(post.getImage());
        dto.setLikesCount(post.getLikesCount());
        dto.setCommentsCount(post.getCommentsCount());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUsername(post.getUser().getUsername());
        dto.setUserProfilePicture(post.getUser().getPicture());
        dto.setUserId(post.getUser().getId());
        return dto;
    }
    public List<PostDTO> getAllPosts() {
        List<Post> posts = postRepository.findAll();

        return posts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<PostDTO> getPostsByUserId(UUID userId) {
        List<Post> posts = postRepository.findByUserId(userId);

        return posts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }



    public PostDTO getPostById(UUID postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("No post found with ID: " + postId));
        return convertToDto(post);
    }

    public List<PostDTO> getLikedPostsByUserId(UUID userId) {
        List<Like> likedByUser = likeRepository.findByUserId(userId);

        // Convert List<Like> to List<Post>
        List<Post> likedPosts = likedByUser.stream()
                .map(Like::getPost)
                .collect(Collectors.toList());

        // Convert List<Post> to List<PostDTO>
        return likedPosts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    //🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡( PUT )🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡🟡

    public void updatePost(UUID postId, String content, String image) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("No post found with ID: " + postId));

        post.setContent(content);
        post.setImage(image);
        postRepository.save(post);
    }


    //🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴( DELETE )🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴🔴
    public void deletePost(UUID postId) {
        if (!postRepository.existsById(postId)) {
            throw new IllegalArgumentException("No post found with ID: " + postId);
        }
        postRepository.deleteById(postId);
    }

}
