package com.example.demo.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class CommentDTO {
    private UUID id;
    private UUID userId;
    private UUID postId;
    private String content;
    private LocalDateTime createdAt;
    private String username;
    private String picture;
}
