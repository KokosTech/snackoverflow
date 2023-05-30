/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.controller.resources.Req;

import lombok.Data;

import java.util.Map;

@Data
public class QuestionReq {
    String title;
    String description;
    String authorId;
    Map<String, String> images; // url and alt
}
