/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.service;

import tech.kaloyan.snackoverflow.entity.User;
import tech.kaloyan.snackoverflow.resources.req.UserLoginReq;
import tech.kaloyan.snackoverflow.resources.req.UserSignupReq;
import tech.kaloyan.snackoverflow.resources.resp.AuthResp;

//TODO: Implement this interface in the next iteration
public interface AuthService {
    AuthResp register(UserSignupReq userSignupReq);
    AuthResp login(UserLoginReq userLoginReq);
    void logout();
    User getUser();
}
