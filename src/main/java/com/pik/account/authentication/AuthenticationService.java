package com.pik.account.authentication;

import com.pik.account.AccountRepository;
import com.pik.security.TokenHandler;
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


    public AuthenticationService(AccountRepository accountRepository,
                                 AuthenticationManager authenticationManager,
                                 TokenHandler tokenHandler) {
        this.accountRepository = accountRepository;
        this.authenticationManager = authenticationManager;
        this.tokenHandler = tokenHandler;
    }


    public AuthenticationResponseDTO login(AuthenticationRequestDTO dto) throws InvalidLoginParametersException {
        notNull(dto);
        validateInputObject(dto);
        UserDetails userDetails = findUserInDatabase(dto);
        try {
            this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getLogin(), dto.getPassword())
            );
        } catch (AuthenticationException e){
            throw new InvalidLoginParametersException(InvalidLoginParametersError.INVALID_PASSWORD.getMessage());
        }

        return generateTokenForUser(userDetails);
    }

    private void validateInputObject(AuthenticationRequestDTO dto) throws InvalidLoginParametersException {
        List<String> result = new ArrayList<>();
        if(dto.getLogin() == null){
            result.add(
                    InvalidLoginParametersError
                            .NULL_LOGIN_VALUE
                            .getMessage()
            );
        }
        if(dto.getPassword() == null){
            result.add(
                    InvalidLoginParametersError
                            .NULL_PASSWORD_VALUE
                            .getMessage()
            );
        }
        if(result.size() > 0){
            throw new InvalidLoginParametersException(result);
        }
    }

    private UserDetails findUserInDatabase(AuthenticationRequestDTO dto) throws InvalidLoginParametersException {
        UserDetails userDetails = accountRepository.findByLogin(dto.getLogin());
        if(userDetails == null){
            throw new InvalidLoginParametersException(
                    InvalidLoginParametersError
                            .NON_EXISTING_LOGIN
                            .getMessage()
            );
        }
        return userDetails;
    }

    private AuthenticationResponseDTO generateTokenForUser(UserDetails user) {
        String token = tokenHandler.createTokenForUser(
                new User(user.getUsername(), user.getPassword(), user.getAuthorities())
        );
        return new AuthenticationResponseDTO(token);
    }
}
