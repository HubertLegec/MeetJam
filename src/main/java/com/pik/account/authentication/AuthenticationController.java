package com.pik.account.authentication;

import com.pik.account.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


@RestController
public class AuthenticationController {
    private AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @RequestMapping(value = "/auth/login", produces = APPLICATION_JSON_VALUE, method = POST)
    @ResponseBody
    public ResponseEntity<AuthenticationResponseDTO> authenticationRequest(@ModelAttribute AuthenticationRequestDTO dto) {
        try {
            AuthenticationResponseDTO result = authenticationService.login(dto);
            return ResponseEntity.ok(result);
        } catch (InvalidLoginParametersException e){
            AuthenticationResponseDTO response = new AuthenticationResponseDTO(e.getMessages());
            return ResponseEntity.status(UNAUTHORIZED).body(response);
        }
    }

    @PreAuthorize(Authority.USER)
    @RequestMapping(value = "/ping", produces = TEXT_PLAIN_VALUE, method = GET)
    public ResponseEntity<String> checkIfAuthorized(){
        return ResponseEntity.ok("PONG");
    }
}
