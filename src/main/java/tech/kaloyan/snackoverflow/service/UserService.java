/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.service;

import tech.kaloyan.snackoverflow.resources.req.UserSignupReq;
import tech.kaloyan.snackoverflow.resources.resp.*;
import tech.kaloyan.snackoverflow.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserAccountResp> getAll();
    Optional<User> getUserById(String id);
    Optional<UserAccountResp> getById(String id);
    Optional<UserAccountResp> getByUsername(String username);
    Optional<UserAccountResp> getByEmail(String email);
    Optional<UserAccountResp> getByUsernameOrEmail(String username, String email);

    UserResp save(UserSignupReq user);
    UserResp update(String id, UserSignupReq user, User currentUser);
    void delete(String id, User currentUser);

    List<QuestionResp> getUserQuestions(String id);
    List<CommentResp> getUserComments(String id);
    List<ReplyResp> getUserReplies(String id);
}
