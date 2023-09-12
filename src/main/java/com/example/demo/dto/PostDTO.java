package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class PostDTO {
    private UUID id;
    private String content;
    private String image;
    private int likesCount;
    private int commentsCount;
    private LocalDateTime createdAt;
    private String username;  // From the User entity
    private String userProfilePicture;  // From the User entity
    private UUID userId;
}
