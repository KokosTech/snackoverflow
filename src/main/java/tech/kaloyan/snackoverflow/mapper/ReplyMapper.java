/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import tech.kaloyan.snackoverflow.controller.resources.Req.ReplyReq;
import tech.kaloyan.snackoverflow.controller.resources.Resp.ReplyResp;
import tech.kaloyan.snackoverflow.entity.Reply;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReplyMapper {
    ReplyMapper MAPPER = Mappers.getMapper(ReplyMapper.class);

    @Mapping(target = "author", expression = "java(mapify(\"id\", reply.getAuthor().getId()))")
    @Mapping(target = "commentId", source = "comment.id")
    ReplyResp toReplyResp(Reply reply);

    @Mapping(target = "author.id", source = "authorId")
    @Mapping(target = "comment.id", source = "commentId")
    Reply toReply(ReplyReq replyResp);

    default Map<String, String> mapify(String key, String value) {
        return Map.of(key, value);
    }

    List<ReplyResp> toReplyResps(List<Reply> replies);
}
