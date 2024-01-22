/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import tech.kaloyan.snackoverflow.resources.req.CommentReq;
import tech.kaloyan.snackoverflow.resources.resp.CommentResp;
import tech.kaloyan.snackoverflow.service.AuthService;
import tech.kaloyan.snackoverflow.service.CommentService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/questions/{questionId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final AuthService authService;
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResp> create(@PathVariable String questionId, @RequestBody CommentReq commentReq) {
        CommentResp saved = commentService.save(questionId, commentReq);
        return ResponseEntity.created(
                UriComponentsBuilder.fromPath("/api/v1/questions/{questionId}/comments/{id}")
                        .buildAndExpand(questionId, saved.getId())
                        .toUri()
        ).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<CommentResp>> getCommentsByQuestionId(@PathVariable String questionId) {
        return ResponseEntity.ok(commentService.getAllByQuestionId(questionId));

    }

    @GetMapping("/{commentId}")
    public ResponseEntity<Optional<CommentResp>> getCommentById(@PathVariable String questionId, @PathVariable String commentId) {
        Optional<CommentResp> commentResp = commentService.getById(commentId);
        return ResponseEntity.ok(commentResp);
    }

    @GetMapping("/{commentId}/history")
    public ResponseEntity<List<CommentResp>> getCommentsHistoryById(@PathVariable String questionId, @PathVariable String commentId) {
        return ResponseEntity.ok(commentService.getHistoryById(commentId));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResp> updateCommentById(@PathVariable String questionId, @PathVariable String commentId, @RequestBody CommentReq commentReq) {
        return ResponseEntity.ok(commentService.update(commentId, questionId, commentReq));
    }


    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteCommentById(@PathVariable String questionId, @PathVariable String commentId) {
        commentService.delete(commentId, questionId);
        return ResponseEntity.noContent().build();
    }
}
