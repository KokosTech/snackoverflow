/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import tech.kaloyan.snackoverflow.controller.resources.Req.QuestionReq;
import tech.kaloyan.snackoverflow.controller.resources.Resp.QuestionResp;
import tech.kaloyan.snackoverflow.entity.Question;
import tech.kaloyan.snackoverflow.entity.Rated;

import java.util.List;
import java.util.Map;

@Mapper
public interface QuestionMapper {
    QuestionMapper MAPPER = Mappers.getMapper(QuestionMapper.class);

    @Mapping(target = "author", expression = "java(mapify(\"id\", question.getAuthor().getId()))")
    @Mapping(target = "image", expression = "java(mapify(\"url\", question.getImage().get(0).getUrl()))")
    @Mapping(target = "rating", expression = "java(getRating(question))")
    QuestionResp toQuestionResp(Question question);

    @Mapping(target = "author.id", source = "authorId")
    @Mapping(target = "image.url", source = "images.url")
    @Mapping(target = "image.alt", source = "images.alt")
    Question toQuestion(QuestionReq questionReq);

    default Double getRating(Question question) {
        return question.getRated().stream().mapToDouble(Rated::getRating).average().orElse(0.0);
    }

    default Map<String, String> mapify (String key, String value) {
        return Map.of(key, value);
    }

    List<QuestionResp> toQuestionResps(List<Question> questions);
}
