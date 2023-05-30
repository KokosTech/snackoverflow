/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.service;

import tech.kaloyan.snackoverflow.controller.resources.Req.RatedReq;
import tech.kaloyan.snackoverflow.controller.resources.Resp.RatedResp;
import tech.kaloyan.snackoverflow.entity.Rated;

import java.util.List;
import java.util.Optional;

public interface RatedService {
    List<RatedResp> getAll();
    List<RatedResp> getAllByUserId(String userId);
    List<RatedResp> getAllByQuestionId(String questionId);
    Optional<RatedResp> getById(String id);

    Rated save(RatedReq ratedReq);
    Rated update(String id, RatedReq ratedReq);
    void delete(String id);
}
