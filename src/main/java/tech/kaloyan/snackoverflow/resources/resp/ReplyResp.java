/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.resources.resp;

import lombok.Data;

import java.util.Date;

@Data
public class ReplyResp {
    private String id;
    private String text;
    private UserResp author;
    private String commentId;
    private Date createdOn;
}
