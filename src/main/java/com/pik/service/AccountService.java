package com.pik.service;

import com.pik.model.Account;
import com.pik.model.dto.AccountDTO;
import com.pik.model.errors.InvalidRegisterParameterError;
import com.pik.model.exception.InvalidRegisterParametersException;
import com.pik.repository.AccountRepository;
import com.pik.utils.CustomPasswordValidator;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;



public class AccountService {
    private AccountRepository accountRepository;
    private CustomPasswordValidator passwordValidator = new CustomPasswordValidator();
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createNewAccount(AccountDTO accountDTO){
        validate(accountDTO);
        String hashedPassword = encoder.encode(accountDTO.getPassword());
        Account newAccount = new Account(
                accountDTO.getLogin(),
                hashedPassword,
                accountDTO.getEmail()
        );
        return accountRepository.save(newAccount);
    }

    private void validate(AccountDTO accountDTO) {
        List<InvalidRegisterParameterError> errors = new ArrayList<>();
        if(accountRepository.findByLogin(accountDTO.getLogin()) != null) {
            errors.add(InvalidRegisterParameterError.DUPLICATE_LOGIN);
        }

        if(!EmailValidator.getInstance().isValid(accountDTO.getEmail())) {
            errors.add(InvalidRegisterParameterError.INVALID_EMAIL);
        }

        if (!passwordValidator.validatePassword(accountDTO.getPassword())) {
            errors.add(InvalidRegisterParameterError.INVALID_PASSWORD);
        }

        if(!errors.isEmpty()) {
            throw new InvalidRegisterParametersException(errors, passwordValidator.getLastPasswordIssues());
        }
    }

}
