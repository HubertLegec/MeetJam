package com.pik.account.profilepicture;

import com.pik.security.TokenHandler;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;

public class ProfilePictureService {

    private TokenHandler tokenHandler;
    private ProfilePictureRepository profilePictureRepository;

    public ProfilePictureService(TokenHandler tokenHandler, ProfilePictureRepository profilePictureRepository) {
        this.tokenHandler = tokenHandler;
        this.profilePictureRepository = profilePictureRepository;
    }

    void uploadProfilePicture(String token, MultipartFile file){
        String userName = getLoginFromToken(token);
        ProfilePicture oldPicture = profilePictureRepository.getProfilePictureByUserLogin(userName);

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
