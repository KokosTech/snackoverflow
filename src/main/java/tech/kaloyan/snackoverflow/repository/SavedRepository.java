/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.repository;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.kaloyan.snackoverflow.entity.Saved;

import java.util.List;
import java.util.Optional;

@Repository
 @Hidden
public interface SavedRepository extends JpaRepository<Saved, String> {
    List<Saved> findAllByUserId(String userId);
    List<Saved> findAllByQuestionId(String questionId);
    Optional<Saved> findByUserIdAndQuestionId(String userId, String questionId);
}
