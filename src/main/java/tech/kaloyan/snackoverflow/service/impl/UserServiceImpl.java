/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.kaloyan.snackoverflow.controller.resources.Resp.UserResp;
import tech.kaloyan.snackoverflow.entity.User;
import tech.kaloyan.snackoverflow.repository.UserRepository;
import tech.kaloyan.snackoverflow.service.UserService;

import java.util.List;
import java.util.Optional;

import static tech.kaloyan.snackoverflow.mapper.UserMapper.MAPPER;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    final UserRepository userRepository;

    @Override
    public List<UserResp> getAll() {
        return MAPPER.toUserRespList(userRepository.findAll());
    }

    @Override
    public Optional<UserResp> getById(String id) {
        return userRepository.findById(id).map(MAPPER::toUserResp);
    }

    @Override
    public User save(User user) {
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
