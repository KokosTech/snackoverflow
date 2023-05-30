/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import tech.kaloyan.snackoverflow.resources.req.SavedReq;
import tech.kaloyan.snackoverflow.resources.resp.SavedResp;
import tech.kaloyan.snackoverflow.entity.Saved;

import java.util.List;

@Mapper
public interface SavedMapper {
    SavedMapper MAPPER = Mappers.getMapper(SavedMapper.class);

    @Mapping(target = "questionId", source = "question.id")
    @Mapping(target = "questionTitle", source = "question.title")
    @Mapping(target = "authorId", source = "question.author.id")
    @Mapping(target = "savedOn", expression = "java(saved.getSavedOn().getTime().toString())")
    @Mapping(target = "userId", source = "user.id")
    SavedResp toSavedResp(Saved saved);

    @Mapping(target = "question.id", source = "questionId")
    @Mapping(target = "user.id", source = "userId")
    Saved toSaved(SavedReq savedReq);

    List<SavedResp> toSavedResps(List<Saved> saveds);
}
