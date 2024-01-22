/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import tech.kaloyan.snackoverflow.resources.req.QuestionReq;
import tech.kaloyan.snackoverflow.resources.req.RatedReq;
import tech.kaloyan.snackoverflow.resources.req.SavedReq;
import tech.kaloyan.snackoverflow.resources.resp.QuestionResp;
import tech.kaloyan.snackoverflow.resources.resp.RatedResp;
import tech.kaloyan.snackoverflow.resources.resp.SavedResp;
import tech.kaloyan.snackoverflow.service.AuthService;
import tech.kaloyan.snackoverflow.service.QuestionService;
import tech.kaloyan.snackoverflow.service.RatedService;
import tech.kaloyan.snackoverflow.service.SavedService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final AuthService authService;
    private final QuestionService questionService;
    private final RatedService ratedService;
    private final SavedService savedService;

    @GetMapping
    public ResponseEntity<List<QuestionResp>> getAllQuestions() {
        return ResponseEntity.ok(questionService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionResp> getQuestionById(@PathVariable String id) {
        return ResponseEntity.ok(questionService.getById(id));
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<QuestionResp>> getQuestionHistoryById(@PathVariable String id) {
        return ResponseEntity.ok(questionService.getHistoryById(id));
    }

    @PostMapping
    public ResponseEntity<QuestionResp> create(@RequestBody QuestionReq questionReq) {
        QuestionResp saved = questionService.save(questionReq, authService.getUser());
        return ResponseEntity.created(
                UriComponentsBuilder.fromPath("/api/v1/questions/{id}")
                        .buildAndExpand(saved.getId())
                        .toUri()
        ).body(saved);
    }

    @PostMapping("/{id}/rate")
    public ResponseEntity<RatedResp> rate(@PathVariable String id, @RequestBody RatedReq ratedReq) {
        return ResponseEntity.ok(ratedService.save(ratedReq, id, authService.getUser()));
    }

    @PostMapping("/{id}/save")
    public ResponseEntity<SavedResp> save(@PathVariable String id, @RequestBody SavedReq savedReq) {
        return ResponseEntity.ok(savedService.save(id, savedReq, authService.getUser()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionResp> update(@PathVariable String id, @RequestBody QuestionReq questionReq) {
        return ResponseEntity.ok(questionService.update(id, questionReq, authService.getUser()));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        questionService.delete(id, authService.getUser());
        return ResponseEntity.noContent().build();
    }
}
