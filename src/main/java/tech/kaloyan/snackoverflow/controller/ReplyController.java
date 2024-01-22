/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import tech.kaloyan.snackoverflow.resources.req.ReplyReq;
import tech.kaloyan.snackoverflow.resources.resp.ReplyResp;
import tech.kaloyan.snackoverflow.service.AuthService;
import tech.kaloyan.snackoverflow.service.CommentService;
import tech.kaloyan.snackoverflow.service.ReplyService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/questions/{questionId}/comments/{commentId}/replies")
@RequiredArgsConstructor
public class ReplyController {
    final private AuthService authService;
    final private CommentService commentService;
    final private ReplyService replyService;

    @PostMapping
    public ResponseEntity<ReplyResp> reply(@PathVariable String questionId, @PathVariable String commentId, @Valid @RequestBody ReplyReq replyReq) {
        ReplyResp saved = replyService.save(questionId, commentId, replyReq);
        return ResponseEntity.created(
                UriComponentsBuilder.fromPath("/api/v1/questions/{questionId}/comments/{commentId}/replies/{id}").buildAndExpand(questionId, commentId, saved.getId()).toUri()
        ).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<ReplyResp>> getRepliesByCommentId(@PathVariable String questionId, @PathVariable String commentId) {
        return ResponseEntity.ok(replyService.getAllByCommentId(commentId));
    }

    @GetMapping("/{replyId}/history")
    public ResponseEntity<List<ReplyResp>> getReplyHistory(@PathVariable String questionId, @PathVariable String commentId, @PathVariable String replyId) {
        return ResponseEntity.ok(replyService.getHistoryById(replyId));
    }

    @PutMapping("/{replyId}")
    public ResponseEntity<ReplyResp> updateReply(@PathVariable String questionId, @PathVariable String commentId, @PathVariable String replyId, @Valid @RequestBody ReplyReq replyReq) {
        return ResponseEntity.ok(replyService.update(questionId, commentId, replyId, replyReq));
    }

    @DeleteMapping("/{replyId}")
    public ResponseEntity<Object> deleteReply(@PathVariable String questionId, @PathVariable String commentId, @PathVariable String replyId) {
        replyService.delete(questionId, commentId, replyId);
        return ResponseEntity.noContent().build();
    }
}
