/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.kaloyan.snackoverflow.resources.req.*;
import tech.kaloyan.snackoverflow.resources.resp.CommentResp;
import tech.kaloyan.snackoverflow.service.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;
    private final CommentService commentService;
    private final ReplyService replyService;
    private final RatedService ratedService;
    private final SavedService savedService;

    // Posts

    @GetMapping
    public ResponseEntity<?> getAllQuestions() {
        return ResponseEntity.ok(questionService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getQuestionById(@PathVariable String id) {
        try {
            return ResponseEntity.ok(questionService.getById(id));
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Question does not exist")) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createQuestion(@RequestBody QuestionReq questionReq) {
        try {
            return ResponseEntity.ok(questionService.save(questionReq));
        } catch (RuntimeException e) {
            if (e.getMessage().equals("User does not exist")) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Comments

    @GetMapping("/{id}/comments")
    public ResponseEntity<?> getCommentsByQuestionId(@PathVariable String id) {
        try {
            return ResponseEntity.ok(commentService.getAllByQuestionId(id));
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Question does not exist")) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}/comments/{commentId}")
    public ResponseEntity<?> getCommentById(@PathVariable String id, @PathVariable String commentId) {
        try {
            return ResponseEntity.ok(commentService.getById(commentId));
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Question does not exist")) {
                return ResponseEntity.notFound().build();
            } else if (e.getMessage().equals("Comment does not exist")) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Replies

    @GetMapping("/{id}/comments/{commentId}/replies")
    public ResponseEntity<?> getRepliesByCommentId(@PathVariable String id, @PathVariable String commentId) {
        try {
            return ResponseEntity.ok(replyService.getAllByCommentId(commentId));
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Question does not exist")) {
                return ResponseEntity.notFound().build();
            } else if (e.getMessage().equals("Comment does not exist")) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Actions

    // comment

    @PostMapping("/{id}/comment")
    public ResponseEntity<?> comment(@PathVariable String id, @RequestBody CommentReq commentReq) {
        try {
            if (!Objects.equals(id, commentReq.getQuestionId())) {
                return ResponseEntity.badRequest().body("Question id does not match");
            }

            return ResponseEntity.ok(commentService.save(commentReq));
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Question does not exist")) {
                return ResponseEntity.notFound().build();
            } else if (e.getMessage().equals("User does not exist")) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // reply

    @PostMapping("/{id}/comment/{commentId}/reply")
    public ResponseEntity<?> reply(@PathVariable String id, @PathVariable String commentId, @RequestBody ReplyReq replyReq) {
        try {
            if (!Objects.equals(commentId, replyReq.getCommentId())) {
                return ResponseEntity.badRequest().body("Comment id does not match");
            }

            Optional<CommentResp> comment = commentService.getById(commentId);
            if (comment.isEmpty() || !Objects.equals(id, comment.get().getQuestionId())) {
                return ResponseEntity.badRequest().body("Question id does not match");
            }

            return ResponseEntity.ok(replyService.save(replyReq));
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Question does not exist")) {
                return ResponseEntity.notFound().build();
            } else if (e.getMessage().equals("Comment does not exist")) {
                return ResponseEntity.notFound().build();
            } else if (e.getMessage().equals("User does not exist")) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // rate

    @PostMapping("/{id}/rate")
    public ResponseEntity<?> rate(@PathVariable String id, @RequestBody RatedReq ratedReq) {
        try {
            if (!Objects.equals(id, ratedReq.getQuestionId())) {
                return ResponseEntity.badRequest().body("Question id does not match");
            }

            return ResponseEntity.ok(ratedService.save(ratedReq));
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Question does not exist")) {
                return ResponseEntity.notFound().build();
            } else if (e.getMessage().equals("User does not exist")) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // save

    @PostMapping("/{id}/save")
    public ResponseEntity<?> save(@PathVariable String id, @RequestBody SavedReq savedReq) {
        try {
            if (!Objects.equals(id, savedReq.getQuestionId())) {
                return ResponseEntity.badRequest().body("Question id does not match");
            }

            return ResponseEntity.ok(savedService.save(savedReq));
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Question does not exist")) {
                return ResponseEntity.notFound().build();
            } else if (e.getMessage().equals("User does not exist")) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
