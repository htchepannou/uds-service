package com.tchepannou.uds.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.tchepannou.uds.service.GreetingService;
import com.tchepannou.uds.service.impl.GreetingServiceImpl;

/**
 * Declare your services here!
 */
@Configuration
public class AppConfig {
    @Bean
    GreetingService greetingService (){
        return new GreetingServiceImpl();
    }
}
