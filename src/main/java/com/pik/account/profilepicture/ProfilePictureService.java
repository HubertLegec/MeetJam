package com.pik.account.profilepicture;

import com.pik.security.TokenHandler;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class ProfilePictureService {

    private TokenHandler tokenHandler;
    private ProfilePictureRepository profilePictureRepository;

    public ProfilePictureService(TokenHandler tokenHandler, ProfilePictureRepository profilePictureRepository) {
        this.tokenHandler = tokenHandler;
        this.profilePictureRepository = profilePictureRepository;
    }

    void uploadProfilePicture(String token, MultipartFile file) {
        String userName = getLoginFromToken(token);
        byte[] fileBytes = getBytesFromMultipartFile(file);
        ProfilePicture oldPicture = profilePictureRepository.getProfilePictureByUserLogin(userName);
        if(oldPicture == null){
            ProfilePicture profilePicture = new ProfilePicture(userName, fileBytes);
            profilePictureRepository.save(profilePicture);
        } else {
            oldPicture.setImage(fileBytes);
            profilePictureRepository.save(oldPicture);
        }
    }

    private byte[] getBytesFromMultipartFile(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new ProcessingImageException("Error during reading image");
        }
    }

    private String getLoginFromToken(String token) {
        try {
            return tokenHandler
                    .parseUserFromToken(token)
                    .getUsername();
        } catch (UsernameNotFoundException e) {
            return null;
        }
    }
}
