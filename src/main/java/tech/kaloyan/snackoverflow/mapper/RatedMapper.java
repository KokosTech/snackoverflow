/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import tech.kaloyan.snackoverflow.controller.resources.Req.RatedReq;
import tech.kaloyan.snackoverflow.controller.resources.Resp.RatedResp;
import tech.kaloyan.snackoverflow.entity.Rated;

@Mapper
public interface RatedMapper {
    RatedMapper MAPPER = Mappers.getMapper(RatedMapper.class);

    @Mapping(target="questionId", source="question.id")
    @Mapping(target="questionTitle", source="question.title")
    @Mapping(target="userId", source="user.id")
    RatedResp toRatedResp(Rated rated);

    @Mapping(target="question.id", source="questionId")
    @Mapping(target="user.id", source="userId")
    Rated toRated(RatedReq ratedReq);
}
