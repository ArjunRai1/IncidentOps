package com.incidentops.comment.service;

import com.incidentops.ai.indexing.IndexingService;
import com.incidentops.audit.entity.Action;
import com.incidentops.audit.service.AuditService;
import com.incidentops.auth.entity.User;
import com.incidentops.auth.repository.UserRepository;
import com.incidentops.comment.dto.CommentResponse;
import com.incidentops.comment.dto.CreateCommentRequest;
import com.incidentops.comment.entity.Comment;
import com.incidentops.comment.exception.IncidentDoesNotExistException;
import com.incidentops.comment.repository.CommentRepository;
import com.incidentops.incident.entity.Incident;
import com.incidentops.incident.exception.UserNotFoundException;
import com.incidentops.incident.repository.IncidentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CommentService {
    private final IncidentRepository incidentRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final AuditService auditService;
    private final IndexingService indexingService;

    public CommentService(IncidentRepository incidentRepository, UserRepository userRepository, CommentRepository commentRepository, AuditService auditService, IndexingService indexingService) {
        this.incidentRepository = incidentRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.auditService = auditService;
        this.indexingService = indexingService;
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException());
    }

    private CommentResponse mapToResponse(Comment comment){
        CommentResponse response = new CommentResponse();
        response.setId(comment.getId());
        response.setComment(comment.getComment());
        response.setEmail(comment.getUser().getEmail());
        response.setCreatedAt(comment.getCreatedAt());
        response.setUpdatedAt(comment.getUpdatedAt());
        return response;
    }

    public CommentResponse addComment(Long incidentId, CreateCommentRequest request){
        Incident incident = incidentRepository.findById(incidentId).orElseThrow(()->new IncidentDoesNotExistException());
        Comment comment = new Comment();
        User user = getCurrentUser();
        comment.setComment(request.getComment());
        comment.setIncident(incident);
        comment.setUser(user);
        auditService.log(incident, user, Action.COMMENT_ADDED, "New comment added");
        Comment savedComment = commentRepository.save(comment);
        indexingService.indexIncident(savedComment.getIncident());
        log.info("New Comment added");
        return mapToResponse(savedComment);
    }

    public List<CommentResponse> getComments(Long incidentId, String direction) {
        Incident incident = incidentRepository.findById(incidentId).orElseThrow(()->new IncidentDoesNotExistException());
        if (!direction.equalsIgnoreCase("asc") && !direction.equalsIgnoreCase("desc")) {
            throw new IllegalArgumentException("Invalid sort direction");
        }
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by("createdAt").ascending() : Sort.by("createdAt").descending();
        List<Comment> comments = commentRepository.findByIncidentId(incidentId, sort);
        return comments.stream().map(this::mapToResponse).toList();
    }

}
