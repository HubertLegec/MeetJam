package com.pik.account.profilepicture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/api/account")
public class ProfilePictureController {

    private ProfilePictureService profilePictureService;

    @Autowired
    public ProfilePictureController(ProfilePictureService profilePictureService) {
        this.profilePictureService = profilePictureService;
    }

    @RequestMapping(value = "picture", method = POST)
    public ResponseEntity<String> uploadPicture(@RequestHeader(value = "X-AUTH-TOKEN") String token, @RequestParam("file") MultipartFile file) {
        if(!file.isEmpty()){
            profilePictureService.uploadProfilePicture(token, file);
            return ResponseEntity.ok("SUCCESS");
        } else {
            return ResponseEntity.status(NO_CONTENT).body("INPUT FILE IS EMPTY");
        }
    }

    @RequestMapping(value = "picture", method = GET, produces = IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getProfilePicture(@RequestParam String login) {
        ProfilePicture profilePicture = profilePictureService.getUserProfilePicture(login);
        if (profilePicture == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else {
            return ResponseEntity.ok(profilePicture.getImage());
        }
    }

    @ExceptionHandler(ProcessingImageException.class)
    public ResponseEntity<String> processingImageExceptionHandler(ProcessingImageException e){
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
