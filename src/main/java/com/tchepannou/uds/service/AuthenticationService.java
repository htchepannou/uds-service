package com.tchepannou.uds.service;

import com.tchepannou.uds.dto.LoginRequest;
import com.tchepannou.uds.dto.AccessTokenResponse;

public interface AuthenticationService {
    AccessTokenResponse findById(long id);

    AccessTokenResponse login(LoginRequest request);

    void logout(long authId);
}
