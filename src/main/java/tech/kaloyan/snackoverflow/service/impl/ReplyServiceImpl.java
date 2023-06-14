/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.service.impl;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.stereotype.Service;
import tech.kaloyan.snackoverflow.entity.Comment;
import tech.kaloyan.snackoverflow.entity.User;
import tech.kaloyan.snackoverflow.exeception.NotAuthorizedException;
import tech.kaloyan.snackoverflow.mapper.CommentMapper;
import tech.kaloyan.snackoverflow.resources.req.ReplyReq;
import tech.kaloyan.snackoverflow.resources.resp.CommentResp;
import tech.kaloyan.snackoverflow.resources.resp.ReplyResp;
import tech.kaloyan.snackoverflow.entity.Reply;
import tech.kaloyan.snackoverflow.repository.ReplyRepository;
import tech.kaloyan.snackoverflow.service.ReplyService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static tech.kaloyan.snackoverflow.mapper.ReplyMapper.MAPPER;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {
    EntityManagerFactory entityManagerFactory;
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

    private List<ReplyResp> getReplyHistory(String id) {
        AuditReader auditReader = AuditReaderFactory.get(entityManagerFactory.createEntityManager());
        List<Number> revisions = auditReader.getRevisions(Reply.class, id);
        List<Reply> replies = new ArrayList<>();
        for (Number idRev : revisions) {
            Reply reply = auditReader.find(Reply.class, id, idRev);
            replies.add(reply);
        }
        return MAPPER.toReplyResps(replies);
    }

    @Override
    public List<ReplyResp> getHistoryById(String id) {
        return getReplyHistory(id);
    }

    @Override
    public List<ReplyResp> getHistoryByIdAndDate(String id, Date date) {
        return getReplyHistory(id).stream().filter(
                comment -> comment.getCreatedOn().before(date)
        ).toList();
    }

    @Override
    public ReplyResp save(ReplyReq replyReq, User currentUser) {
        if (replyReq.getAuthorId() == null) {
            replyReq.setAuthorId(currentUser.getId());
        } else if (!replyReq.getAuthorId().equals(currentUser.getId())) {
            throw new NotAuthorizedException("User is not authorized to create reply for another user");
        }

        return MAPPER.toReplyResp(replyRepository.save(MAPPER.toReply(replyReq)));
    }

    @Override
    public ReplyResp update(String id, ReplyReq replyReq, User currentUser) {
        Reply reply = replyRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Reply with id " + id + " not found")
        );

        if (!reply.getAuthor().getId().equals(currentUser.getId())) {
            throw new NotAuthorizedException("User is not authorized to update reply for another user");
        }

        reply.setText(replyReq.getText());
        return MAPPER.toReplyResp(replyRepository.save(reply));
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
