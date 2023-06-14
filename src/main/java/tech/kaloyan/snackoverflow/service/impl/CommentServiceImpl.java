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
import tech.kaloyan.snackoverflow.repository.CommentRepository;
import tech.kaloyan.snackoverflow.resources.req.CommentReq;
import tech.kaloyan.snackoverflow.resources.resp.CommentResp;
import tech.kaloyan.snackoverflow.service.CommentService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static tech.kaloyan.snackoverflow.mapper.CommentMapper.MAPPER;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final EntityManagerFactory entityManagerFactory;

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
    public CommentResp save(CommentReq commentReq, User currentUser) {
        if (commentReq.getAuthorId() == null) {
            commentReq.setAuthorId(currentUser.getId());
        } else if (!commentReq.getAuthorId().equals(currentUser.getId())) {
            throw new NotAuthorizedException("User is not authorized to create comment for another user");
        }

        return MAPPER.toCommentResp(commentRepository.save(MAPPER.toComment(commentReq)));
    }

    @Override
    public CommentResp update(String id, CommentReq commentReq, User currentUser) {
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Comment with id " + id + " not found")
        );

        if (!comment.getAuthor().getId().equals(currentUser.getId())) {
            throw new NotAuthorizedException("User is not authorized to update comment for another user");
        }

        comment.setContent(commentReq.getContent());
        return MAPPER.toCommentResp(commentRepository.save(comment));
    }

    @Override
    public void delete(String id, User currentUser) {
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Comment with id " + id + " not found")
        );

        if (!comment.getAuthor().getId().equals(currentUser.getId())) {
            throw new NotAuthorizedException("User is not authorized to delete comment for another user");
        }

        commentRepository.deleteById(id);
    }
}
