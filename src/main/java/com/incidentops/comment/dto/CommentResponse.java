package com.incidentops.comment.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResponse {
    private Long id;

    private String comment;

    private String email;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
