package com.pik.security;

import com.pik.model.Account;
import com.pik.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by Hubert on 02.04.2016.
 */
@Service
public class UserDetailsStorageService implements UserDetailsService {
    @Autowired
    AccountRepository accountRepository;

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
