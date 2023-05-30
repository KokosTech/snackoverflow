/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.resources.req;

import lombok.Data;

@Data
public class ReplyReq {
    private String text;
    private String authorId;
    private String commentId;
}
