package com.pik.configuration;

import com.pik.aop.LoggingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class AppConfig {

    @Bean
    LoggingService loggingService(){
        return new LoggingService();
    }

}
