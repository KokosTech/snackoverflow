/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.controller.resources.Resp;

import lombok.Data;

import java.util.List;

@Data
public class UserResp {
    private String id;
    private String username;
    private List<QuestionResp> questions;
}
