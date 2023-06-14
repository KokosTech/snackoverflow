/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.resources.resp;

import lombok.Data;

import java.util.Calendar;

@Data
public class CommentResp {
    private String id;
    private String content;
    private Calendar createdOn;
    private UserResp author;
    private String questionId;
}
