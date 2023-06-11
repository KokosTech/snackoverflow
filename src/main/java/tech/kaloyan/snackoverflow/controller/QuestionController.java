/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import tech.kaloyan.snackoverflow.entity.Question;
import tech.kaloyan.snackoverflow.resources.req.QuestionReq;
import tech.kaloyan.snackoverflow.resources.req.RatedReq;
import tech.kaloyan.snackoverflow.resources.req.SavedReq;
import tech.kaloyan.snackoverflow.service.AuthService;
import tech.kaloyan.snackoverflow.service.QuestionService;
import tech.kaloyan.snackoverflow.service.RatedService;
import tech.kaloyan.snackoverflow.service.SavedService;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final AuthService authService;
    private final QuestionService questionService;
    private final RatedService ratedService;
    private final SavedService savedService;

    @GetMapping
    public ResponseEntity<?> getAllQuestions() {
        return ResponseEntity.ok(questionService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getQuestionById(@PathVariable String id) {
        try {
            return ResponseEntity.ok(questionService.getById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody QuestionReq questionReq) {
        try {
            Question saved = questionService.save(questionReq, authService.getUser());
            return ResponseEntity.created(
                    UriComponentsBuilder.fromPath("/api/v1/questions/{id}")
                            .buildAndExpand(saved.getId())
                            .toUri()
            ).body(saved);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/rate")
    public ResponseEntity<?> rate(@PathVariable String id, @RequestBody RatedReq ratedReq) {
        try {
            if (!Objects.equals(id, ratedReq.getQuestionId())) {
                return ResponseEntity.badRequest().body("Question id does not match");
            }

            return ResponseEntity.ok(ratedService.save(ratedReq, authService.getUser()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/save")
    public ResponseEntity<?> save(@PathVariable String id, @RequestBody SavedReq savedReq) {
        try {
            if (!Objects.equals(id, savedReq.getQuestionId())) {
                return ResponseEntity.badRequest().body("Question id does not match");
            }

            return ResponseEntity.ok(savedService.save(savedReq, authService.getUser()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // update

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody QuestionReq questionReq) {
        try {
            return ResponseEntity.ok(questionService.update(id, questionReq, authService.getUser()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // delete

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        try {
            questionService.delete(id, authService.getUser());
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
