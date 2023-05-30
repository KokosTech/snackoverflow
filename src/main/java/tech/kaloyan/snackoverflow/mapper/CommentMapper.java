/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import tech.kaloyan.snackoverflow.controller.resources.Req.CommentReq;
import tech.kaloyan.snackoverflow.controller.resources.Resp.CommentResp;
import tech.kaloyan.snackoverflow.entity.Comment;

import java.util.List;
import java.util.Map;

@Mapper
public interface CommentMapper {
    CommentMapper MAPPER = Mappers.getMapper(CommentMapper.class);

    @Mapping(target = "author", expression = "java(mapify(\"id\", comment.getAuthor().getId()))")
    @Mapping(target = "createdOn", expression = "java(comment.getCreatedOn().getTime().toString())")
    @Mapping(target = "questionId", source = "question.id")
    CommentResp toCommentResp(Comment comment);

    @Mapping(target = "author.id", source = "authorId")
    @Mapping(target = "question.id", source = "questionId")
    Comment toComment(CommentReq commentReq);

    default Map<String, String> mapify (String key, String value) {
        return Map.of(key, value);
    }

    List<CommentResp> toCommentResps(List<Comment> comments);
}
