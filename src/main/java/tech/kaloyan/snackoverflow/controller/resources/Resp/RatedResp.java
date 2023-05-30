/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.controller.resources.Resp;

import lombok.Data;

@Data
public class RatedResp {
    private String id;
    private int rating;
    private String questionId;
    private String questionTitle;
    private String userId;
}
