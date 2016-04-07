package com.pik.service;

import com.pik.model.Account;
import com.pik.model.dto.AccountDTO;
import com.pik.repository.AccountRepository;
import com.pik.utils.CustomPasswordValidator;
import com.sun.xml.internal.ws.developer.Serialization;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


public class AccountService {


    private AccountRepository accountRepository;
    private CustomPasswordValidator passwordValidator = new CustomPasswordValidator();


    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public String createNewAccount(AccountDTO accountDTO){
        if(accountRepository.findByLogin(accountDTO.login) != null)
            return "User with this login already exists!";

        if(!EmailValidator.getInstance().isValid(accountDTO.email))
            return "User email is invalid!";

        if (!passwordValidator.validatePassword(accountDTO.password)) {
            return passwordValidator.getLastMessage();
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Account newAccount = new Account(accountDTO.login, encoder.encode(accountDTO.password), accountDTO.email);
        accountRepository.save(newAccount);

        return "Success";
    }

}
