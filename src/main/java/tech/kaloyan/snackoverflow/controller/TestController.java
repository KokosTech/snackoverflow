/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.kaloyan.snackoverflow.entity.User;
import tech.kaloyan.snackoverflow.resources.req.QuestionReq;
import tech.kaloyan.snackoverflow.resources.req.UserSignupReq;
import tech.kaloyan.snackoverflow.service.QuestionService;
import tech.kaloyan.snackoverflow.service.UserService;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final QuestionService questionService;
    private final UserService userService;

    @PostMapping("/add-question")
    public ResponseEntity<?> addQuestion(
            @RequestBody QuestionReq questionReq
            ){
        return ResponseEntity.ok(questionService.save(questionReq));
    }

    @GetMapping("/get-question/{id}")
    public ResponseEntity<?> getQuestion(
            @PathVariable String id
            ){
        return ResponseEntity.ok(questionService.getById(id));
    }

    @PostMapping("/test-user")
    public ResponseEntity<?> testUser(
            @RequestBody UserSignupReq userSignupReq
            ){
        return ResponseEntity.ok(userService.save(userSignupReq));
    }
}
