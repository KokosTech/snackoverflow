/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import tech.kaloyan.snackoverflow.entity.User;
import tech.kaloyan.snackoverflow.repository.UserRepository;
import tech.kaloyan.snackoverflow.resources.req.UserSignupReq;
import tech.kaloyan.snackoverflow.resources.resp.UserAccountResp;
import tech.kaloyan.snackoverflow.service.UserService;

import java.util.List;
import java.util.Optional;

import static tech.kaloyan.snackoverflow.mapper.UserMapper.MAPPER;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    final UserRepository userRepository;

    @Override
    public List<UserAccountResp> getAll() {
        return MAPPER.toUserRespList(userRepository.findAll());
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<UserAccountResp> getById(String id) {
        return userRepository.findById(id).map(MAPPER::toUserAccountResp);
    }

    @Override
    public Optional<UserAccountResp> getByUsername(String username) {
        return userRepository.findByUsername(username).map(MAPPER::toUserAccountResp);
    }

    @Override
    public Optional<UserAccountResp> getByEmail(String email) {
        return userRepository.findByEmail(email).map(MAPPER::toUserAccountResp);
    }

    @Override
    public Optional<UserAccountResp> getByUsernameOrEmail(String username, String email) {
        return userRepository.findByUsernameOrEmail(username, email).map(MAPPER::toUserAccountResp);
    }

    @Override
    public User save(UserSignupReq userReq) {
        User user = MAPPER.toUser(userReq);
        user.setPasshash(BCrypt.hashpw(userReq.getPassword(), BCrypt.gensalt()));
        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        return userRepository.save(user);
    }

    @Override
    public void delete(String id) {
        userRepository.deleteById(id);
    }
}
