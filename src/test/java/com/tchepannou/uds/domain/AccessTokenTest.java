package com.tchepannou.uds.domain;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class AccessTokenTest {

    @Test
    public void test_expire_success() throws Exception {
        // Given
        Date now = new Date ();
        AccessToken token = new AccessToken();

        // When
        boolean result = token.expire();

        // Then
        assertThat(result).isTrue();
        assertThat(token.isExpired()).isTrue();
        assertThat(token.getToDate()).isAfterOrEqualsTo(now);
    }

    @Test
    public void test_expire_failure () {
        // Given
        Date yesterday = DateUtils.addDays(new Date(), -1);
        AccessToken token = new AccessToken();
        token.setExpired(true);
        token.setToDate(yesterday);

        // When
        boolean result = token.expire();

        // Then
        assertThat(result).isFalse();
        assertThat(token.isExpired()).isTrue();
        assertThat(token.getToDate()).isEqualTo(yesterday);
    }

    @Test
    public void test_isExpired_false () {
        // Given
        AccessToken token = new AccessToken();

        // Then
        assertThat(token.isExpired()).isFalse();
    }

    @Test
    public void test_isExpired_Field () {
        // Given
        Date tomorrow = DateUtils.addDays(new Date(), 1);
        AccessToken token = new AccessToken();
        token.setExpired(true);
        token.setExpiryDate(tomorrow);

        // Then
        assertThat(token.isExpired()).isTrue();
    }

    @Test
    public void test_isExpired_byDate () {
        // Given
        Date yesterday = DateUtils.addDays(new Date(), -1);
        AccessToken token = new AccessToken();
        token.setExpired(false);
        token.setExpiryDate(yesterday);

        // Then
        assertThat(token.isExpired()).isTrue();
    }
}
