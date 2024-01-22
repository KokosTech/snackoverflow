/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.kaloyan.snackoverflow.entity.Saved;
import tech.kaloyan.snackoverflow.entity.User;
import tech.kaloyan.snackoverflow.exeception.ConflictException;
import tech.kaloyan.snackoverflow.exeception.NotAuthorizedException;
import tech.kaloyan.snackoverflow.repository.SavedRepository;
import tech.kaloyan.snackoverflow.resources.req.SavedReq;
import tech.kaloyan.snackoverflow.resources.resp.SavedResp;
import tech.kaloyan.snackoverflow.service.SavedService;

import java.util.*;

import static tech.kaloyan.snackoverflow.mapper.SavedMapper.MAPPER;

@Service
@RequiredArgsConstructor
public class SavedServiceImpl implements SavedService {
    private final SavedRepository savedRepository;

    @Override
    public List<SavedResp> getAll() {
        return MAPPER.toSavedResps(savedRepository.findAll());
    }

    @Override
    public List<SavedResp> getAllByUserId(String userId) {
        return MAPPER.toSavedResps(savedRepository.findAllByUserId(userId));
    }

    @Override
    public List<SavedResp> getAllByQuestionId(String questionId) {
        return MAPPER.toSavedResps(savedRepository.findAllByQuestionId(questionId));
    }

    @Override
    public Optional<SavedResp> getById(String id) {
        return savedRepository.findById(id).map(MAPPER::toSavedResp);
    }

    @Override
    public SavedResp save(String id, SavedReq savedReq, User currentUser) {
        if (savedReq.getUserId() == null) {
            savedReq.setUserId(currentUser.getId());
        } else if (!savedReq.getUserId().equals(currentUser.getId())) {
            throw new NotAuthorizedException("User is not authorized to create saved for another user");
        }

        if (!Objects.equals(id, savedReq.getQuestionId())) {
            throw new ConflictException("Question id in the path and in the body do not match");
        }

        Optional<Saved> checkSaved = savedRepository.findByUserIdAndQuestionId(savedReq.getUserId(), savedReq.getQuestionId());
        checkSaved.ifPresent(saved -> {
            this.delete(saved.getId(), currentUser);
            throw new ConflictException("Deleted saved");
        });


        Saved saved = MAPPER.toSaved(savedReq);
        saved.setSavedOn(new Calendar.Builder().setInstant(new Date()).build());

        Saved savedFromDb = savedRepository.save(saved);
        System.out.println(savedFromDb);

        return MAPPER.toSavedResp(savedFromDb);
    }

    @Override
    public void delete(String id, User currentUser) {
        Optional<Saved> saved = savedRepository.findById(id);
        if (saved.isEmpty()) {
            throw new NotAuthorizedException("User is not authorized to delete saved for another user");
        } else if (!saved.get().getUser().getId().equals(currentUser.getId())) {
            throw new NotAuthorizedException("User is not authorized to delete saved for another user");
        }

        savedRepository.deleteById(id);
    }
}
