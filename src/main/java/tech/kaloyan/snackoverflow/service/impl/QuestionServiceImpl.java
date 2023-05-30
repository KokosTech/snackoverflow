/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.kaloyan.snackoverflow.controller.resources.Req.QuestionReq;
import tech.kaloyan.snackoverflow.controller.resources.Resp.QuestionResp;
import tech.kaloyan.snackoverflow.entity.Question;
import tech.kaloyan.snackoverflow.repository.QuestionRepository;
import tech.kaloyan.snackoverflow.service.QuestionService;

import java.util.List;
import java.util.Optional;

import static tech.kaloyan.snackoverflow.mapper.QuestionMapper.MAPPER;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;

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
        return questionRepository.save(MAPPER.toQuestion(questionReq));
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
