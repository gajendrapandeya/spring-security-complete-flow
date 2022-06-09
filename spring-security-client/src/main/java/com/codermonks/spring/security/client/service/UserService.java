package com.codermonks.spring.security.client.service;

import com.codermonks.spring.security.client.entity.User;
import com.codermonks.spring.security.client.model.UserModel;

public interface UserService {
    User registerUser(UserModel model);

    void saveVerificationTokenForUser(String token, User user);
}
