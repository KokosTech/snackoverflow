/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.controller.resources.Resp;

import lombok.Data;

import java.util.Calendar;
import java.util.Map;

@Data
public class QuestionResp {
    private String id;
    private String title;
    private String description;
    private Calendar createdOn;
    private Map<String, String> author; // id, username
    private Map<String, String> image; // url, alt
    private Double rating;
}
