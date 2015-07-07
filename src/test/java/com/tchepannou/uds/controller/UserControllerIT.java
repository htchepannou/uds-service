package com.tchepannou.uds.controller;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.internal.mapper.ObjectMapperType;
import com.tchepannou.uds.Starter;
import com.tchepannou.uds.dao.UserDao;
import com.tchepannou.uds.domain.User;
import com.tchepannou.uds.dto.CreateUserRequest;
import com.tchepannou.uds.dto.UserStatusRequest;
import com.tchepannou.uds.service.PasswordEncryptor;
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

import java.util.UUID;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Starter.class)
@WebIntegrationTest
@Sql({
        "/db/data/clean.sql",
        "/db/data/user.sql"
})
public class UserControllerIT {
    @Value("${server.port}")
    int port;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncryptor passwordEncryptor;

    @Before
    public void setUp (){
        RestAssured.port = port;
    }

    @Test
    public void test_statusCodes (){

        // @formatter:off
        when()
            .get("/api/users/status-codes")
        .then()
            .statusCode(HttpStatus.SC_OK)
            .log()
                .all()
            .body("userStatusCodes.id", hasItems(1, 2, 3))
            .body("userStatusCodes.name", hasItems("new", "active", "suspended"))
            .body("userStatusCodes.active", hasItems(false, true, true))
            .body("userStatusCodes.defaultStatus", hasItems(true, false, true))
        ;
        // @formatter:on
    }

    @Test
    public void test_findById (){

        // @formatter:off
        when()
            .get("/api/users/101")
        .then()
            .statusCode(HttpStatus.SC_OK)
            .log()
                .all()
            .body("id", is(101))
            .body("partyId", is(100))
            .body("login", is("ray.sponsible"))
            .body("password", nullValue())
            .body("fromDate", is("1973-12-27 10:30:45 -0500"))
            .body("toDate", nullValue())
            .body("status.id", is(101))
            .body("status.statusCode", is(1))
            .body("status.statusText", is("new"))
            .body("status.comment", is("Initial"))
            .body("status.date", is("1973-12-27 10:30:45 -0500"))
        ;
        // @formatter:on
    }


    @Test
    public void test_findById_deleted (){

        // @formatter:off
        when()
            .get("/api/users/301")
        .then()
            .statusCode(HttpStatus.SC_NOT_FOUND)
            .log()
                .all()
        ;
        // @formatter:on
    }

    @Test
    public void test_findById_badId (){
        // @formatter:off
        when()
            .get("/api/users/99999")
        .then()
            .statusCode(HttpStatus.SC_NOT_FOUND)
            .log()
                .all()
        ;
        // @formatter:on
    }

    @Test
    public void test_create () throws Exception {
        CreateUserRequest request = new CreateUserRequest();
        request.setPartyId(401);
        request.setLogin("john.smith");
        request.setPassword("__secret__");

        // @formatter:off
        int userId = given ()
                .contentType(ContentType.JSON)
                .content(request, ObjectMapperType.JACKSON_2)
        .when()
            .post("/api/users")
        .then()
            .statusCode(HttpStatus.SC_OK)
            .log()
                .all()
            .body("id", greaterThan(1))
            .body("partyId", is(401))
            .body("login", is("john.smith"))
            .body("password", nullValue())
            .body("fromDate", notNullValue())
            .body("status.id", greaterThan(101))
            .body("status.statusCode", is(1))
            .body("status.statusText", is("new"))
            .body("status.date", notNullValue())
        .extract()
            .path("id")
        ;
        // @formatter:on

        User user = userDao.findById(userId);
        assertThat(user).isNotNull();
        assertThat(passwordEncryptor.matches("__secret__", user)).isTrue();
    }

    @Test
    public void test_create_duplicateParty () throws Exception {
        CreateUserRequest request = new CreateUserRequest();
        request.setPartyId(400);
        request.setLogin("john400.smith");
        request.setPassword("__secret__");

        // @formatter:off
        given ()
                .contentType(ContentType.JSON)
                .content(request, ObjectMapperType.JACKSON_2)
        .when()
            .post("/api/users")
        .then()
            .statusCode(HttpStatus.SC_CONFLICT)
            .log()
                .all()
            .body("message", is("duplicate_party"))
        ;
        // @formatter:on
    }

    @Test
    public void test_create_duplicateLogin () throws Exception {
        CreateUserRequest request = new CreateUserRequest();
        request.setPartyId(510);
        request.setLogin("ray.sponsible");
        request.setPassword("__secret__");

        // @formatter:off
        given ()
                .contentType(ContentType.JSON)
                .content(request, ObjectMapperType.JACKSON_2)
        .when()
            .post("/api/users")
        .then()
            .statusCode(HttpStatus.SC_CONFLICT)
            .log()
                .all()
            .body("message", is("duplicate_login"))
        ;
        // @formatter:on
    }

    @Test
    public void test_updateLogin () throws Exception {
        final String login = "ray" + UUID.randomUUID().toString();

        // @formatter:off
        given ()
                .param("login", login)
        .when()
            .post("/api/users/600/login")
        .then()
            .statusCode(HttpStatus.SC_OK)
            .log()
                .all()
            .body("id", is(600))
            .body("partyId", is(600))
            .body("login", is(login))
            .body("password", nullValue())
            .body("fromDate", is("1973-12-27 10:30:45 -0500"))
            .body("toDate", nullValue())
            .body("status.statusCode", is(2))
            .body("status.statusText", is("active"))
            .body("status.comment", is("Activated"))
            .body("status.date", is("1973-12-27 10:30:45 -0500"))
        ;
        // @formatter:on
    }

    @Test
    public void test_updateLogin_badId () throws Exception {
        final String login = "ray" + UUID.randomUUID().toString();

        // @formatter:off
        given ()
                .param("login", login)
        .when()
            .post("/api/users/99999/login")
        .then()
            .statusCode(HttpStatus.SC_NOT_FOUND)
            .log()
                .all()
        ;
        // @formatter:on
    }

    @Test
    public void test_updateLogin_duplicateLogin () throws Exception {
        // @formatter:off
        given ()
                .param("login", "ray.sponsible")
        .when()
            .post("/api/users/600/login")
        .then()
            .statusCode(HttpStatus.SC_CONFLICT)
            .log()
                .all()
            .body("message", is("duplicate_login"))
        ;
        // @formatter:on
    }

    @Test
    public void test_updatePassword () throws Exception {
        final String password = "??secret???";

        // @formatter:off
        given ()
                .param("password", password)
        .when()
            .post("/api/users/700/password")
        .then()
            .statusCode(HttpStatus.SC_OK)
            .log()
                .all()
            .body("id", is(700))
            .body("partyId", is(700))
            .body("login", is("ray700.sponsible"))
            .body("password", nullValue())
            .body("toDate", nullValue())
        ;
        // @formatter:on

        User user = userDao.findById(700);
        assertThat(user).isNotNull();
        assertThat(passwordEncryptor.matches(password, user)).isTrue();
    }

    @Test
    public void test_updatePassword_badId () throws Exception {
        final String password = "??secret???";

        // @formatter:off
        given ()
                .param("password", password)
        .when()
            .post("/api/users/9999/password")
        .then()
            .statusCode(HttpStatus.SC_NOT_FOUND)
            .log()
                .all()
        ;
        // @formatter:on
    }

    @Test
    public void test_delete () throws Exception {
        // @formatter:off
        when()
            .delete("/api/users/1000")
        .then()
                .statusCode(HttpStatus.SC_OK)
                .log()
            .all()
        ;
        // @formatter:on

        User user = userDao.findById(1000);
        assertThat(user.isDeleted()).isTrue();
        assertThat(user.getFromDate()).isNotNull();
        assertThat(user.getToDate()).isNotNull();
        assertThat(user.getLogin()).matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
    }

    @Test
    public void test_delete_badId () throws Exception {
        // @formatter:off
        when()
            .delete("/api/users/9999")
        .then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .log()
            .all()
        ;
        // @formatter:on
    }

    @Test
    public void test_setStatus() throws Exception {
        UserStatusRequest request = new UserStatusRequest();
        request.setStatusCode(2);
        request.setComment("Hello world");

        // @formatter:off
        given ()
                .contentType(ContentType.JSON)
                .content(request, ObjectMapperType.JACKSON_2)
        .when()
            .post("/api/users/1100/status")
        .then()
            .statusCode(HttpStatus.SC_OK)
            .log()
                .all()
            .body("id", is(1100))
            .body("partyId", is(1100))
            .body("login", is("ray1100.sponsible"))
            .body("password", nullValue())
            .body("fromDate", is("1973-12-27 10:30:45 -0500"))
            .body("status.id", greaterThan(101))
            .body("status.statusCode", is(2))
            .body("status.statusText", is("active"))
            .body("status.date", notNullValue())
            .body("status.comment", is("Hello world"))
        ;
        // @formatter:on
    }

    @Test
    public void test_setStatus_badUserId() throws Exception {
        UserStatusRequest request = new UserStatusRequest();
        request.setStatusCode(2);
        request.setComment("Hello world");

        // @formatter:off
        given ()
                .contentType(ContentType.JSON)
                .content(request, ObjectMapperType.JACKSON_2)
        .when()
            .post("/api/users/99999/status")
        .then()
            .statusCode(HttpStatus.SC_NOT_FOUND)
            .log()
                .all()
        ;
        // @formatter:on
    }


    @Test
    public void test_setStatus_badStatusCode() throws Exception {
        UserStatusRequest request = new UserStatusRequest();
        request.setStatusCode(999999);
        request.setComment("Hello world");

        // @formatter:off
        given ()
                .contentType(ContentType.JSON)
                .content(request, ObjectMapperType.JACKSON_2)
        .when()
            .post("/api/users/1100/status")
        .then()
            .statusCode(HttpStatus.SC_BAD_REQUEST)
            .log()
                .all()
            .body("message", is("statusCode"))
        ;
        // @formatter:on
    }
}
