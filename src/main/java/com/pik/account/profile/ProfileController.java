package com.pik.account.profile;

import com.pik.account.profile.dto.ChangeEmailDTO;
import com.pik.account.profile.dto.ChangePasswordDTO;
import com.pik.account.profile.dto.ProfileDTO;
import com.pik.account.profile.dto.UpdateDetailsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


@RestController
@RequestMapping("/api/account")
public class ProfileController {
    private ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @RequestMapping(value = "changePassword", method = POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> changePassword(@RequestHeader(value = "X-AUTH-TOKEN") String token, @RequestBody ChangePasswordDTO dto){
        profileService.changePassword(token, dto);
        return ResponseEntity.ok("SUCCESS");
    }

    @RequestMapping(value = "changeEmail", method = POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> changeEmail(@RequestHeader(value = "X-AUTH-TOKEN") String token, @RequestBody ChangeEmailDTO dto) {
        profileService.changeEMail(token, dto);
        return ResponseEntity.ok("SUCCESS");
    }

    @RequestMapping(value = "details", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ProfileDTO> getProfileDetails(@RequestParam String login){
        ProfileDTO result = profileService.getProfileDetails(login);
        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "details", method = POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateProfileDetails(@RequestHeader(value = "X-AUTH-TOKEN") String token, @RequestBody UpdateDetailsDTO dto) {
        profileService.updateProfileDetails(token, dto);
        return ResponseEntity.ok("SUCCESS");
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<String> invalidPasswordHandler(InvalidPasswordException e) {
        return ResponseEntity.status(e.getStatus()).body(e.getMessage());
    }

    @ExceptionHandler(LoginDoesNotExistException.class)
    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Login not found")
    public void loginDoesNotExistHandler(){}
}
