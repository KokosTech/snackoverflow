/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tech.kaloyan.snackoverflow.service.JWTService;
import tech.kaloyan.snackoverflow.uilts.FilterExceptionResponseHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final JWTService jwtService;
    private final UserDetailsService userDetailsService;

    private final List<String> excludeUrlPatterns = new ArrayList<String>(Arrays.asList("/swagger-ui.html", "/swagger-uui.html", "/swagger-ui", "/v3/api-docs", "/webjars/springfox-swagger-ui/springfox.css", "/webjars/springfox-swagger-ui/swagger-ui-bundle.js", "/webjars/springfox-swagger-ui/swagger-ui.css", "/webjars/springfox-swagger-ui/swagger-ui-standalone-preset.js", "/webjars/springfox-swagger-ui/springfox.js", "/swagger-resources/configuration/ui", "/webjars/springfox-swagger-ui/favicon-32x32.png", "/swagger-resources/configuration/security", "/swagger-resources", "/v3/api-docs", "/webjars/springfox-swagger-ui/fonts/titillium-web-v6-latin-700.woff2", "/webjars/springfox-swagger-ui/fonts/open-sans-v15-latin-regular.woff2", "/webjars/springfox-swagger-ui/fonts/open-sans-v15-latin-700.woff2", "/webjars/springfox-swagger-ui/favicon-16x16.png", "/api/v1/auth/login", "/api/v1/auth/signup"));

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            FilterExceptionResponseHandler.sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "TOKEN_MISSING");
            return;
        }

        final String jwt = authHeader.substring(7); // Assuming 'Bearer ' is 7 characters long

        String username = null;
        try {
            username = jwtService != null ? jwtService.extractUsername(jwt) : null;
        } catch (ExpiredJwtException e) {
            FilterExceptionResponseHandler.sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "TOKEN_EXPIRED");
            return;
        } catch (JwtException e) {
            FilterExceptionResponseHandler.sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "TOKEN_INVALID");
            return;
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService != null ? userDetailsService.loadUserByUsername(username) : null;
            if (userDetails != null && jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestURI = request.getRequestURI();
        for (String pattern : excludeUrlPatterns) {
            if (requestURI.contains(pattern)) {
                return true;
            }
        }
        return false;
    }
}
