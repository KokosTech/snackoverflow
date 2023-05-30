/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.service;

import tech.kaloyan.snackoverflow.controller.resources.Resp.UserResp;
import tech.kaloyan.snackoverflow.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserResp> getAll();
    Optional<UserResp> getById(String id);

    User save(User user);
    User update(User user);
    void delete(String id);
}
