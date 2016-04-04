package com.pik.controller;

import com.pik.model.Account;
import com.pik.model.dto.AccountDTO;
import com.pik.repository.AccountRepository;
import com.pik.service.AccountService;
import edu.vt.middleware.password.*;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Hubert on 01.04.2016.
 */
@RestController
@RequestMapping("/api/account")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping(value = "user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> user(){
        System.out.println("get user");
        return new ResponseEntity<>(new Account("Adam", "123", "email"), HttpStatus.OK);
    }



    @RequestMapping(value = "register", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    @ResponseBody
    public String register(@ModelAttribute AccountDTO dto){
        accountService.createNewAccount(dto);

        return "Account created!";
    }

    //Test Methods
    @RequestMapping(value = "users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> users(){
        List<String> accounts = accountRepository.findAll().stream().map(u -> u.getLogin()).collect(Collectors.toList());
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }
    @RequestMapping(value = "clearusers", produces = MediaType.APPLICATION_JSON_VALUE)
    public String clearUsers(){
        accountRepository.deleteAll();
        return "Erased users";
    }

    //sample /api/account/register
}
