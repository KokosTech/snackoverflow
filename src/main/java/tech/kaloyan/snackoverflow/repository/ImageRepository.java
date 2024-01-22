/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.repository;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.kaloyan.snackoverflow.entity.Image;

import java.util.List;

@Repository
 @Hidden
public interface ImageRepository extends JpaRepository<Image, String> {
    List<Image> findAllByQuestionId(String questionId);
}
