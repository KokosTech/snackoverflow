/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.kaloyan.snackoverflow.entity.User;
import tech.kaloyan.snackoverflow.exeception.NotAuthorizedException;
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
    public Reply save(ReplyReq replyReq, User currentUser) {
        if (replyReq.getAuthorId() == null) {
            replyReq.setAuthorId(currentUser.getId());
        } else if (!replyReq.getAuthorId().equals(currentUser.getId())) {
            throw new NotAuthorizedException("User is not authorized to create reply for another user");
        }

        return replyRepository.save(MAPPER.toReply(replyReq));
    }

    @Override
    public Reply update(String id, ReplyReq replyReq, User currentUser) {
        Reply reply = replyRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Reply with id " + id + " not found")
        );

        if (!reply.getAuthor().getId().equals(currentUser.getId())) {
            throw new NotAuthorizedException("User is not authorized to update reply for another user");
        }

        reply.setText(replyReq.getText());
        return replyRepository.save(reply);
    }

    @Override
    public void delete(String id, User currentUser) {
        Reply reply = replyRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Reply with id " + id + " not found")
        );

        if (!reply.getAuthor().getId().equals(currentUser.getId())) {
            throw new NotAuthorizedException("User is not authorized to delete reply for another user");
        }

        replyRepository.delete(reply);
    }
}
