package com.pik.service;

import com.pik.model.Account;
import com.pik.model.dto.AccountDTO;
import com.pik.repository.AccountRepository;
import com.pik.utils.CustomPasswordValidator;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class AccountService {

    private AccountRepository accountRepository;
    private CustomPasswordValidator passwordValidator = new CustomPasswordValidator();


    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public String createNewAccount(AccountDTO accountDTO){
        if(accountRepository.findByLogin(accountDTO.getLogin()) != null)
            return "User with this login already exists!";

        if(!EmailValidator.getInstance().isValid(accountDTO.getEmail()))
            return "User email is invalid!";

        if (!passwordValidator.validatePassword(accountDTO.getPassword())) {
            return passwordValidator.getLastMessage();
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Account newAccount = new Account(accountDTO.getLogin(), encoder.encode(accountDTO.getPassword()), accountDTO.getEmail());
        accountRepository.save(newAccount);

        return "Success";
    }

    public Account findAccountByLogin(String login){
        return accountRepository.findByLogin(login);
    }

}
