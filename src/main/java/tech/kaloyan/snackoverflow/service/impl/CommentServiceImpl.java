/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
    public Comment save(CommentReq commentReq) {
        return commentRepository.save(MAPPER.toComment(commentReq));
    }

    @Override
    public Comment update(String id, CommentReq commentReq) {
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Comment with id " + id + " not found")
        );
        comment.setContent(commentReq.getContent());
        return commentRepository.save(comment);
    }

    @Override
    public void delete(String id) {
        commentRepository.deleteById(id);
    }
}
