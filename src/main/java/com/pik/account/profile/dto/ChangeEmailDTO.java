package com.pik.account.profile.dto;

import groovy.transform.ToString;

@ToString(includeNames = true)
public class ChangeEmailDTO {
    private String email;

    public ChangeEmailDTO() {
    }

    public ChangeEmailDTO(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
