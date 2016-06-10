package com.pik.account.profilepicture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/api/account")
public class ProfilePictureController {

    private ProfilePictureService profilePictureService;

    @Autowired
    public ProfilePictureController(ProfilePictureService profilePictureService) {
        this.profilePictureService = profilePictureService;
    }

    @RequestMapping(value = "picture", method = POST)
    public ResponseEntity<String> uploadPicture(@RequestHeader(value = "X-AUTH-TOKEN") String token, @RequestParam MultipartFile file) {
        if(!file.isEmpty()){
            profilePictureService.uploadProfilePicture(token, file);
            return ResponseEntity.ok("SUCCESS");
        } else {
            return ResponseEntity.status(NO_CONTENT).body("INPUT FILE IS EMPTY");
        }
    }
}
