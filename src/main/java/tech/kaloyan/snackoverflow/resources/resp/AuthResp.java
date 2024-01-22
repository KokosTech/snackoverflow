/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.resources.resp;

import lombok.Data;

@Data
public class AuthResp {
    private String id;
    private String username;
    private String email;
    private String jwt;
}
