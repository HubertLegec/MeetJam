package com.pik.account.profilepicture;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ProfilePicture {
    @Id
    private String id;
    private String userLogin;
    private byte[] image;

    public ProfilePicture() {
    }

    public ProfilePicture(String userLogin, byte[] image) {
        this.userLogin = userLogin;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
