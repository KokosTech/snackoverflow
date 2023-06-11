/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.kaloyan.snackoverflow.entity.User;
import tech.kaloyan.snackoverflow.exeception.NotAuthorizedException;
import tech.kaloyan.snackoverflow.resources.req.CommentReq;
import tech.kaloyan.snackoverflow.resources.resp.CommentResp;
import tech.kaloyan.snackoverflow.entity.Comment;
import tech.kaloyan.snackoverflow.repository.CommentRepository;
import tech.kaloyan.snackoverflow.service.CommentService;

import java.util.List;
import java.util.Optional;

import static tech.kaloyan.snackoverflow.mapper.CommentMapper.MAPPER;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

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

    @Override
    public Comment save(CommentReq commentReq, User currentUser) {
        if (commentReq.getAuthorId() == null) {
            commentReq.setAuthorId(currentUser.getId());
        } else if (!commentReq.getAuthorId().equals(currentUser.getId())) {
            throw new NotAuthorizedException("User is not authorized to create comment for another user");
        }

        return commentRepository.save(MAPPER.toComment(commentReq));
    }

    @Override
    public Comment update(String id, CommentReq commentReq, User currentUser) {
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Comment with id " + id + " not found")
        );

        if (!comment.getAuthor().getId().equals(currentUser.getId())) {
            throw new NotAuthorizedException("User is not authorized to update comment for another user");
        }

        comment.setContent(commentReq.getContent());
        return commentRepository.save(comment);
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
