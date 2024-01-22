/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.service.impl;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.kaloyan.snackoverflow.entity.Image;
import tech.kaloyan.snackoverflow.entity.Question;
import tech.kaloyan.snackoverflow.entity.User;
import tech.kaloyan.snackoverflow.exeception.NotAuthorizedException;
import tech.kaloyan.snackoverflow.repository.ImageRepository;
import tech.kaloyan.snackoverflow.repository.QuestionRepository;
import tech.kaloyan.snackoverflow.resources.req.QuestionReq;
import tech.kaloyan.snackoverflow.resources.resp.QuestionResp;
import tech.kaloyan.snackoverflow.service.QuestionService;
import tech.kaloyan.snackoverflow.service.UserService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static tech.kaloyan.snackoverflow.mapper.QuestionMapper.MAPPER;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final EntityManagerFactory entityManagerFactory;
    private final QuestionRepository questionRepository;
    private final ImageRepository imageRepository;

    private final UserService userService;

    AuditReader auditReader;

    @PostConstruct
    public void init() {
        this.auditReader = AuditReaderFactory.get(entityManagerFactory.createEntityManager());
    }

    @Override
    public List<QuestionResp> getAll() {
        return MAPPER.toQuestionResps(questionRepository.findAll());
    }

    @Override
    public List<QuestionResp> getAllByAuthorId(String authorId) {
        if (authorId == null) {
            return MAPPER.toQuestionResps(questionRepository.findAll());
        }

        return MAPPER.toQuestionResps(questionRepository.findAllByAuthorId(authorId));
    }

    @Override
    public QuestionResp getById(String id) {
        Optional<QuestionResp> optionalQuestion = questionRepository.findById(id).map(MAPPER::toQuestionResp);

        if (optionalQuestion.isEmpty()) {
            throw new EntityNotFoundException("Question with id " + id + " not found");
        }

        return optionalQuestion.get();
    }


    private List<QuestionResp> getQuestionHistory(String id) {
        List<Number> revisions = auditReader.getRevisions(Question.class, id);

        if (revisions.isEmpty()) {
            throw new EntityNotFoundException("Question with id " + id + " not found");
        }

        List<Question> boards = new ArrayList<>();

        for (Number idRev : revisions) {
            Question board = auditReader.find(Question.class, id, idRev);
            System.out.println(board);
            User author = board.getAuthor();

            if (author != null) {
                Optional<User> authorDb = userService.getUserById(
                        board.getAuthor().getId()
                );

                authorDb.ifPresent(board::setAuthor);
            }

            boards.add(board);
        }

        return MAPPER.toQuestionResps(boards);
    }

    @Override
    public List<QuestionResp> getHistoryById(String id) {
        return getQuestionHistory(id);
    }

    @Override
    public List<QuestionResp> getHistoryByIdAndDate(String id, Date date) {
        return getQuestionHistory(id).stream().filter(question -> question.getCreatedOn().before(date)).toList();
    }

    @Override
    @Transactional
    public QuestionResp save(QuestionReq questionReq, User currentUser) {
        if (questionReq.getAuthorId() == null) {
            questionReq.setAuthorId(currentUser.getId());
        } else if (!questionReq.getAuthorId().equals(currentUser.getId())) {
            throw new NotAuthorizedException("User is not authorized to create question for another user");
        }

        Question question = MAPPER.toQuestion(questionReq);
        List<Image> images = question.getImage();
        question.setImage(null);

        System.out.println(question);
        question = questionRepository.save(question);

        for (Image image : images) {
            image.setQuestion(question);
            Image savedImage = imageRepository.save(image);
            if (question.getImage() == null) {
                question.setImage(new ArrayList<>());
                question.getImage().add(savedImage);
            } else {
                question.getImage().add(savedImage);
            }
        }

        return MAPPER.toQuestionResp(question);
    }

    @Override
    public QuestionResp update(String id, QuestionReq questionReq, User currentUser) {
        Question question = questionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Question with id " + id + " not found"));

        if (!question.getAuthor().getId().equals(currentUser.getId())) {
            throw new NotAuthorizedException("User is not authorized to update question for another user");
        }

        question.setTitle(questionReq.getTitle());
        question.setDescription(questionReq.getDescription());
        return MAPPER.toQuestionResp(questionRepository.save(question));
    }

    @Override
    public void delete(String id, User currentUser) {
        Question question = questionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Question with id " + id + " not found"));

        if (!question.getAuthor().getId().equals(currentUser.getId())) {
            throw new NotAuthorizedException("User is not authorized to delete question for another user");
        }

        questionRepository.delete(question);
    }
}
