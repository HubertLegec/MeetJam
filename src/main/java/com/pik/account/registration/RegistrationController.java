package com.pik.account.registration;

import com.pik.common.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


@RestController
@RequestMapping("/api/account")
public class RegistrationController {
    private RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @RequestMapping(value = "register", produces = APPLICATION_JSON_VALUE, method = POST)
    public ResponseEntity<ResultMessageDTO> registerNewAccount(@ModelAttribute AccountDTO accountDTO){
        try {
            registrationService.createNewAccount(accountDTO);
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
