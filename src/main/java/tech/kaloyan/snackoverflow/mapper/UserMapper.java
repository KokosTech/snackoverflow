/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import tech.kaloyan.snackoverflow.entity.User;
import tech.kaloyan.snackoverflow.resources.req.UserSignupReq;
import tech.kaloyan.snackoverflow.resources.resp.*;

import java.util.List;

@Mapper
public interface UserMapper {
    UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "questions", expression = "java(getQuestions(user))")
    UserAccountResp toUserAccountResp(User user);

    UserResp toUserResp(User user);
    AuthResp toAuthResp(User user);

    @Mapping(target = "lastLogin", expression = "java(user.getLastLogin().getTime().toString())")
    @Mapping(target = "questions", expression = "java(getQuestions(user))")
    @Mapping(target = "saved", expression = "java(getSaved(user))")
    @Mapping(target = "comments", expression = "java(getComments(user))")
    @Mapping(target = "replies", expression = "java(getReplies(user))")
    AuthUserResp toAuthUserResp(User user);

    User toUser(UserSignupReq userReq);

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

    List<UserAccountResp> toUserRespList(List<User> users);
}
