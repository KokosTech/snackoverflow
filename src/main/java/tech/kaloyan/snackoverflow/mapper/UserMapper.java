/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import tech.kaloyan.snackoverflow.controller.resources.Resp.*;
import tech.kaloyan.snackoverflow.entity.User;

import java.util.List;

@Mapper
public interface UserMapper {
    UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "questions", expression = "java(getQuestions(user))")
    UserResp toUserResp(User user);

    @Mapping(target = "lastLogin", expression = "java(user.getLastLogin().getTime().toString())")
    @Mapping(target = "questions", expression = "java(getQuestions(user))")
    @Mapping(target = "saved", expression = "java(getSaved(user))")
    @Mapping(target = "comments", expression = "java(getComments(user))")
    @Mapping(target = "reply", expression = "java(getReplies(user))")
    AuthUserResp toAuthUserResp(User user);

    default List<QuestionResp> getQuestions(User user) {
        return QuestionMapper.MAPPER.toQuestionResps(user.getQuestions());
    }

    default List<SavedResp> getSaved(User user) {
        return SavedMapper.MAPPER.toSavedResps(user.getSaved());
    }

    default List<CommentResp> getComments(User user) {
        return CommentMapper.MAPPER.toCommentResps(user.getComments());
    }

    default List<ReplyResp> getReplies(User user) {
        return ReplyMapper.MAPPER.toReplyResps(user.getReply());
    }
}
