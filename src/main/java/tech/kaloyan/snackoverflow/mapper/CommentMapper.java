/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import tech.kaloyan.snackoverflow.resources.req.CommentReq;
import tech.kaloyan.snackoverflow.resources.resp.CommentResp;
import tech.kaloyan.snackoverflow.entity.Comment;

import java.util.List;
import java.util.Map;

@Mapper
public interface CommentMapper {
    CommentMapper MAPPER = Mappers.getMapper(CommentMapper.class);

    @Mapping(target = "author.id", source = "author.id")
    @Mapping(target = "author.username", source = "author.username")
    @Mapping(target = "questionId", source = "question.id")
    CommentResp toCommentResp(Comment comment);

    @Mapping(target = "author.id", source = "authorId")
    @Mapping(target = "question.id", source = "questionId")
    Comment toComment(CommentReq commentReq);

    List<CommentResp> toCommentResps(List<Comment> comments);
}
