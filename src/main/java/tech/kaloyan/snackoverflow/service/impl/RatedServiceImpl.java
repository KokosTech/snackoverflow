/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.kaloyan.snackoverflow.resources.req.RatedReq;
import tech.kaloyan.snackoverflow.resources.resp.RatedResp;
import tech.kaloyan.snackoverflow.entity.Rated;
import tech.kaloyan.snackoverflow.repository.RatedRepository;
import tech.kaloyan.snackoverflow.service.RatedService;

import java.util.List;
import java.util.Optional;

import static tech.kaloyan.snackoverflow.mapper.RatedMapper.MAPPER;

@Service
@RequiredArgsConstructor
public class RatedServiceImpl implements RatedService {
    private final RatedRepository ratedRepository;

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
    public Rated save(RatedReq ratedReq) {
        return ratedRepository.save(MAPPER.toRated(ratedReq));
    }

    @Override
    public Rated update(String id, RatedReq ratedReq) {
        Rated rated = ratedRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Rated with id " + id + " not found")
        );
        rated.setRating(ratedReq.getRating());
        return ratedRepository.save(rated);
    }

    @Override
    public void delete(String id) {
        ratedRepository.deleteById(id);
    }
}
