/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.resources.req;

import lombok.Data;

@Data
public class SavedReq {
    private String userId;
    private String questionId;
}
