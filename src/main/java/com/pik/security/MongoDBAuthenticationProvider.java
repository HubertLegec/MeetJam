package com.pik.security;

import com.pik.model.Account;
import com.pik.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

public class MongoDBAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private AccountRepository accounts;

    @Autowired
    public MongoDBAuthenticationProvider(AccountRepository accountRepository) {
        this.accounts = accountRepository;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        UserDetails loadedUser;

        try {
            Account account = accounts.findByLogin(username);
            loadedUser = new User(account.getUsername(), account.getPassword(), account.getAuthorities());
        } catch (Exception repositoryProblem) {
            throw new InternalAuthenticationServiceException(repositoryProblem.getMessage(), repositoryProblem);
        }

        if (loadedUser == null) {
            throw new InternalAuthenticationServiceException(
                    "UserDetailsService returned null, which is an interface contract violation");
        }
        return loadedUser;
    }
}