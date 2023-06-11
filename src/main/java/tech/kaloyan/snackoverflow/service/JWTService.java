/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.function.Function;

public interface JWTService {
    String extractUsername(String jwt);
    <T> T extractClaim(String jwt, Function<Claims, T> claimsResolver);
    String generateToken(UserDetails userDetails);
    String generateToken(Map<String, Object> extractClaims, UserDetails userDetails);
    boolean isTokenValid(String jwt, UserDetails userDetails);
}
