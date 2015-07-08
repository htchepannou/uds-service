package com.tchepannou.uds.service;

import com.tchepannou.uds.dto.AuthRequest;
import com.tchepannou.uds.dto.AuthResponse;

public interface AuthenticationService {
    AuthResponse findById(long id);

    AuthResponse login(AuthRequest request);

    void logout(long authId);
}
