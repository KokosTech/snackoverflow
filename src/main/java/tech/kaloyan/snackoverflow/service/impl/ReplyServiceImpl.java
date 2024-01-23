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
import tech.kaloyan.snackoverflow.entity.Reply;
import tech.kaloyan.snackoverflow.entity.User;
import tech.kaloyan.snackoverflow.exeception.BadRequestException;
import tech.kaloyan.snackoverflow.exeception.NotAuthorizedException;
import tech.kaloyan.snackoverflow.repository.ReplyRepository;
import tech.kaloyan.snackoverflow.resources.req.ReplyReq;
import tech.kaloyan.snackoverflow.resources.resp.CommentResp;
import tech.kaloyan.snackoverflow.resources.resp.ReplyResp;
import tech.kaloyan.snackoverflow.service.ReplyService;
import tech.kaloyan.snackoverflow.service.UserService;

import java.util.*;

import static tech.kaloyan.snackoverflow.mapper.ReplyMapper.MAPPER;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {
    private final AuthServiceImpl authService;
    private final EntityManagerFactory entityManagerFactory;
    private final ReplyRepository replyRepository;
    private final CommentServiceImpl commentService;
    private final UserService userService;

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
            User author = reply.getAuthor();

            if (author != null) {
                Optional<User> authorDb = userService.getUserById(
                        reply.getAuthor().getId()
                );

                authorDb.ifPresent(reply::setAuthor);
            }
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
    public ReplyResp save(String questionId, String commentId, ReplyReq replyReq) {
        checkComment(questionId, commentId);
        checkAuthor(replyReq.getAuthorId());

        return MAPPER.toReplyResp(replyRepository.save(MAPPER.toReply(replyReq)));
    }

    private void checkComment(String questionId, String commentId) {
        Optional<CommentResp> comment = commentService.getById(commentId);
        if (comment.isEmpty() || !Objects.equals(questionId, comment.get().getQuestionId())) {
            throw new BadRequestException("Question id does not match");
        }
    }

    private void checkAuthor(String authorId) {
        User currentUser = authService.getUser();

        if (!authorId.equals(currentUser.getId())) {
            throw new NotAuthorizedException("User is not authorized to delete reply for another user");
        }
    }

    private Reply checkReply(String questionId, String commentId, String replyId) {
        Reply reply = replyRepository.findById(replyId).orElseThrow(
                () -> new EntityNotFoundException("Reply with id " + replyId + " not found")
        );

        if (!Objects.equals(commentId, reply.getComment().getId())) {
            throw new BadRequestException("Comment id does not match");
        }

        checkComment(questionId, commentId);
        checkAuthor(reply.getAuthor().getId());

        return reply;
    }

    @Override
    public ReplyResp update(String questionId, String commentId, String replyId, ReplyReq replyReq) {
        Reply reply = checkReply(questionId, commentId, replyId);
        reply.setText(replyReq.getText());
        return MAPPER.toReplyResp(replyRepository.save(reply));
    }

    @Override
    public void delete(String questionId, String commentId, String replyId) {
        replyRepository.delete(checkReply(questionId, commentId, replyId));
    }
}
