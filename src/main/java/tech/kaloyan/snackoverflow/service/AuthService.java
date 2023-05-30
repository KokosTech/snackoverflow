/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.service;

import tech.kaloyan.snackoverflow.resources.resp.UserResp;

//TODO: Implement this interface in the next iteration
public interface AuthService {
    UserResp register(String username, String password);
    UserResp login(String username, String password);
    void logout();
}
