/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.resources.req;

import lombok.Data;

@Data
public class CommentReq {
    String content;
    String authorId;
    String questionId;
}
