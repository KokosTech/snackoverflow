/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import tech.kaloyan.snackoverflow.entity.User;
import tech.kaloyan.snackoverflow.repository.UserRepository;
import tech.kaloyan.snackoverflow.resources.req.UserLoginReq;
import tech.kaloyan.snackoverflow.resources.req.UserSignupReq;
import tech.kaloyan.snackoverflow.resources.resp.UserResp;
import tech.kaloyan.snackoverflow.service.AuthService;
import tech.kaloyan.snackoverflow.service.UserService;

import static tech.kaloyan.snackoverflow.mapper.UserMapper.MAPPER;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    public UserResp register(UserSignupReq userSignupReq) {
        if (userService.getByUsernameOrEmail(userSignupReq.getUsername(), userSignupReq.getEmail()).isEmpty()) {
            return MAPPER.toUserResp(userService.save(userSignupReq));
        } else {
            throw new RuntimeException("User already exists");
        }
    }

    @Override
    public UserResp login(UserLoginReq userLoginReq) {
        if(userRepository.findByEmail(userLoginReq.getEmail()).isEmpty()) {
            throw new RuntimeException("User does not exist");
        }

        User user = userRepository.findByEmail(userLoginReq.getEmail()).get();

        String password = userLoginReq.getPassword();
        String passhash = user.getPasshash();

        if(!BCrypt.checkpw(password, passhash)) {
            throw new RuntimeException("Wrong password");
        }

        // TODO (next week): JWT

        return MAPPER.toUserResp(user);
    }

    @Override
    public void logout() {
        // TODO (next week): JWT
    }
}
