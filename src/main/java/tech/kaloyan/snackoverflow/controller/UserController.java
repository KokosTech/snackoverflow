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
import tech.kaloyan.snackoverflow.service.UserService;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable String id) {
        try {
            // TODO: add user profile with jwt to look at private info
            return ResponseEntity.ok(userService.getById(id));
        } catch (RuntimeException e) {
            if (e.getMessage().equals("User does not exist")) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // TODO: implement with JWT
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody UserSignupReq userSignupReq) {
        try {
            Optional<User> user = userService.getUserById(id);

            if (user.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            if (!userSignupReq.getUsername().isEmpty()) {
                user.get().setUsername(userSignupReq.getUsername());
            } else if (!userSignupReq.getEmail().isEmpty()) {
                user.get().setEmail(userSignupReq.getEmail());
            } else if (!userSignupReq.getPassword().isEmpty()) {
                String newPasshash = BCrypt.hashpw(userSignupReq.getPassword(), BCrypt.gensalt());
                user.get().setPasshash(newPasshash);
            }

            return ResponseEntity.ok(userService.update(user.get()));
        } catch (RuntimeException e) {
            if (e.getMessage().equals("User does not exist")) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // TODO: Implement with JWT
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        try {
            userService.delete(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            if (e.getMessage().equals("User does not exist")) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get Questions

    @GetMapping("/{id}/questions")
    public ResponseEntity<?> getUserQuestions(@PathVariable String id) {
        try {
            return ResponseEntity.ok(userService.getUserById(id).isPresent() ? userService.getUserById(id).get().getQuestions() : null);
        } catch (RuntimeException e) {
            if (e.getMessage().equals("User does not exist")) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get Comments

    @GetMapping("/{id}/comments")
    public ResponseEntity<?> getUserComments(@PathVariable String id) {
        try {
            return ResponseEntity.ok(userService.getUserById(id).isPresent() ? userService.getUserById(id).get().getComments() : null);
        } catch (RuntimeException e) {
            if (e.getMessage().equals("User does not exist")) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get Replies

    @GetMapping("/{id}/replies")
    public ResponseEntity<?> getUserReplies(@PathVariable String id) {
        try {
            return ResponseEntity.ok(userService.getUserById(id).isPresent() ? userService.getUserById(id).get().getReply() : null);
        } catch (RuntimeException e) {
            if (e.getMessage().equals("User does not exist")) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
