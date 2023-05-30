/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.service;

import tech.kaloyan.snackoverflow.controller.resources.Req.ReplyReq;
import tech.kaloyan.snackoverflow.controller.resources.Resp.ReplyResp;
import tech.kaloyan.snackoverflow.entity.Reply;

import java.util.List;
import java.util.Optional;

public interface ReplyService {
    List<ReplyResp> getAll();
    List<ReplyResp> getAllByAuthorId(String authorId);
    List<ReplyResp> getAllByCommentId(String commentId);
    Optional<ReplyResp> getById(String id);

    Reply save(ReplyReq replyReq);
    Reply update(String id, ReplyReq replyReq);
    void delete(String id);
}
