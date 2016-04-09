package com.pik.controller;

import com.pik.model.dto.AccountDTO;
import com.pik.model.dto.ResultMessageDTO;
import com.pik.model.exception.InvalidRegisterParametersException;
import com.pik.repository.AccountRepository;
import com.pik.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/account")
public class AccountController {
    private AccountService accountService;
    private AccountRepository accountRepository;

    @Autowired
    public AccountController(AccountService accountService, AccountRepository accountRepository) {
        this.accountService = accountService;
        this.accountRepository = accountRepository;
    }

    @RequestMapping(value = "register", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public ResponseEntity<ResultMessageDTO> registerNewAccount(@ModelAttribute AccountDTO accountDTO){
        try
        {
            accountService.createNewAccount(accountDTO);
        }
        catch (InvalidRegisterParametersException e)
        {
            ResultMessageDTO dto = new ResultMessageDTO();
            dto.messages = e.getMessages();
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(dto);
        }
        catch (DataAccessResourceFailureException e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResultMessageDTO(e.getMessage()));
        }

        return ResponseEntity.ok(new ResultMessageDTO("Account created."));
    }


}
