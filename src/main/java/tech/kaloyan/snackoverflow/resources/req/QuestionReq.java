/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.resources.req;

import lombok.Data;

import java.util.List;

@Data
public class QuestionReq {
    String title;
    String description;
    String authorId;
    List<ImageReq> images;
}
