/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.controller.resources.Req;

import lombok.Data;

@Data
public class RatedReq {
    private int rating;
    private String authorId;
    private String questionId;
}
