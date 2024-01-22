/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.repository;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tech.kaloyan.snackoverflow.entity.Question;

import java.util.List;

@Repository
 @Hidden
public interface QuestionRepository extends JpaRepository<Question, String> {
    @Override
    @Query ("SELECT q FROM Question q WHERE q.validFrom <= CURRENT_TIMESTAMP")
    List<Question> findAll();

    @Query ("SELECT q FROM Question q WHERE q.author.id = ?1 AND  q.validFrom <= CURRENT_TIMESTAMP")
    List<Question> findAllByAuthorId(String authorId);

}
