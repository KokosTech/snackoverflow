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
import tech.kaloyan.snackoverflow.resources.resp.UserResp;
import tech.kaloyan.snackoverflow.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<UserResp> login(@Valid @RequestBody UserLoginReq userLoginReq) {
        UserResp userResp = authService.login(userLoginReq);
        ResponseEntity.BodyBuilder response = ResponseEntity.ok();
        response.header("Set-Cookie", "jwt=" + userResp.getJwt() + "; Path=/; HttpOnly; SameSite=Strict");
        return response.body(userResp);
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResp> signup(@Valid @RequestBody UserSignupReq userSignupReq) {
        UserResp userResp = authService.register(userSignupReq);
        ResponseEntity.BodyBuilder response = ResponseEntity.ok();
        response.header("Set-Cookie", "jwt=" + userResp.getJwt() + "; Path=/; HttpOnly; SameSite=Strict");
        return response.body(userResp);

    }

    @GetMapping("/logout")
    public void logout() {
        authService.logout();
    }
}
