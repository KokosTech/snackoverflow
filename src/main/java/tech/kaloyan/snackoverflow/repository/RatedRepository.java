/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.repository;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.kaloyan.snackoverflow.entity.Rated;

import java.util.List;
import java.util.Optional;

@Repository
 @Hidden
public interface RatedRepository extends JpaRepository<Rated, String> {
    List<Rated> findAllByUserId(String userId);
    List<Rated> findAllByQuestionId(String questionId);
    Optional<Rated> findByUserIdAndQuestionId(String userId, String questionId);

}
