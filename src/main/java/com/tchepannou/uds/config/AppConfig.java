package com.tchepannou.uds.config;

import com.tchepannou.uds.dao.DomainDao;
import com.tchepannou.uds.dao.UserStatusCodeDao;
import com.tchepannou.uds.dao.impl.DomainDaoImpl;
import com.tchepannou.uds.dao.impl.UserStatusCodeDaoImpl;
import com.tchepannou.uds.service.DomainService;
import com.tchepannou.uds.service.UserStatusCodeService;
import com.tchepannou.uds.service.impl.DomainServiceImpl;
import com.tchepannou.uds.service.impl.UserStatusCodeServiceImpl;
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
    public DomainDao domainDao () {
        return new DomainDaoImpl(dataSource());
    }
    @Bean
    public DomainService domainService () {
        return new DomainServiceImpl();
    }

    @Bean
    public UserStatusCodeDao statusCodeDao () {
        return new UserStatusCodeDaoImpl(dataSource());
    }
    @Bean
    public UserStatusCodeService userStatusCodeService() {
        return new UserStatusCodeServiceImpl();
    }
}
