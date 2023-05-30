/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.kaloyan.snackoverflow.entity.Saved;

import java.util.List;

@Repository
public interface SavedRepository extends JpaRepository<Saved, String> {
    List<Saved> findAllByUserId(String userId);
    List<Saved> findAllByQuestionId(String questionId);
}
