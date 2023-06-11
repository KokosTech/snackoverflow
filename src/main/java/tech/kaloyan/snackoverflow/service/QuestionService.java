/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.service;

import tech.kaloyan.snackoverflow.entity.User;
import tech.kaloyan.snackoverflow.resources.req.QuestionReq;
import tech.kaloyan.snackoverflow.resources.resp.QuestionResp;
import tech.kaloyan.snackoverflow.entity.Question;

import java.util.List;
import java.util.Optional;

public interface QuestionService {
    List<QuestionResp> getAll();
    List<QuestionResp> getAllByAuthorId(String authorId);
    Optional<QuestionResp> getById(String id);

    Question save(QuestionReq questionReq, User currentUser);
    Question update(String id, QuestionReq questionReq, User currentUser);
    void delete(String id, User currentUser);
}
