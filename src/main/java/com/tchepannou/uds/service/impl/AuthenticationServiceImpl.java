package com.tchepannou.uds.service.impl;

import com.tchepannou.uds.dao.AccessTokenDao;
import com.tchepannou.uds.dao.DomainUserDao;
import com.tchepannou.uds.dao.UserDao;
import com.tchepannou.uds.dao.UserStatusCodeDao;
import com.tchepannou.uds.domain.AccessToken;
import com.tchepannou.uds.domain.DomainUser;
import com.tchepannou.uds.domain.User;
import com.tchepannou.uds.domain.UserStatusCode;
import com.tchepannou.uds.dto.AuthRequest;
import com.tchepannou.uds.dto.AuthResponse;
import com.tchepannou.uds.exception.*;
import com.tchepannou.uds.service.AuthenticationService;
import com.tchepannou.uds.service.PasswordEncryptor;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public class AuthenticationServiceImpl implements AuthenticationService {
    //-- Attributes
    @Value("${access_token.ttl.minutes:30}")
    private int ttl;

    @Autowired
    private AccessTokenDao accessTokenDao;

    @Autowired
    private DomainUserDao domainUserDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserStatusCodeDao userStatusCodeDao;

    @Autowired
    private PasswordEncryptor passwordEncryptor;

    //-- AuthenticationService overrides
    @Override
    public AuthResponse findById(final long id) {
        AccessToken token = accessTokenDao.findById(id);
        if (token == null) {
            throw new NotFoundException(id, AccessToken.class);
        } else if (token.isExpired()){
            throw new AccessTokenExpiredException(id);
        }

        return toAuthResponse(token);
    }

    @Override
    @Transactional
    public AuthResponse login(final AuthRequest request) {
        /* Authenticate */
        User user = userDao.findByLogin(request.getLogin());
        if (user == null || user.isDeleted()){
            throw new AuthFailedException("User{" + request.getLogin() + " not found");
        }
        if (!passwordEncryptor.matches(request.getPassword(), user)){
            throw new AuthFailedException("Password mismatch");
        }

        /* make sure account is active */
        UserStatusCode statusCode = userStatusCodeDao.findByUser(user.getId());
        if (statusCode == null || !statusCode.isActive()){
            throw new AccountNotActiveException("User's status is not active. status=" + statusCode);
        }

        /* Make sure user as access to domain */
        List<DomainUser> domainUsers = domainUserDao.findByDomainByUser(request.getDomainId(), user.getId());
        if (domainUsers.isEmpty()){
            throw new AccessDeniedException(user + " doesn't have access to Domain{id=" + request.getDomainId() + "}");
        }

        /* Create access token */
        AccessToken token = createAccessToken(request, user);
        accessTokenDao.create(token);

        return toAuthResponse(token);
    }

    @Override
    @Transactional
    public void logout(final long authId) {
        AccessToken token = accessTokenDao.findById(authId);
        if (token == null) {
            throw new NotFoundException(authId, AccessToken.class);
        } else if (token.isExpired()) {
            throw new AccessTokenExpiredException(authId);
        } else {
            token.expire();
            accessTokenDao.update(token);
        }
    }

    //-- Private
    private AuthResponse toAuthResponse (AccessToken token) {
        return new AuthResponse.Builder()
                .withAccessToken(token)
                .build();
    }

    private AccessToken createAccessToken(AuthRequest request, User user) {
        final Date now = new Date();
        final AccessToken token = new AccessToken();
        token.setFromDate(now);
        token.setUserId(user.getId());
        token.setRemoteIp(request.getRemoteIp());
        token.setUserAgent(request.getUserAgent());
        token.setDomainId(request.getDomainId());
        token.setExpiryDate(DateUtils.addMinutes(now, ttl));

        return token;
    }

}
