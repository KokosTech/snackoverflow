/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.service;

import tech.kaloyan.snackoverflow.entity.User;
import tech.kaloyan.snackoverflow.resources.req.ReplyReq;
import tech.kaloyan.snackoverflow.resources.resp.ReplyResp;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ReplyService {
    List<ReplyResp> getAll();

    List<ReplyResp> getAllByAuthorId(String authorId);

    List<ReplyResp> getAllByCommentId(String commentId);

    Optional<ReplyResp> getById(String id);

    List<ReplyResp> getHistoryById(String id);

    List<ReplyResp> getHistoryByIdAndDate(String id, Date date);

    ReplyResp save(String questionId, String commentId, ReplyReq replyReq);

    ReplyResp update(String questionId, String commentId, String replyId, ReplyReq replyReq);

    void delete(String questionId, String commentId, String replyId);
}
