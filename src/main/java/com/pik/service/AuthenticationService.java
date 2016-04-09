package com.pik.service;

import com.pik.model.dto.AuthenticationResponseDTO;
import com.pik.repository.AccountRepository;
import com.pik.security.TokenHandler;
import com.sun.istack.internal.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthenticationService {
    private AccountRepository accountRepository;
    private AuthenticationManager authenticationManager;
    private TokenHandler tokenHandler;

    @Autowired
    public AuthenticationService(AccountRepository accountRepository, AuthenticationManager authenticationManager, TokenHandler tokenHandler) {
        this.accountRepository = accountRepository;
        this.authenticationManager = authenticationManager;
        this.tokenHandler = tokenHandler;
    }


    public AuthenticationResponseDTO login(@NotNull String login, @NotNull String password){
        try {
            this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(login, password)
            );

            // Reload password post-authentication so we can generate token
            UserDetails userDetails = accountRepository.findByLogin(login);
            String token = tokenHandler.createTokenForUser(new User(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities()));

            AuthenticationResponseDTO responseDTO = new AuthenticationResponseDTO();
            responseDTO.token = token;
            responseDTO.valid = true;

            return responseDTO;

        } catch (AuthenticationException e){
            AuthenticationResponseDTO responseDTO = new AuthenticationResponseDTO();
            responseDTO.token = null;
            responseDTO.valid = false;
            responseDTO.messages.add(e.getMessage());

            return responseDTO;
        }
    }
}
