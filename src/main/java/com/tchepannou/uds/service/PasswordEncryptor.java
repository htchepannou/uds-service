package com.tchepannou.uds.service;

import com.tchepannou.uds.domain.User;

public interface PasswordEncryptor {
    String encrypt(String password);
    boolean matches(String password, User user);
}
