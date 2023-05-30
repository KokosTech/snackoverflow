/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.kaloyan.snackoverflow.entity.Image;
import tech.kaloyan.snackoverflow.repository.ImageRepository;
import tech.kaloyan.snackoverflow.resources.req.ImageReq;
import tech.kaloyan.snackoverflow.resources.req.QuestionReq;
import tech.kaloyan.snackoverflow.resources.resp.QuestionResp;
import tech.kaloyan.snackoverflow.entity.Question;
import tech.kaloyan.snackoverflow.repository.QuestionRepository;
import tech.kaloyan.snackoverflow.repository.UserRepository;
import tech.kaloyan.snackoverflow.service.QuestionService;

import java.util.List;
import java.util.Optional;

import static tech.kaloyan.snackoverflow.mapper.QuestionMapper.MAPPER;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final ImageRepository imageRepository;

    @Override
    public List<QuestionResp> getAll() {
        return MAPPER.toQuestionResps(questionRepository.findAll());
    }

    @Override
    public List<QuestionResp> getAllByAuthorId(String authorId) {
        return MAPPER.toQuestionResps(questionRepository.findAllByAuthorId(authorId));
    }

    @Override
    public Optional<QuestionResp> getById(String id) {
        return questionRepository.findById(id).map(MAPPER::toQuestionResp);
    }

    @Override
    public Question save(QuestionReq questionReq) {
        Question question = MAPPER.toQuestion(questionReq);
        List<Image> images = question.getImage();
        question.setImage(null);
        question = questionRepository.save(question);

        for (Image image : images) {
            image.setQuestion(question);
            imageRepository.save(image);
        }

        return question;
    }

    @Override
    public Question update(String id, QuestionReq questionReq) {
        Question question = questionRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Question with id " + id + " not found")
        );
        question.setTitle(questionReq.getTitle());
        question.setDescription(questionReq.getDescription());
        return questionRepository.save(question);
    }

    @Override
    public void delete(String id) {
        questionRepository.deleteById(id);
    }
}
