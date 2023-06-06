/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.kaloyan.snackoverflow.resources.req.UserLoginReq;
import tech.kaloyan.snackoverflow.resources.req.UserSignupReq;
import tech.kaloyan.snackoverflow.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginReq userLoginReq) {
        try {
            return ResponseEntity.ok(authService.login(userLoginReq));
        } catch (RuntimeException e) {
            if (e.getMessage().equals("User does not exist")) {
                return ResponseEntity.notFound().build();
            } else if (e.getMessage().equals("Wrong password")) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserSignupReq userSignupReq) {
        try {
            return ResponseEntity.ok(authService.register(userSignupReq));
        } catch (RuntimeException e) {
            if (e.getMessage().equals("User already exists")) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/logout")
    public void logout() {
        // TODO (next week): JWT
    }
}
