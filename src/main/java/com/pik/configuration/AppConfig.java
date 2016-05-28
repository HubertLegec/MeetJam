package com.pik.configuration;

import com.pik.aop.LoggingService;
import com.pik.account.AccountRepository;
import com.pik.account.registration.RegistrationService;
import com.pik.event.details.EventDetailsService;
import com.pik.event.search.SearchEventService;
import com.pik.security.TokenAuthenticationService;
import com.pik.event.EventRepository;
import com.pik.event.createremove.CreateRemoveEventService;
import com.pik.security.TokenHandler;
import com.pik.security.UserDetailsStorageService;
import com.pik.account.authentication.AuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.authentication.AuthenticationManager;

@Configuration
@EnableAspectJAutoProxy
public class AppConfig {

    @Bean
    LoggingService loggingService(){
        return new LoggingService();
    }

    @Bean
    UserDetailsStorageService userDetailsService(AccountRepository accountRepository){
        return new UserDetailsStorageService(accountRepository);
    }

    @Bean
    RegistrationService accountService(AccountRepository accountRepository){
        return new RegistrationService(accountRepository);
    }

    @Bean
    CreateRemoveEventService eventService(EventRepository eventRepository, TokenHandler tokenHandler){
        return new CreateRemoveEventService(eventRepository, tokenHandler);
    }

    @Bean
    SearchEventService searchEventService(EventRepository eventRepository, TokenHandler tokenHandler){
        return new SearchEventService(eventRepository, tokenHandler);
    }

    @Bean
    EventDetailsService eventDetailsService(EventRepository eventRepository){
        return new EventDetailsService(eventRepository);
    }

    @Bean
    AuthenticationService authenticationService(AccountRepository accountRepository,
                                                AuthenticationManager authenticationManager,
                                                TokenHandler tokenHandler){
        return new AuthenticationService(accountRepository, authenticationManager, tokenHandler);
    }

    @Bean
    TokenHandler tokenHandler(UserDetailsStorageService userDetailsService){
        return new TokenHandler("meetJam", userDetailsService);
    }

    @Bean
    public TokenAuthenticationService tokenAuthenticationService(TokenHandler tokenHandler){
        return new TokenAuthenticationService(tokenHandler);
    }

}
