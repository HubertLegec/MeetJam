package com.pik.security;

import com.pik.model.Account;
import com.pik.repository.AccountRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public class UserDetailsStorageService implements UserDetailsService {

    private AccountRepository accountRepository;

    public UserDetailsStorageService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByLogin(username);
        if (account != null) {
            return account;
        } else {
            throw new UsernameNotFoundException("could not find the user '" + username + "'");
        }
    }
}
