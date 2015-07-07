package com.tchepannou.uds.controller;

import com.tchepannou.uds.domain.UserStatusCode;
import com.tchepannou.uds.dto.UserStatusCodeListResponse;
import com.tchepannou.uds.service.UserStatusCodeService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(basePath = "/users", value = "User's Account", produces = MediaType.APPLICATION_JSON_VALUE)
@RequestMapping(value="/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController extends AbstractController {
    //-- Attributes
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

//    @Autowired
//    private UserService userService;

    @Autowired
    private UserStatusCodeService userStatusCodeService;

//    @Autowired
//    private UserStatusService userStatusService;

    //-- AbstractController overrides
    @Override
    protected Logger getLogger() {
        return LOG;
    }

    //-- REST methods
    @RequestMapping(method = RequestMethod.GET, value = "/status-codes")
    @ApiOperation("List all users' status codes")
    public UserStatusCodeListResponse statusCodes () {
        final List<UserStatusCode> statuses = userStatusCodeService.findAll();
        return new UserStatusCodeListResponse.Builder()
                .withUserStatusCodes(statuses)
                .build();
    }


//    @RequestMapping(method = RequestMethod.GET, value = "/{userId}")
//    @ApiOperation("Returns a User")
//    public UserResponse findById(@PathVariable final long userId) {
//        final User user = userService.findById(userId);
//        return toUserResponse(user);
//    }
//
//    @RequestMapping(method = RequestMethod.POST)
//    @ApiOperation("Create a new user account")
//    public UserResponse create (@Valid @RequestBody final CreateUserRequest request) {
//        final User user = userService.create(request);
//        return toUserResponse(user);
//    }
//
//    @RequestMapping(method = RequestMethod.POST, value = "/{userId}/password")
//    @ApiOperation("Update a user's password")
//    public UserResponse password (
//            @PathVariable final long userId,
//            @RequestParam(value = "password", required = true) @NotBlank final String password
//    ) {
//        User user = userService.updatePassword(userId, password);
//        return toUserResponse(user);
//    }
//
//    @RequestMapping(method = RequestMethod.POST, value = "/{userId}/login")
//    @ApiOperation("Update a user's login")
//    public UserResponse login (
//            @PathVariable final long userId,
//            @RequestParam(value = "login", required = true) @NotBlank final String login
//    ) {
//        User user = userService.updateLogin(userId, login);
//        return toUserResponse(user);
//    }
//
//    @RequestMapping(method = RequestMethod.DELETE, value = "/{userId}")
//    @ApiOperation("Delete a user account")
//    public void delete (@PathVariable final long userId) {
//        userService.delete(userId);
//    }
//
//    //-- Error Handler
//    @ExceptionHandler(DuplicateEmailException.class)
//    @ResponseStatus(value= HttpStatus.CONFLICT)
//    public ErrorResponse handleError(
//            final HttpServletRequest request,
//            final DuplicateEmailException exception
//    ) {
//        return handleException(request, HttpStatus.CONFLICT, "duplicate_email", exception);
//    }
//
//    @ExceptionHandler(AccountAlreadyExistException.class)
//    @ResponseStatus(value= HttpStatus.CONFLICT)
//    public ErrorResponse handleError(
//            final HttpServletRequest request,
//            final AccountAlreadyExistException exception
//    ) {
//        return handleException(request, HttpStatus.CONFLICT, "account_already_exist", exception);
//    }
//
//    //-- Private
//    private UserResponse toUserResponse (User user) {
//        UserResponse.Builder builder = new UserResponse.Builder()
//                .withUser(user);
//
//        UserStatus userStatus = userStatusService.findById(user.getStatusId());
//        if (userStatus != null) {
//            UserStatusCode statusCode = userStatusCodeService.findById(userStatus.getStatusCodeId());
//            builder
//                    .withUserStatus(userStatus)
//                    .withUserStatusCode(statusCode)
//            ;
//        }
//        return builder.build();
//    }
}
