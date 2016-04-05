package com.pik.service;

import com.pik.model.Account;
import com.pik.model.dto.AccountDTO;
import com.pik.repository.AccountRepository;
import com.pik.utils.utils.CustomPaswordValidator;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    private CustomPaswordValidator paswordValidator = new CustomPaswordValidator();


    public String createNewAccount(AccountDTO accountDTO){
        if(accountRepository.findByLogin(accountDTO.getLogin())!=null)
            return "User with this login already exists!";

        if(!EmailValidator.getInstance().isValid(accountDTO.getEmail()))
            return "User email is invalid!";

        if (!paswordValidator.validatePassword(accountDTO.getPassword())) {
            return paswordValidator.getLastMessage();
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Account newAccount = new Account(accountDTO.getLogin(), encoder.encode(accountDTO.getPassword()), accountDTO.getEmail());
        accountRepository.save(newAccount);

        return "Success";
    }



}
