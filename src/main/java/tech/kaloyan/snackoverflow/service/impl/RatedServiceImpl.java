/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import tech.kaloyan.snackoverflow.entity.Rated;
import tech.kaloyan.snackoverflow.entity.User;
import tech.kaloyan.snackoverflow.exeception.BadRequestException;
import tech.kaloyan.snackoverflow.exeception.NotAuthorizedException;
import tech.kaloyan.snackoverflow.repository.QuestionRepository;
import tech.kaloyan.snackoverflow.repository.RatedRepository;
import tech.kaloyan.snackoverflow.resources.req.RatedReq;
import tech.kaloyan.snackoverflow.resources.resp.RatedResp;
import tech.kaloyan.snackoverflow.service.RatedService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static tech.kaloyan.snackoverflow.mapper.RatedMapper.MAPPER;

@Service
@RequiredArgsConstructor
public class RatedServiceImpl implements RatedService {
    private final RatedRepository ratedRepository;
    private final QuestionRepository questionRepository;

    @Override
    public List<RatedResp> getAll() {
        return MAPPER.toRatedResps(ratedRepository.findAll());
    }

    @Override
    public List<RatedResp> getAllByUserId(String userId) {
        return MAPPER.toRatedResps(ratedRepository.findAllByUserId(userId));
    }

    @Override
    public List<RatedResp> getAllByQuestionId(String questionId) {
        return MAPPER.toRatedResps(ratedRepository.findAllByQuestionId(questionId));
    }

    @Override
    public Optional<RatedResp> getById(String id) {
        return ratedRepository.findById(id).map(MAPPER::toRatedResp);
    }

    @Override
    public RatedResp save(RatedReq ratedReq, String questionId, User currentUser) {
        if (!Objects.equals(questionId, ratedReq.getQuestionId())) {
            throw new BadRequestException("Question id does not match");
        }

        if (ratedReq.getUserId() == null) {
            ratedReq.setUserId(currentUser.getId());
        } else if (!ratedReq.getUserId().equals(currentUser.getId())) {
            throw new NotAuthorizedException("User is not authorized to create rated for another user");
        }

        if (questionRepository.findById(ratedReq.getQuestionId()).isEmpty()) {
            throw new NotFoundException("Question with id " + ratedReq.getQuestionId() + " not found");
        }


        Optional<Rated> rated = ratedRepository.findByUserIdAndQuestionId(ratedReq.getUserId(), ratedReq.getQuestionId());
        if (rated.isPresent()) {
            rated.get().setRating(ratedReq.getRating());
            return MAPPER.toRatedResp(ratedRepository.save(rated.get()));
        }

        return MAPPER.toRatedResp(ratedRepository.save(MAPPER.toRated(ratedReq)));
    }

    @Override
    public void delete(String id, User currentUser) {
        if (
                ratedRepository.findById(id).isEmpty() ||
                        !ratedRepository.findById(id).get().getUser().getId().equals(currentUser.getId())
        ) {
            throw new NotAuthorizedException("User is not authorized to delete this rated");
        }

        ratedRepository.deleteById(id);
    }
}
