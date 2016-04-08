package com.pik.service;

import com.pik.model.Account;
import com.pik.model.dto.AccountDTO;
import com.pik.model.errors.InvalidRegisterParameterError;
import com.pik.model.exception.InvalidRegisterParametersException;
import com.pik.repository.AccountRepository;
import com.pik.utils.CustomPasswordValidator;
import com.sun.istack.internal.NotNull;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.LinkedList;
import java.util.List;


public class AccountService {


    private AccountRepository accountRepository;
    private CustomPasswordValidator passwordValidator = new CustomPasswordValidator();


    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createNewAccount(@NotNull AccountDTO accountDTO){

        List<InvalidRegisterParameterError> errors = new LinkedList<>();

        if(accountRepository.findByLogin(accountDTO.getLogin()) != null)
        {
            errors.add(InvalidRegisterParameterError.DUPLICATE_LOGIN);
        }

        if(!EmailValidator.getInstance().isValid(accountDTO.getEmail()))
        {
            errors.add(InvalidRegisterParameterError.INVALID_EMAIL);
        }

        if (!passwordValidator.validatePassword(accountDTO.getPassword()))
        {
            errors.add(InvalidRegisterParameterError.INVALID_PASSWORD);
        }

        if(!errors.isEmpty())
            throw new InvalidRegisterParametersException(errors,passwordValidator.getLastPasswordIssues());

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Account newAccount = new Account(accountDTO.getLogin(), encoder.encode(accountDTO.getPassword()), accountDTO.getEmail());
        accountRepository.save(newAccount);

        return newAccount;
    }

}
