/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.service;

import tech.kaloyan.snackoverflow.entity.User;
import tech.kaloyan.snackoverflow.resources.req.SavedReq;
import tech.kaloyan.snackoverflow.resources.resp.SavedResp;

import java.util.List;
import java.util.Optional;

public interface SavedService {
    List<SavedResp> getAll();

    List<SavedResp> getAllByUserId(String userId);

    List<SavedResp> getAllByQuestionId(String questionId);

    Optional<SavedResp> getById(String id);

    SavedResp save(String id, SavedReq saved, User currentUser);

    void delete(String id, User currentUser);
}
