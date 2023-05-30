/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import tech.kaloyan.snackoverflow.resources.req.RatedReq;
import tech.kaloyan.snackoverflow.resources.resp.RatedResp;
import tech.kaloyan.snackoverflow.entity.Rated;

import java.util.List;

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

    List<RatedResp> toRatedResps(List<Rated> ratedList);
}
