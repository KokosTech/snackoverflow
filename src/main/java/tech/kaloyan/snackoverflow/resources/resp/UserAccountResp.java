/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.resources.resp;

import lombok.Data;

import java.util.List;

@Data
public class UserAccountResp {
    private String id;
    private String username;
    private List<QuestionResp> questions;
}
