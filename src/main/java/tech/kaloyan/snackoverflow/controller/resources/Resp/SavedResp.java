/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.controller.resources.Resp;

import lombok.Data;

@Data
public class SavedResp {
    private String id;
    private String questionId;
    private String questionTitle;
    private String authorId;
    private String savedOn;
    private String userId;
}
