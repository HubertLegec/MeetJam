package com.pik.service;

import com.pik.model.dto.AuthenticationRequestDTO;
import com.pik.model.dto.AuthenticationResponseDTO;
import com.pik.repository.AccountRepository;
import com.pik.security.TokenHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.Assert.notNull;

public class AuthenticationService {
    private AccountRepository accountRepository;
    private AuthenticationManager authenticationManager;
    private TokenHandler tokenHandler;


    public AuthenticationService(AccountRepository accountRepository, AuthenticationManager authenticationManager, TokenHandler tokenHandler) {
        this.accountRepository = accountRepository;
        this.authenticationManager = authenticationManager;
        this.tokenHandler = tokenHandler;
    }


    public AuthenticationResponseDTO login(AuthenticationRequestDTO dto){
        notNull(dto);
        try {
            this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getLogin(), dto.getPassword())
            );

            // Reload password post-authentication so we can generate token
            UserDetails userDetails = accountRepository.findByLogin(dto.getLogin());
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

    private void validateInputObject(AuthenticationRequestDTO dto){
        List<String> result = new ArrayList<>();
        if(dto.getLogin() == null){
            result.add("Login field can't be null");
        }
        if(dto.getPassword() == null){
            result.add("Password field can't be null");
        }
    }
}
