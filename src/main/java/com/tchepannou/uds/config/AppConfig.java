package com.tchepannou.uds.config;

import com.tchepannou.uds.dao.*;
import com.tchepannou.uds.dao.impl.*;
import com.tchepannou.uds.service.*;
import com.tchepannou.uds.service.impl.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * Declare your services here!
 */
@Configuration
public class AppConfig {
    @Value("${database.driver}")
    private String driver;

    @Value ("${database.url}")
    private String url;

    @Value ("${database.username}")
    private String username;

    @Value ("${database.password}")
    private String password;


    //-- Beans
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }


    @Bean
    public DataSource dataSource (){
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setDriverClassName(driver);
        ds.setUrl(url);
        return ds;
    }

    @Bean
    public AccessTokenDao accessTokenDao () {
        return new AccessTokenDaoImpl(dataSource());
    }

    @Bean
    public AuthorizationService authorizationService (){
        return new AuthorizationServiceImpl();
    }

    @Bean
    public AuthenticationService authService () {
        return new AuthenticationServiceImpl();
    }

    @Bean
    PasswordEncryptor passwordEncryptor (){
        return new PasswordEncryptorImpl();
    }

    @Bean
    public DomainDao domainDao () {
        return new DomainDaoImpl(dataSource());
    }

    @Bean
    public DomainService domainService () {
        return new DomainServiceImpl();
    }

    @Bean
    public DomainUserDao domainUserDao () {
        return new DomainUserDaoImpl(dataSource());
    }

    @Bean
    public PermissionDao permissionDao () {
        return new PermissionDaoImpl(dataSource());
    }

    @Bean
    public RoleDao roleDao () {
        return new RoleDaoImpl(dataSource());
    }

    @Bean
    public UserStatusCodeDao userStatusCodeDao () {
        return new UserStatusCodeDaoImpl(dataSource());
    }
    @Bean
    public UserStatusCodeService userStatusCodeService() {
        return new UserStatusCodeServiceImpl();
    }

    @Bean
    public UserDao userDao () {
        return new UserDaoImpl(dataSource());
    }

    @Bean
    public UserStatusDao userStatusDao () {
        return new UserStatusDaoImpl(dataSource());
    }
    @Bean
    public UserService userService() {
        return new UserServiceImpl();
    }
}
