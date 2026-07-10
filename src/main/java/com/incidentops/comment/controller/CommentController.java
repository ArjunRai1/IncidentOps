package com.incidentops.comment.controller;

import com.incidentops.comment.dto.CommentResponse;
import com.incidentops.comment.dto.CreateCommentRequest;
import com.incidentops.comment.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/incidents/{incidentId}/comments")
public class CommentController{
    private final CommentService commentService;
    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentResponse> addComment(@PathVariable Long incidentId, @Valid @RequestBody CreateCommentRequest request){
        CommentResponse response = commentService.addComment(incidentId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long incidentId, @RequestParam(defaultValue = "desc") String direction){
        List<CommentResponse> response = commentService.getComments(incidentId, direction);
        return ResponseEntity.ok(response);
    }
}

