package com.tchepannou.uds.controller;

import com.tchepannou.uds.dto.*;
import com.tchepannou.uds.exception.DuplicateLoginException;
import com.tchepannou.uds.exception.DuplicatePartyException;
import com.tchepannou.uds.service.UserService;
import com.tchepannou.uds.service.UserStatusCodeService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    @ApiOperation("Returns all the status codes")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
    })
    public UserStatusCodeListResponse statusCodes () {
        return userStatusCodeService.findAll();
    }


    @RequestMapping(method = RequestMethod.GET, value = "/{userId}")
    @ApiOperation("Returns a User")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "User not found", response = ErrorResponse.class)
    })
    public UserResponse findById(@PathVariable final long userId) {
        return userService.findById(userId);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation("Create")
    public ResponseEntity<UserResponse> create (@Valid @RequestBody final CreateUserRequest request) {
        final UserResponse response = userService.create(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{userId}/password")
    @ApiOperation("Update a password")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "User not found", response = ErrorResponse.class)
    })
    public UserResponse password (
            @PathVariable final long userId,
            @RequestParam(value = "password", required = true) @NotBlank final String password
    ) {
        return userService.updatePassword(userId, password);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{userId}/login")
    @ApiOperation("Update a login")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "User not found", response = ErrorResponse.class),
            @ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class),
            @ApiResponse(code = 409, message = "Duplicate login", response = ErrorResponse.class)
    })
    public UserResponse login (
            @PathVariable final long userId,
            @RequestParam(value = "login", required = true) @NotBlank final String login
    ) {
        return userService.updateLogin(userId, login);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{userId}")
    @ApiOperation("Delete")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "User not found", response = ErrorResponse.class)
    })
    public void delete (@PathVariable final long userId) {
        userService.delete(userId);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{userId}/status")
    @ApiOperation("Change user's status")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "User not found", response = ErrorResponse.class)
    })
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
