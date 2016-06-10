package com.pik.account.profiledetails.dto;

import groovy.transform.ToString;

@ToString(includeNames = true)
public class ChangePasswordDTO {
    private String oldPassword;
    private String newPassword;

    public ChangePasswordDTO() {

    }

    public ChangePasswordDTO(String newPassword, String oldPassword) {
        this.newPassword = newPassword;
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}
