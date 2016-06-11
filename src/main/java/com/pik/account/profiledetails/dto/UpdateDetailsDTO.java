package com.pik.account.profiledetails.dto;

import groovy.transform.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString(includeNames = true)
public class UpdateDetailsDTO {
    private List<String> userInstruments = new ArrayList<>();
    private String skypeLogin;
    private String instagramLogin;
    private String phoneNumber;
    private String description;

    public UpdateDetailsDTO() {

    }

    public UpdateDetailsDTO(List<String> userInstruments, String skypeLogin, String instagramLogin, String phoneNumber, String description) {
        this.userInstruments = userInstruments;
        this.skypeLogin = skypeLogin;
        this.instagramLogin = instagramLogin;
        this.phoneNumber = phoneNumber;
        this.description = description;
    }

    public List<String> getUserInstruments() {
        return userInstruments;
    }

    public void setUserInstruments(List<String> userInstruments) {
        this.userInstruments = userInstruments;
    }

    public String getSkypeLogin() {
        return skypeLogin;
    }

    public void setSkypeLogin(String skypeLogin) {
        this.skypeLogin = skypeLogin;
    }

    public String getInstagramLogin() {
        return instagramLogin;
    }

    public void setInstagramLogin(String instagramLogin) {
        this.instagramLogin = instagramLogin;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
