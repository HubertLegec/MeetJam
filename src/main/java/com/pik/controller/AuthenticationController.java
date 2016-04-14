package com.pik.controller;

import com.pik.model.Authority;
import com.pik.model.dto.AuthenticationRequestDTO;
import com.pik.model.dto.AuthenticationResponseDTO;
import com.pik.model.dto.ResultMessageDTO;
import com.pik.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthenticationController {
    private AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @RequestMapping(value = "/auth/login", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<AuthenticationResponseDTO> authenticationRequest(@ModelAttribute AuthenticationRequestDTO dto) {
        AuthenticationResponseDTO result = authenticationService.login(dto);
        if (result.valid) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(401).body(result);
        }
    }

    @PreAuthorize(Authority.USER)
    @RequestMapping(value = "/ping", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public ResponseEntity<ResultMessageDTO> checkIfAuthorized(){
        ResultMessageDTO dto = new ResultMessageDTO();
        dto.messages.add("PONG");
        return ResponseEntity.ok(dto);
    }
}
