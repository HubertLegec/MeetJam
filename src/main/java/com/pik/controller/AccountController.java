package com.pik.controller;

import com.pik.model.dto.AccountDTO;
import com.pik.model.dto.ResultMessageDTO;
import com.pik.model.exception.BaseException;
import com.pik.repository.AccountRepository;
import com.pik.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;


@RestController
@RequestMapping("/api/account")
public class AccountController {
    private AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(value = "register", produces = APPLICATION_JSON_VALUE, method = POST)
    public ResponseEntity<ResultMessageDTO> registerNewAccount(@ModelAttribute AccountDTO accountDTO){
        try {
            accountService.createNewAccount(accountDTO);
        } catch (BaseException e) {
            ResultMessageDTO dto = new ResultMessageDTO();
            dto.messages = e.getMessages();
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .body(dto);
        } catch (DataAccessResourceFailureException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResultMessageDTO(e.getMessage()));
        }

        return ResponseEntity.ok(new ResultMessageDTO("Account created."));
    }


}
