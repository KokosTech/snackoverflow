/*
 * Copyright (c) 2024. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.resources.update;

import lombok.Data;
import tech.kaloyan.snackoverflow.resources.req.ImageReq;

import java.util.Date;
import java.util.List;

@Data
public class QuestionUpd {
    String title;
    String description;
    Date validFrom;
}
