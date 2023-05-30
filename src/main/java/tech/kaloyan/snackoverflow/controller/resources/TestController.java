/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.controller.resources;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.kaloyan.snackoverflow.controller.resources.Req.QuestionReq;
import tech.kaloyan.snackoverflow.service.QuestionService;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final QuestionService questionService;

    @PostMapping("/add-question")
    public ResponseEntity<?> addQuestion(
            @RequestBody QuestionReq questionReq
            ){
        return ResponseEntity.ok(questionService.save(questionReq));
    }
}
