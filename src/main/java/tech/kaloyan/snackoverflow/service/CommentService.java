/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.service;

import tech.kaloyan.snackoverflow.entity.User;
import tech.kaloyan.snackoverflow.resources.req.CommentReq;
import tech.kaloyan.snackoverflow.resources.resp.CommentResp;
import tech.kaloyan.snackoverflow.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    List<CommentResp> getAll();
    List<CommentResp> getAllByAuthorId(String authorId);
    List<CommentResp> getAllByQuestionId(String questionId);
    Optional<CommentResp> getById(String id);

    Comment save(CommentReq commentReq, User currentUser);

    Comment update(String id, CommentReq commentReq, User currentUser);
    void delete(String id, User currentUser);
}
