/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import tech.kaloyan.snackoverflow.entity.Reply;
import tech.kaloyan.snackoverflow.resources.req.ReplyReq;
import tech.kaloyan.snackoverflow.resources.resp.CommentResp;
import tech.kaloyan.snackoverflow.resources.resp.ReplyResp;
import tech.kaloyan.snackoverflow.service.AuthService;
import tech.kaloyan.snackoverflow.service.CommentService;
import tech.kaloyan.snackoverflow.service.ReplyService;

import javax.validation.Valid;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/questions/{questionId}/comments/{commentId}/replies")
@RequiredArgsConstructor
public class ReplyController {
    final private AuthService authService;
    final private CommentService commentService;
    final private ReplyService replyService;

    // create

    @PostMapping
    public ResponseEntity<?> reply(@PathVariable String questionId, @PathVariable String commentId, @Valid @RequestBody ReplyReq replyReq) {
        try {
            if (!Objects.equals(commentId, replyReq.getCommentId())) {
                return ResponseEntity.badRequest().body("Comment id does not match");
            }

            Optional<CommentResp> comment = commentService.getById(commentId);
            if (comment.isEmpty() || !Objects.equals(questionId, comment.get().getQuestionId())) {
                return ResponseEntity.badRequest().body("Question id does not match");
            }

            ReplyResp saved = replyService.save(replyReq, authService.getUser());
            return ResponseEntity.created(
                    UriComponentsBuilder.fromPath("/api/v1/questions/{questionId}/comments/{commentId}/replies/{id}").buildAndExpand(questionId, commentId, saved.getId()).toUri()
            ).body(saved);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // read

    @GetMapping
    public ResponseEntity<?> getRepliesByCommentId(@PathVariable String questionId, @PathVariable String commentId) {
        try {
            return ResponseEntity.ok(replyService.getAllByCommentId(commentId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{replyId}/history")
    public ResponseEntity<?> getReplyHistory(@PathVariable String questionId, @PathVariable String commentId, @PathVariable String replyId) {
        try {
            return ResponseEntity.ok(replyService.getHistoryById(replyId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // update

    @PostMapping("/{replyId}")
    public ResponseEntity<?> updateReply(@PathVariable String questionId, @PathVariable String commentId, @PathVariable String replyId, @Valid @RequestBody ReplyReq replyReq) {
        try {
            if (!Objects.equals(commentId, replyReq.getCommentId())) {
                return ResponseEntity.badRequest().body("Comment id does not match");
            }

            Optional<CommentResp> comment = commentService.getById(commentId);
            if (comment.isEmpty() || !Objects.equals(questionId, comment.get().getQuestionId())) {
                return ResponseEntity.badRequest().body("Question id does not match");
            }

            ReplyResp updated = replyService.update(replyId, replyReq, authService.getUser());
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // delete

    @DeleteMapping("/{replyId}")
    public ResponseEntity<?> deleteReply(@PathVariable String questionId, @PathVariable String commentId, @PathVariable String replyId) {
        try {
            Optional<ReplyResp> reply = replyService.getById(replyId);
            if(reply.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            if (!Objects.equals(commentId, reply.get().getCommentId())) {
                return ResponseEntity.badRequest().body("Comment id does not match");
            }

            Optional<CommentResp> comment = commentService.getById(commentId);
            if (comment.isEmpty() || !Objects.equals(questionId, comment.get().getQuestionId())) {
                return ResponseEntity.badRequest().body("Question id does not match");
            }

            replyService.delete(replyId, authService.getUser());
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
