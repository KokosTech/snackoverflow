/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.kaloyan.snackoverflow.resources.req.UserLoginReq;
import tech.kaloyan.snackoverflow.resources.req.UserSignupReq;
import tech.kaloyan.snackoverflow.resources.resp.AuthResp;
import tech.kaloyan.snackoverflow.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResp> login(@Valid @RequestBody UserLoginReq userLoginReq) {
        AuthResp authResp = authService.login(userLoginReq);
        ResponseEntity.BodyBuilder response = ResponseEntity.ok();
        response.header("Set-Cookie", "jwt=" + authResp.getJwt() + "; Path=/; HttpOnly; SameSite=Strict");
        return response.body(authResp);
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResp> signup(@Valid @RequestBody UserSignupReq userSignupReq) {
        AuthResp authResp = authService.register(userSignupReq);
        ResponseEntity.BodyBuilder response = ResponseEntity.ok();
        response.header("Set-Cookie", "jwt=" + authResp.getJwt() + "; Path=/; HttpOnly; SameSite=Strict");
        return response.body(authResp);

    }

    @GetMapping("/logout")
    public void logout() {
        authService.logout();
    }
}
