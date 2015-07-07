package com.tchepannou.uds.service.impl;

import com.tchepannou.uds.domain.User;
import com.tchepannou.uds.service.PasswordEncryptor;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PasswordEncryptorImplTest {
    private final PasswordEncryptor service = new PasswordEncryptorImpl();

    @Test
    public void test_encrypt_null() throws Exception {
        assertThat(service.encrypt(null)).isNull();
    }

    @Test
    public void test_matches() throws Exception {
        User user = new User();
        user.setPassword(service.encrypt("secret"));

        assertThat(service.matches("secret", user)).isTrue();
    }

    @Test
    public void test_matches_null() throws Exception {
        User user = new User ();
        user.setPassword(service.encrypt("secret"));

        assertThat(service.matches(null, user)).isFalse();
    }


    @Test
    public void test_matches_all_null() throws Exception {
        assertThat(service.matches(null, new User())).isTrue();
    }
}
