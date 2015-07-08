package com.tchepannou.uds.controller;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.internal.mapper.ObjectMapperType;
import com.tchepannou.uds.Starter;
import com.tchepannou.uds.dao.AccessTokenDao;
import com.tchepannou.uds.domain.AccessToken;
import com.tchepannou.uds.dto.LoginRequest;
import org.apache.commons.lang.time.DateUtils;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Starter.class)
@WebIntegrationTest
@Sql({
        "/db/data/clean.sql",
        "/db/data/authentication.sql"
})
public class AuthenticationControllerIT {
    @Value("${server.port}")
    int port;

    @Value ("${access_token.ttl.minutes:30}")
    private int ttl;

    @Autowired
    private AccessTokenDao accessTokenDao;


    @Before
    public void setUp (){
        RestAssured.port = port;
    }

    @Test
    public void test_findById (){
        // @formatter:off
        when()
            .get("/api/auth/100")
        .then()
            .statusCode(HttpStatus.SC_OK)
            .log()
                .all()
            .body("id", is(100))
            .body("domainId", is(100))
            .body("userId", is(100))
            .body("expired", is(false))
            .body("fromDate", is("1973-12-27 10:30:45 -0500"))
            .body("expiryDate", is("2020-12-27 10:30:45 -0500"))
        ;
        // @formatter:on
    }

    @Test
    public void test_findById_badId (){
        // @formatter:off
        when()
            .get("/api/auth/99999")
        .then()
            .statusCode(HttpStatus.SC_NOT_FOUND)
            .log()
                .all()
        ;
        // @formatter:on
    }

    @Test
    public void test_findById_expired (){
        // @formatter:off
        when()
            .get("/api/auth/200")
        .then()
            .statusCode(HttpStatus.SC_NOT_FOUND)
            .log()
                .all()
            .body("message", is("expired"))
        ;
        // @formatter:on
    }


    @Test
    public void test_logout (){
        Date now = new Date ();

        // @formatter:off
        when()
            .delete("/api/auth/300")
        .then()
            .statusCode(HttpStatus.SC_OK)
            .log()
                .all()
        ;
        // @formatter:on

        AccessToken token = accessTokenDao.findById(300);
        assertThat(token.isExpired()).isTrue();
        assertThat(token.getToDate()).isInSameMinuteWindowAs(now);
    }

    @Test
    public void test_logout_expired (){
        // @formatter:off
        when()
            .delete("/api/auth/400")
        .then()
            .statusCode(HttpStatus.SC_NOT_FOUND)
            .log()
                .all()
            .body("message", is("expired"))
        ;
        // @formatter:on
    }

    @Test
    public void test_logout_badId (){
        // @formatter:off
        when()
            .delete("/api/auth/999")
        .then()
            .statusCode(HttpStatus.SC_NOT_FOUND)
            .log()
                .all()
        ;
        // @formatter:on
    }


    @Test
    public void test_login (){
        Date now = new Date ();
        LoginRequest req = createAuthRequest(100, "ray.sponsible", "secret");

        // @formatter:off
        int id = given()
                .contentType(ContentType.JSON)
                .content(req, ObjectMapperType.JACKSON_2)
        .when()
            .post("/api/auth/")
        .then()
            .statusCode(HttpStatus.SC_OK)
            .log()
                .all()
            .body("userId", is(100))
            .body("domainId", is(100))
            .body("expired", is(false))
            .body("fromDate", notNullValue())
            .body("expiryDate", notNullValue())
            .body("toDate", nullValue())
            .extract()
                .path("id")
        ;
        // @formatter:on

        AccessToken token = accessTokenDao.findById(id);
        assertThat(token.getFromDate()).isInSameMinuteWindowAs(now);
        assertThat(token.getExpiryDate()).isInSameMinuteWindowAs(DateUtils.addMinutes(now, ttl));
    }

    @Test
    public void test_login_deletedUser (){
        LoginRequest req = createAuthRequest(100, "ray500.sponsible", "secret");

        // @formatter:off
        given()
                .contentType(ContentType.JSON)
                .content(req, ObjectMapperType.JACKSON_2)
        .when()
            .post("/api/auth/")
        .then()
            .statusCode(HttpStatus.SC_CONFLICT)
            .log()
                .all()
            .body("message", is("auth_failed"))
        ;
        // @formatter:on
    }

    @Test
    public void test_login_badLogin (){
        LoginRequest req = createAuthRequest(100, "???", "secret");

        // @formatter:off
        given()
                .contentType(ContentType.JSON)
                .content(req, ObjectMapperType.JACKSON_2)
        .when()
            .post("/api/auth/")
        .then()
            .statusCode(HttpStatus.SC_CONFLICT)
            .log()
                .all()
            .body("message", is("auth_failed"))
        ;
        // @formatter:on
    }

    @Test
    public void test_login_noLogin (){
        LoginRequest req = createAuthRequest(100, null, "secret");

        // @formatter:off
        given()
                .contentType(ContentType.JSON)
                .content(req, ObjectMapperType.JACKSON_2)
        .when()
            .post("/api/auth/")
        .then()
            .statusCode(HttpStatus.SC_BAD_REQUEST)
            .log()
                .all()
            .body("message", is("login"))
        ;
        // @formatter:on
    }

    @Test
    public void test_login_badPassword (){
        LoginRequest req = createAuthRequest(100, "ray.sponsible", "se??cret");

        // @formatter:off
        given()
                .contentType(ContentType.JSON)
                .content(req, ObjectMapperType.JACKSON_2)
        .when()
            .post("/api/auth/")
        .then()
            .statusCode(HttpStatus.SC_CONFLICT)
            .log()
                .all()
            .body("message", is("auth_failed"))
        ;
        // @formatter:on
    }

    @Test
    public void test_login_noPassword (){
        LoginRequest req = createAuthRequest(100, "ray.sponsible", null);

        // @formatter:off
        given()
                .contentType(ContentType.JSON)
                .content(req, ObjectMapperType.JACKSON_2)
        .when()
            .post("/api/auth/")
        .then()
            .statusCode(HttpStatus.SC_BAD_REQUEST)
            .log()
                .all()
            .body("message", is("password"))
        ;
        // @formatter:on
    }

    @Test
    public void test_login_accessDenied (){
        LoginRequest req = createAuthRequest(100, "ray600.sponsible", "secret");

        // @formatter:off
        given()
                .contentType(ContentType.JSON)
                .content(req, ObjectMapperType.JACKSON_2)
        .when()
            .post("/api/auth/")
        .then()
            .statusCode(HttpStatus.SC_CONFLICT)
            .log()
                .all()
            .body("message", is("access_denied"))
        ;
        // @formatter:on
    }

    @Test
    public void test_login_noDomain (){
        LoginRequest req = createAuthRequest(0, "ray600.sponsible", "secret");

        // @formatter:off
        given()
                .contentType(ContentType.JSON)
                .content(req, ObjectMapperType.JACKSON_2)
        .when()
            .post("/api/auth/")
        .then()
            .statusCode(HttpStatus.SC_BAD_REQUEST)
            .log()
                .all()
            .body("message", is("domainId"))
        ;
        // @formatter:on
    }


    @Test
    public void test_login_notActive (){
        LoginRequest req = createAuthRequest(100, "ray700.sponsible", "secret");

        // @formatter:off
        given()
                .contentType(ContentType.JSON)
                .content(req, ObjectMapperType.JACKSON_2)
        .when()
            .post("/api/auth/")
        .then()
            .statusCode(HttpStatus.SC_CONFLICT)
            .log()
                .all()
            .body("message", is("inactive"))
        ;
        // @formatter:on
    }

    private LoginRequest createAuthRequest (long domainId, String login, String password){
        LoginRequest req = new LoginRequest();
        req.setDomainId(domainId);
        req.setRemoteIp("10.1.1.1");
        req.setUserAgent("user-agent");
        req.setLogin(login);
        req.setPassword(password);
        return req;
    }
}
