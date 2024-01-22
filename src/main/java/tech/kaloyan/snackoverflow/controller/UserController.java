/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.kaloyan.snackoverflow.resources.req.UserSignupReq;
import tech.kaloyan.snackoverflow.resources.resp.*;
import tech.kaloyan.snackoverflow.service.AuthService;
import tech.kaloyan.snackoverflow.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final AuthService authService;
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<Optional<UserAccountResp>> getUser(@PathVariable String id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResp> updateUser(@PathVariable String id, @Valid @RequestBody UserSignupReq userSignupReq) {
        return ResponseEntity.ok(userService.update(id, userSignupReq, authService.getUser()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.delete(id, authService.getUser());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/questions")
    public ResponseEntity<List<QuestionResp>> getUserQuestions(@PathVariable String id) {
        return ResponseEntity.ok().body(userService.getUserQuestions(id));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentResp>> getUserComments(@PathVariable String id) {
        return ResponseEntity.ok().body(userService.getUserComments(id));
    }

    @GetMapping("/{id}/replies")
    public ResponseEntity<List<ReplyResp>> getUserReplies(@PathVariable String id) {
        return ResponseEntity.ok().body(userService.getUserReplies(id));
    }
}
