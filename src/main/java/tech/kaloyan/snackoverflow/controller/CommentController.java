/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.kaloyan.snackoverflow.resources.req.CommentReq;
import tech.kaloyan.snackoverflow.resources.resp.CommentResp;
import tech.kaloyan.snackoverflow.service.AuthService;
import tech.kaloyan.snackoverflow.service.CommentService;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/questions/{questionId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final AuthService authService;
    private final CommentService commentService;

    // create

    @PostMapping
    public ResponseEntity<?> create(@PathVariable String questionId, @RequestBody CommentReq commentReq) {
        try {
            if (!Objects.equals(questionId, commentReq.getQuestionId())) {
                return ResponseEntity.badRequest().body("Question id does not match");
            }

            return ResponseEntity.ok(commentService.save(commentReq, authService.getUser()));
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Question does not exist")) {
                return ResponseEntity.notFound().build();
            } else if (e.getMessage().equals("User does not exist")) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // read

    @GetMapping
    public ResponseEntity<?> getCommentsByQuestionId(@PathVariable String questionId) {
        try {
            return ResponseEntity.ok(commentService.getAllByQuestionId(questionId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<?> getCommentById(@PathVariable String questionId, @PathVariable String commentId) {
        try {
            Optional<CommentResp> commentResp = commentService.getById(commentId);

            if (commentResp.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            if (commentResp.get().getQuestionId().equals(questionId)) {
                return ResponseEntity.ok(commentResp.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{commentId}/history")
    public ResponseEntity<?> getCommentsHistoryById(@PathVariable String questionId, @PathVariable String commentId) {
        try {
            return ResponseEntity.ok(commentService.getHistoryById(commentId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // update

    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateCommentById(@PathVariable String questionId, @PathVariable String commentId, @RequestBody CommentReq commentReq) {
        try {
            Optional<CommentResp> commentResp = commentService.getById(commentId);

            if (commentResp.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            if (commentResp.get().getQuestionId().equals(questionId)) {
                return ResponseEntity.ok(commentService.update(commentId, commentReq, authService.getUser()));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // delete

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteCommentById(@PathVariable String questionId, @PathVariable String commentId) {
        try {
            Optional<CommentResp> commentResp = commentService.getById(commentId);

            if (commentResp.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            if (commentResp.get().getQuestionId().equals(questionId)) {
                commentService.delete(commentId, authService.getUser());
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
