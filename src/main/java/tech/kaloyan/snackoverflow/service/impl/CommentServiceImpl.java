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
import tech.kaloyan.snackoverflow.exeception.BadRequestException;
import tech.kaloyan.snackoverflow.exeception.NotAuthorizedException;
import tech.kaloyan.snackoverflow.repository.CommentRepository;
import tech.kaloyan.snackoverflow.resources.req.CommentReq;
import tech.kaloyan.snackoverflow.resources.resp.CommentResp;
import tech.kaloyan.snackoverflow.service.CommentService;
import tech.kaloyan.snackoverflow.service.UserService;

import java.util.*;

import static tech.kaloyan.snackoverflow.mapper.CommentMapper.MAPPER;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final EntityManagerFactory entityManagerFactory;
    private final AuthServiceImpl authService;
    private final QuestionServiceImpl questionService;
    private final UserService userService;

    private void checkQuestion(String questionId, String commentId) {
        if (!Objects.equals(questionId, commentId)) {
            throw new BadRequestException("Question id does not match");
        }

        questionService.getById(questionId);
    }

    private String getAuthorId(String authorId) {
        String currentUserId = authService.getUser().getId();
        if (!authorId.equals(currentUserId)) {
            throw new NotAuthorizedException("User is not authorized to create comment for another user");
        }

        return currentUserId;
    }

    @Override
    public List<CommentResp> getAll() {
        return MAPPER.toCommentResps(commentRepository.findAll());
    }

    @Override
    public List<CommentResp> getAllByAuthorId(String authorId) {
        return MAPPER.toCommentResps(commentRepository.findAllByAuthorId(authorId));
    }

    @Override
    public List<CommentResp> getAllByQuestionId(String questionId) {
        return MAPPER.toCommentResps(commentRepository.findAllByQuestionId(questionId));
    }

    @Override
    public Optional<CommentResp> getById(String id) {
        return commentRepository.findById(id).map(MAPPER::toCommentResp);
    }

    private List<CommentResp> getCommentHistory(String id) {
        AuditReader auditReader = AuditReaderFactory.get(entityManagerFactory.createEntityManager());
        List<Number> revisions = auditReader.getRevisions(Comment.class, id);
        List<Comment> comments = new ArrayList<>();
        for (Number idRev : revisions) {
            Comment comment = auditReader.find(Comment.class, id, idRev);
            User author = comment.getAuthor();

            if (author != null) {
                Optional<User> authorDb = userService.getUserById(
                        comment.getAuthor().getId()
                );

                authorDb.ifPresent(comment::setAuthor);
            }
            comments.add(comment);
        }
        return MAPPER.toCommentResps(comments);
    }

    @Override
    public List<CommentResp> getHistoryById(String id) {
        return getCommentHistory(id);
    }

    @Override
    public List<CommentResp> getHistoryByIdAndDate(String id, Date date) {
        return getCommentHistory(id).stream().filter(
                comment -> comment.getCreatedOn().before(date)
        ).toList();
    }

    @Override
    public CommentResp save(String questionId, CommentReq commentReq) {
        checkQuestion(questionId, commentReq.getQuestionId());
        commentReq.setAuthorId(getAuthorId(commentReq.getAuthorId()));
        return MAPPER.toCommentResp(commentRepository.save(MAPPER.toComment(commentReq)));
    }

    @Override
    public CommentResp update(String id, String questionId, CommentReq commentReq) {
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Comment with id " + id + " not found")
        );

        checkQuestion(questionId, comment.getQuestion().getId());
        commentReq.setAuthorId(getAuthorId(commentReq.getAuthorId()));

        comment.setContent(commentReq.getContent());
        return MAPPER.toCommentResp(commentRepository.save(comment));
    }

    @Override
    public void delete(String id, String questionId) {
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Comment with id " + id + " not found")
        );

        checkQuestion(questionId, comment.getQuestion().getId());
        getAuthorId(comment.getAuthor().getId());

        commentRepository.deleteById(id);
    }
}
