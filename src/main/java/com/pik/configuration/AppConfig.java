package com.pik.configuration;

import com.pik.aop.LoggingService;
import com.pik.repository.AccountRepository;
import com.pik.service.AccountService;
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

    @Bean
    AccountService accountService(AccountRepository accountRepository){
        return new AccountService(accountRepository);
    }


}
