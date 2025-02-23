package com.harsh.auth.services;

import com.harsh.auth.entities.User;
import com.harsh.auth.dtos.requests.UserInfoDto;

public interface UserService {
    String getUserIdByUsername(String username);
    User checkIfUserExists(UserInfoDto userInfoDto);
    String signupUser(UserInfoDto userInfoDto);
}
