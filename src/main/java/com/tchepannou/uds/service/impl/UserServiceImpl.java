package com.tchepannou.uds.service.impl;

import com.tchepannou.uds.dao.UserDao;
import com.tchepannou.uds.dao.UserStatusCodeDao;
import com.tchepannou.uds.dao.UserStatusDao;
import com.tchepannou.uds.domain.User;
import com.tchepannou.uds.domain.UserStatus;
import com.tchepannou.uds.domain.UserStatusCode;
import com.tchepannou.uds.dto.CreateUserRequest;
import com.tchepannou.uds.dto.UserResponse;
import com.tchepannou.uds.dto.UserStatusRequest;
import com.tchepannou.uds.exception.BadRequestException;
import com.tchepannou.uds.exception.DuplicateLoginException;
import com.tchepannou.uds.exception.DuplicatePartyException;
import com.tchepannou.uds.exception.NotFoundException;
import com.tchepannou.uds.service.PasswordEncryptor;
import com.tchepannou.uds.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;

public class UserServiceImpl implements UserService {
    //-- Attributes
    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncryptor passwordEncryptor;

    @Autowired
    private UserStatusDao userStatusDao;

    @Autowired
    private UserStatusCodeDao userStatusCodeDao;


    //-- UserService overrides
    @Override
    public UserResponse findById(long id) {
        return toUserResponse(
                findUser(id)
        );
    }

    @Override
    public UserResponse findByLogin(String login) {
        User user = userDao.findByLogin(login);
        if (user == null || user.isDeleted()){
            throw new NotFoundException(login, User.class);
        }
        return toUserResponse(user);
    }

    @Override
    @Transactional
    public UserResponse create (final CreateUserRequest request) {
        try {
            User user = userDao.findByParty(request.getPartyId());
            if (user != null) {
                throw new DuplicatePartyException(request.getPartyId());
            }

            /* create user */
            user = new User();
            user.setPartyId(request.getPartyId());
            user.setLogin(request.getLogin());
            user.setPassword(passwordEncryptor.encrypt(request.getPassword()));
            userDao.create(user);

            /* default status */
            final UserStatusCode statusCode = userStatusCodeDao.findDefault();
            final UserStatus status = new UserStatus();
            status.setUserId(user.getId());
            status.setStatusCodeId(statusCode.getId());
            userStatusDao.create(status);

            /* set user's status */
            user.setStatusId(status.getId());
            userDao.update(user);

            return toUserResponse(user, status, statusCode);
        } catch (DuplicateKeyException e){  // NOSONAR
            throw new DuplicateLoginException(request.getLogin());
        }
    }

    @Override
    @Transactional
    public UserResponse updatePassword(final long id, final String password) {
        final User user = findUser(id);
        user.setPassword(
                passwordEncryptor.encrypt(password)
        );
        userDao.update(user);
        return toUserResponse(user);
    }

    @Override
    @Transactional
    public UserResponse updateLogin(final long id, final String login) {
        final User user = findUser(id);
        try {
            user.setLogin(login);
            userDao.update(user);
            return toUserResponse(user);
        } catch (DuplicateKeyException e){  // NOSONAR
            throw new DuplicateLoginException(login);
        }
    }

    @Override
    @Transactional
    public void delete(long id) {
        findUser(id);
        userDao.delete(id);
    }

    @Override
    @Transactional
    public UserResponse setStatus(long id, UserStatusRequest request) {
        UserStatusCode statusCode = userStatusCodeDao.findById(request.getStatusCode());
        if (statusCode == null){
            throw new BadRequestException("statusCode");
        }
        /* status */
        User user = findUser(id);
        UserStatus status = new UserStatus();
        status.setUserId(id);
        status.setStatusCodeId(request.getStatusCode());
        status.setComment(request.getComment());
        userStatusDao.create(status);

        /* update user */
        user.setStatusId(status.getId());

        return toUserResponse(user, status, statusCode);
    }

    //-- Private
    private User findUser (long id) {
        User user = userDao.findById(id);
        if (user == null || user.isDeleted()){
            throw new NotFoundException(id, User.class);
        }
        return user;
    }

    private UserResponse toUserResponse (User user) {
        UserStatus userStatus = userStatusDao.findById(user.getStatusId());
        UserStatusCode statusCode = userStatus != null ? userStatusCodeDao.findById(userStatus.getStatusCodeId()) : null;
        return toUserResponse(user, userStatus, statusCode);
    }
    private UserResponse toUserResponse (User user, UserStatus userStatus, UserStatusCode statusCode) {
        UserResponse.Builder builder = new UserResponse.Builder()
                .withUser(user);

        if (userStatus != null) {
            builder
                    .withUserStatus(userStatus)
                    .withUserStatusCode(statusCode)
            ;
        }
        return builder.build();
    }
}
