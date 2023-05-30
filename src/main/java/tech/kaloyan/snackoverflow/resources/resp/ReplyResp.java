/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.resources.resp;

import lombok.Data;

@Data
public class ReplyResp {
    private String id;
    private String text;
    private UserResp author;
    private String commentId;
}
