/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.service.impl;

import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.kaloyan.snackoverflow.entity.User;
import tech.kaloyan.snackoverflow.repository.UserRepository;
import tech.kaloyan.snackoverflow.resources.req.UserLoginReq;
import tech.kaloyan.snackoverflow.resources.req.UserSignupReq;
import tech.kaloyan.snackoverflow.resources.resp.UserResp;
import tech.kaloyan.snackoverflow.service.AuthService;
import tech.kaloyan.snackoverflow.service.JWTService;
import tech.kaloyan.snackoverflow.service.UserService;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import static tech.kaloyan.snackoverflow.mapper.UserMapper.MAPPER;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public UserResp register(UserSignupReq userSignupReq) {
        if (userSignupReq.getEmail() == null || userSignupReq.getEmail().isEmpty()) {
            throw new BadCredentialsException("EMAIL_REQ");
        }

        if (userSignupReq.getUsername() == null || userSignupReq.getUsername().isEmpty()) {
            throw new BadCredentialsException("USERNAME_REQ");
        }

        if (userSignupReq.getPassword() == null || userSignupReq.getPassword().isEmpty()) {
            throw new BadCredentialsException("PASSWORD_REQ");
        }

        System.out.println(userSignupReq.toString());

        User user = MAPPER.toUser(userSignupReq);
        user.setPasshash(passwordEncoder.encode(userSignupReq.getPassword()));
        user.setCreatedOn(new Calendar.Builder().setInstant(new Date()).build());
        user.setLastLogin(new Calendar.Builder().setInstant(new Date()).build());

        String jwt = jwtService.generateToken(user);
        UserResp userResp;

        try {
            userResp= MAPPER.toUserResp(userRepository.save(user));
        } catch (DataIntegrityViolationException e) {
            throw new EntityExistsException("User already exists");
        }

        userResp.setJwt(jwt);
        return userResp;
    }

    @Override
    public UserResp login(UserLoginReq userLoginReq) {
        User user;

        if (userLoginReq.getEmail() == null || userLoginReq.getEmail().isEmpty()) {
            throw new BadCredentialsException("EMAIL_REQ");
        }

        if (userLoginReq.getPassword() == null || userLoginReq.getPassword().isEmpty()) {
            throw new BadCredentialsException("PASSWORD_REQ");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userLoginReq.getEmail(),
                            userLoginReq.getPassword()
                    )
            );

            Optional<User> userOptional = userRepository.findByUsername(userLoginReq.getEmail());
            if (userOptional.isEmpty()) {
                throw new BadCredentialsException("INVALID_CREDENTIALS");
            }
            user = userOptional.get();
            user.setLastLogin(new Calendar.Builder().setInstant(new Date()).build());
            userRepository.save(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new BadCredentialsException("INVALID_CREDENTIALS");
        }

        String jwt = jwtService.generateToken(user);
        UserResp userResp = MAPPER.toUserResp(user);
        userResp.setJwt(jwt);

        return userResp;
    }

    @Override
    public void logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    @Override
    public User getUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
