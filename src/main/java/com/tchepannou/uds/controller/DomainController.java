package com.tchepannou.uds.controller;

import com.tchepannou.uds.dto.*;
import com.tchepannou.uds.exception.DuplicateNameException;
import com.tchepannou.uds.service.AuthorizationService;
import com.tchepannou.uds.service.DomainService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
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
@Api(basePath = "/domains", value = "Domains", produces = MediaType.APPLICATION_JSON_VALUE)
@RequestMapping(value="/api/domains", produces = MediaType.APPLICATION_JSON_VALUE)
public class DomainController extends AbstractController {
    //-- Attributes
    private static final Logger LOG = LoggerFactory.getLogger(DomainController.class);

    @Autowired
    private DomainService domainService;

    @Autowired
    private AuthorizationService authorizationService;


    //-- AbstractController overrides
    @Override
    protected Logger getLogger() {
        return LOG;
    }

    //-- REST methods
    @RequestMapping(method = RequestMethod.GET, value = "/{domainId}")
    @ApiOperation("Returns a domain")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "Domain not found", response = ErrorResponse.class)
    })
    public DomainResponse findById(@PathVariable final long domainId) {
        return domainService.findById(domainId);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation("Returns all the domains")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
    })
    public DomainListResponse findAll() {
        return domainService.findAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation("Create")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success"),
            @ApiResponse(code = 400, message = "Invalid request", response = ErrorResponse.class),
            @ApiResponse(code = 409, message = "failure", response = ErrorResponse.class)
    })
    public ResponseEntity<DomainResponse> create(@RequestBody @Valid final DomainRequest request) {
        DomainResponse response = domainService.create(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{domainId}")
    @ApiOperation("Update")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Invalid request", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "Domain not found", response = ErrorResponse.class),
            @ApiResponse(code = 409, message = "failure", response = ErrorResponse.class)
    })
    public DomainResponse update(
            @PathVariable final long domainId,
            @Valid @RequestBody final DomainRequest request
    ) {
        return domainService.update(domainId, request);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{domainId}")
    @ApiOperation("Delete")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "Domain not found", response = ErrorResponse.class),
    })
    public void delete(@PathVariable final long domainId) {
        domainService.delete(domainId);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{domainId}/users/{userId}/roles")
    @ApiOperation("Return user's roles")
    public RoleListResponse roles (
            @PathVariable final long domainId,
            @PathVariable final long userId
    ) {
        return authorizationService.roles(domainId, userId);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{domainId}/users/{userId}/permissions")
    @ApiOperation("Return user's permissions")
    public PermissionListResponse permissions (
            @PathVariable final long domainId,
            @PathVariable final long userId
    ) {
        return authorizationService.permissions(domainId, userId);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{domainId}/users/{userId}/roles/{roleId}")
    @ApiOperation("Grant a role to user")
    public void grant (
            @PathVariable final long domainId,
            @PathVariable final long userId,
            @PathVariable final long roleId
    ) {
        authorizationService.grant(domainId, userId, roleId);
    }

    //-- Message Handler
    @ExceptionHandler(DuplicateNameException.class)
    @ResponseStatus(value= HttpStatus.CONFLICT)
    public ErrorResponse handleError(
            final HttpServletRequest request,
            final DuplicateNameException exception
    ) {
        return handleException(request, HttpStatus.CONFLICT, "duplicate_name", exception);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{domainId}/users/{userId}/roles/{roleId}")
    @ApiOperation("Revoke a user's role")
    public void revoke (
            @PathVariable final long domainId,
            @PathVariable final long userId,
            @PathVariable final long roleId
    ) {
        authorizationService.revoke(domainId, userId, roleId);
    }

}
