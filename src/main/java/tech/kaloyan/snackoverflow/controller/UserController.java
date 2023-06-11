/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;
import tech.kaloyan.snackoverflow.entity.User;
import tech.kaloyan.snackoverflow.resources.req.UserSignupReq;
import tech.kaloyan.snackoverflow.service.AuthService;
import tech.kaloyan.snackoverflow.service.UserService;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final AuthService authService;
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable String id) {
        try {
            User currentUser = authService.getUser();

            if (!currentUser.getId().equals(id)) {
                return ResponseEntity.ok(userService.getById(id));
            }

            return ResponseEntity.ok(userService.getUserById(id));
        } catch (RuntimeException e) {
            if (e.getMessage().equals("User does not exist")) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @Valid @RequestBody UserSignupReq userSignupReq) {
        try {
            return ResponseEntity.ok(userService.update(id, userSignupReq, authService.getUser()));
        } catch (RuntimeException e) {
            if (e.getMessage().equals("User does not exist")) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        try {
            userService.delete(id, authService.getUser());
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get Questions

    @GetMapping("/{id}/questions")
    public ResponseEntity<?> getUserQuestions(@PathVariable String id) {
        try {
            return ResponseEntity.ok(userService.getUserById(id).isPresent() ? userService.getUserById(id).get().getQuestions() : null);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get Comments

    @GetMapping("/{id}/comments")
    public ResponseEntity<?> getUserComments(@PathVariable String id) {
        try {
            return ResponseEntity.ok(userService.getUserById(id).isPresent() ? userService.getUserById(id).get().getComments() : null);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get Replies

    @GetMapping("/{id}/replies")
    public ResponseEntity<?> getUserReplies(@PathVariable String id) {
        try {
            return ResponseEntity.ok(userService.getUserById(id).isPresent() ? userService.getUserById(id).get().getReply() : null);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
