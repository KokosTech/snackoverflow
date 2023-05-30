/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.controller.resources.Resp;

import lombok.Data;

import java.util.Calendar;
import java.util.Map;

@Data
public class CommentResp {
    private String id;
    private String content;
    private String createdOn;
    private Map<String, String> author; // id, username
    private String questionId;
}
