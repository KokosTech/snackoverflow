/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.resources.resp;

import lombok.Data;

import java.util.List;

@Data
public class AuthUserResp {
    private String id;
    private String username;
    private String email;
    private String lastLogin;
    private List<QuestionResp> questions;
    private List<SavedResp> saved;
    private List<CommentResp> comments;
    private List<ReplyResp> replies;
    private List<RatedResp> rated;
}
