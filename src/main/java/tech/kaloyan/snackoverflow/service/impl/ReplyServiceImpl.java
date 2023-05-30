/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.kaloyan.snackoverflow.resources.req.ReplyReq;
import tech.kaloyan.snackoverflow.resources.resp.ReplyResp;
import tech.kaloyan.snackoverflow.entity.Reply;
import tech.kaloyan.snackoverflow.repository.ReplyRepository;
import tech.kaloyan.snackoverflow.service.ReplyService;

import java.util.List;
import java.util.Optional;

import static tech.kaloyan.snackoverflow.mapper.ReplyMapper.MAPPER;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {
    ReplyRepository replyRepository;

    @Override
    public List<ReplyResp> getAll() {
        return MAPPER.toReplyResps(replyRepository.findAll());
    }

    @Override
    public List<ReplyResp> getAllByAuthorId(String authorId) {
        return MAPPER.toReplyResps(replyRepository.findAllByAuthorId(authorId));
    }

    @Override
    public List<ReplyResp> getAllByCommentId(String commentId) {
        return MAPPER.toReplyResps(replyRepository.findAllByCommentId(commentId));
    }

    @Override
    public Optional<ReplyResp> getById(String id) {
        return replyRepository.findById(id).map(MAPPER::toReplyResp);
    }

    @Override
    public Reply save(ReplyReq replyReq) {
        return replyRepository.save(MAPPER.toReply(replyReq));
    }

    @Override
    public Reply update(String id, ReplyReq replyReq) {
        Reply reply = replyRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Reply with id " + id + " not found")
        );
        reply.setText(replyReq.getText());
        return replyRepository.save(reply);
    }

    @Override
    public void delete(String id) {

    }
}
