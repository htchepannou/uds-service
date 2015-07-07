package com.tchepannou.uds.service;

import com.tchepannou.uds.dto.CreateUserRequest;
import com.tchepannou.uds.dto.UserResponse;
import com.tchepannou.uds.dto.UserStatusRequest;

public interface UserService {
    UserResponse findById(long id);

    UserResponse findByLogin(String login);

    UserResponse create(CreateUserRequest request);

    UserResponse updatePassword(long id, String password);

    UserResponse updateLogin(long id, String login);

    UserResponse setStatus(long id, UserStatusRequest request);

    void delete(long id);
}
