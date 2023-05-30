/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.resources.resp;

import lombok.Data;

import java.util.Calendar;
import java.util.List;

@Data
public class QuestionResp {
    private String id;
    private String title;
    private String description;
    private Calendar createdOn;
    private UserResp author;
    private List<ImageResp> images;
    private Double rating;
}
