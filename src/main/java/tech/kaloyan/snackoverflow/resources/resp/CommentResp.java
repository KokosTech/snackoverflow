/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.resources.resp;

import lombok.Data;

@Data
public class CommentResp {
    private String id;
    private String content;
    private String createdOn;
    private UserResp author;
    private String questionId;
}
