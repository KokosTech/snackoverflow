/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.controller.resources.Req;

import lombok.Data;

@Data
public class SavedReq {
    private String userId;
    private String questionId;
}
