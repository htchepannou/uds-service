package com.tchepannou.uds.controller;

import com.tchepannou.uds.dto.AccessTokenResponse;
import com.tchepannou.uds.dto.ErrorResponse;
import com.tchepannou.uds.dto.LoginRequest;
import com.tchepannou.uds.exception.AccessDeniedException;
import com.tchepannou.uds.exception.AccessTokenExpiredException;
import com.tchepannou.uds.exception.AccountNotActiveException;
import com.tchepannou.uds.exception.AuthFailedException;
import com.tchepannou.uds.service.AuthenticationService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Api(basePath = "/auth", value = "Authentication", produces = MediaType.APPLICATION_JSON_VALUE)
@RequestMapping(value="/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController extends AbstractController {
    //-- Attributes
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private AuthenticationService authService;

    //-- AbstractController overrides
    @Override
    protected Logger getLogger() {
        return LOG;
    }

    //-- REST methods
    @RequestMapping(method = RequestMethod.GET, value="/{authId}")
    @ApiOperation("Returns an access token")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = AccessTokenResponse.class),
            @ApiResponse(code = 404, message = "Access token not found or expired", response = ErrorResponse.class)
    })
    public AccessTokenResponse findById (@PathVariable final long authId) {
        return authService.findById(authId);
    }


    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation("Login")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = AccessTokenResponse.class),
            @ApiResponse(code = 400, message = "Invalid request", response = ErrorResponse.class),
            @ApiResponse(code = 409, message = "Authentication failed", response = ErrorResponse.class)
    })
    public AccessTokenResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @RequestMapping(method = RequestMethod.DELETE, value="/{authId}")
    @ApiOperation("Logout")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "Access token not found or expired")
    })
    public void logout(@PathVariable final long authId) {
        authService.logout(authId);
    }

    //-- Exception handlers
    @ExceptionHandler(AccessTokenExpiredException.class)
    @ResponseStatus(value= HttpStatus.NOT_FOUND)
    public ErrorResponse handleError(
            final HttpServletRequest request,
            final AccessTokenExpiredException exception
    ) {
        return handleException(request, HttpStatus.NOT_FOUND, "expired", exception);
    }

    @ExceptionHandler(AuthFailedException.class)
    @ResponseStatus(value= HttpStatus.CONFLICT)
    public ErrorResponse handleError(
            final HttpServletRequest request,
            final AuthFailedException exception
    ) {
        return handleException(request, HttpStatus.CONFLICT, "auth_failed", exception);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(value= HttpStatus.CONFLICT)
    public ErrorResponse handleError(
            final HttpServletRequest request,
            final AccessDeniedException exception
    ) {
        return handleException(request, HttpStatus.CONFLICT, "access_denied", exception);
    }

    @ExceptionHandler(AccountNotActiveException.class)
    @ResponseStatus(value= HttpStatus.CONFLICT)
    public ErrorResponse handleError(
            final HttpServletRequest request,
            final AccountNotActiveException exception
    ) {
        return handleException(request, HttpStatus.CONFLICT, "inactive", exception);
    }

}
