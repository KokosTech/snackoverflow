/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.service;

import tech.kaloyan.snackoverflow.resources.req.UserLoginReq;
import tech.kaloyan.snackoverflow.resources.req.UserSignupReq;
import tech.kaloyan.snackoverflow.resources.resp.UserResp;

//TODO: Implement this interface in the next iteration
public interface AuthService {
    UserResp register(UserSignupReq userSignupReq);
    UserResp login(UserLoginReq userLoginReq);
    void logout();
}
