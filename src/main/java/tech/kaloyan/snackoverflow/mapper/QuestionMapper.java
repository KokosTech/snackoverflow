/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import tech.kaloyan.snackoverflow.resources.req.QuestionReq;
import tech.kaloyan.snackoverflow.resources.resp.QuestionResp;
import tech.kaloyan.snackoverflow.entity.Image;
import tech.kaloyan.snackoverflow.entity.Question;
import tech.kaloyan.snackoverflow.entity.Rated;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper
public interface QuestionMapper {
    QuestionMapper MAPPER = Mappers.getMapper(QuestionMapper.class);

    @Mapping(target = "author.id", source = "author.id")
    @Mapping(target = "author.username", source = "author.username")
    @Mapping(target = "images", source = "image")
    @Mapping(target = "rating", expression = "java(getRating(question))")
    QuestionResp toQuestionResp(Question question);

//    QuestionReq (images -> map<url, alt>) -> Question image (list of Image) -> Question

    @Mapping(target = "author.id", source = "authorId")
    @Mapping(target = "image", source = "images")
    @Mapping(target = "validFrom", source = "validFrom")
    Question toQuestion(QuestionReq questionReq);

    default Double getRating(Question question) {
        if (question.getRated() == null || question.getRated().isEmpty()) {
            return 0.0;
        }

        return question.getRated().stream().mapToDouble(Rated::getRating).average().orElse(0.0);
    }

    List<QuestionResp> toQuestionResps(List<Question> questions);
}
