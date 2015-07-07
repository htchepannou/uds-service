package com.tchepannou.uds.controller;

import com.tchepannou.uds.dto.*;
import com.tchepannou.uds.exception.DuplicatePartyException;
import com.tchepannou.uds.exception.DuplicateLoginException;
import com.tchepannou.uds.service.UserService;
import com.tchepannou.uds.service.UserStatusCodeService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Api(basePath = "/users", value = "User's Account", produces = MediaType.APPLICATION_JSON_VALUE)
@RequestMapping(value="/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController extends AbstractController {
    //-- Attributes
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserStatusCodeService userStatusCodeService;

    //-- AbstractController overrides
    @Override
    protected Logger getLogger() {
        return LOG;
    }

    //-- REST methods
    @RequestMapping(method = RequestMethod.GET, value = "/status-codes")
    @ApiOperation("List all users' status codes")
    public UserStatusCodeListResponse statusCodes () {
        return userStatusCodeService.findAll();
    }


    @RequestMapping(method = RequestMethod.GET, value = "/{userId}")
    @ApiOperation("Returns a User")
    public UserResponse findById(@PathVariable final long userId) {
        return userService.findById(userId);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation("Create a new user account")
    public UserResponse create (@Valid @RequestBody final CreateUserRequest request) {
        return userService.create(request);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{userId}/password")
    @ApiOperation("Update a user's password")
    public UserResponse password (
            @PathVariable final long userId,
            @RequestParam(value = "password", required = true) @NotBlank final String password
    ) {
        return userService.updatePassword(userId, password);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{userId}/login")
    @ApiOperation("Update a user's login")
    public UserResponse login (
            @PathVariable final long userId,
            @RequestParam(value = "login", required = true) @NotBlank final String login
    ) {
        return userService.updateLogin(userId, login);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{userId}")
    @ApiOperation("Delete a user account")
    public void delete (@PathVariable final long userId) {
        userService.delete(userId);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{userId}/status")
    @ApiOperation("Update the user's status")
    public UserResponse setStatus (
            @PathVariable final long userId,
            @Valid @RequestBody final UserStatusRequest request
    ) {
        return userService.setStatus(userId, request);
    }

    //-- Error Handler
    @ExceptionHandler(DuplicateLoginException.class)
    @ResponseStatus(value= HttpStatus.CONFLICT)
    public ErrorResponse handleError(
            final HttpServletRequest request,
            final DuplicateLoginException exception
    ) {
        return handleException(request, HttpStatus.CONFLICT, "duplicate_login", exception);
    }

    @ExceptionHandler(DuplicatePartyException.class)
    @ResponseStatus(value= HttpStatus.CONFLICT)
    public ErrorResponse handleError(
            final HttpServletRequest request,
            final DuplicatePartyException exception
    ) {
        return handleException(request, HttpStatus.CONFLICT, "duplicate_party", exception);
    }

}
