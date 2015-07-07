package com.tchepannou.uds.controller;

import com.tchepannou.uds.dto.*;
import com.tchepannou.uds.exception.DuplicateNameException;
import com.tchepannou.uds.service.AuthorizationService;
import com.tchepannou.uds.service.DomainService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    @ApiOperation("Find a domain by ID")
    public DomainResponse findById(@PathVariable final long domainId) {
        return domainService.findById(domainId);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation("Find all the domains")
    public DomainListResponse findAll() {
        return domainService.findAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation("Create a new domain")
    public DomainResponse create(@RequestBody @Valid final DomainRequest request) {
        return domainService.create(request);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{domainId}")
    @ApiOperation("Update a domain")
    public DomainResponse update(
            @PathVariable final long domainId,
            @Valid @RequestBody final DomainRequest request
    ) {
        return domainService.update(domainId, request);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{domainId}")
    @ApiOperation("Delete a domain by ID")
    public void delete(@PathVariable final long domainId) {
        domainService.delete(domainId);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{domainId}/users/{userId}/roles")
    @ApiOperation("Grant to a user a role in a domain")
    public RoleListResponse roles (
            @PathVariable final long domainId,
            @PathVariable final long userId
    ) {
        return authorizationService.roles(domainId, userId);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{domainId}/users/{userId}/permissions")
    @ApiOperation("Grant to a user a role in a domain")
    public PermissionListResponse permissions (
            @PathVariable final long domainId,
            @PathVariable final long userId
    ) {
        return authorizationService.permissions(domainId, userId);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{domainId}/users/{userId}/roles/{roleId}")
    @ApiOperation("Grant to a user a role in a domain")
    public void grant (
            @PathVariable final long domainId,
            @PathVariable final long userId,
            @PathVariable final long roleId
    ) {
        authorizationService.grant(domainId, userId, roleId);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{domainId}/users/{userId}/roles/{roleId}")
    @ApiOperation("Remove a user's role in a domain")
    public void revoke (
            @PathVariable final long domainId,
            @PathVariable final long userId,
            @PathVariable final long roleId
    ) {
        authorizationService.revoke(domainId, userId, roleId);
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

}
