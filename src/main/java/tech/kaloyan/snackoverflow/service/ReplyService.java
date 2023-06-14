/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.service;

import tech.kaloyan.snackoverflow.entity.User;
import tech.kaloyan.snackoverflow.resources.req.ReplyReq;
import tech.kaloyan.snackoverflow.resources.resp.ReplyResp;
import tech.kaloyan.snackoverflow.entity.Reply;

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

    ReplyResp save(ReplyReq replyReq, User currentUser);
    ReplyResp update(String id, ReplyReq replyReq, User currentUser);
    void delete(String id, User currentUser);
}
