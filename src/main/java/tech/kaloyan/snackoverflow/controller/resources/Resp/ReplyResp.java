/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.controller.resources.Resp;

import lombok.Data;

import java.util.Map;

@Data
public class ReplyResp {
    private String id;
    private String text;
    private Map<String, String> author; // id, username
    private String commentId;
}
