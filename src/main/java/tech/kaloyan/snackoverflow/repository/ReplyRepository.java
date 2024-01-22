/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.repository;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.kaloyan.snackoverflow.entity.Reply;

import java.util.List;

@Repository
 @Hidden
public interface ReplyRepository extends JpaRepository<Reply, String> {
    List<Reply> findAllByAuthorId(String authorId);
    List<Reply> findAllByCommentId(String commentId);
}
