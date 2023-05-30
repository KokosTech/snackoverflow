/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.resources.req;

import lombok.Data;

@Data
public class UserLoginReq {
    private String email;
    private String password;
}
