/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.service;

import tech.kaloyan.snackoverflow.entity.User;
import tech.kaloyan.snackoverflow.resources.req.RatedReq;
import tech.kaloyan.snackoverflow.resources.resp.RatedResp;
import tech.kaloyan.snackoverflow.entity.Rated;

import java.util.List;
import java.util.Optional;

public interface RatedService {
    List<RatedResp> getAll();
    List<RatedResp> getAllByUserId(String userId);
    List<RatedResp> getAllByQuestionId(String questionId);
    Optional<RatedResp> getById(String id);

    RatedResp save(RatedReq ratedReq, String questionId, User currentUser);
    void delete(String id, User currentUser);
}
