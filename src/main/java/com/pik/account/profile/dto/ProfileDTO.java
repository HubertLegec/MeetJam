package com.pik.account.profile.dto;

import com.pik.account.Account;
import groovy.transform.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString(includeNames = true)
public class ProfileDTO {
    private String login;
    private String email;
    private List<String> userInstruments = new ArrayList<>();
    private String skypeLogin;
    private String instagramLogin;
    private String phoneNumber;
    private String description;

    public ProfileDTO() {

    }

    public ProfileDTO(Account account) {
        this.login = account.getUsername();
        this.email = account.getEmail();
        this.skypeLogin = account.getDetails().getSkypeLogin();
        this.instagramLogin = account.getDetails().getInstagramLogin();
        this.phoneNumber = account.getDetails().getPhoneNumber();
        this.description = account.getDetails().getDescription();
        account.getDetails().getUserInstruments().forEach( instrument ->
                userInstruments.add(instrument.getName())
        );
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInstagramLogin() {
        return instagramLogin;
    }

    public void setInstagramLogin(String instagramLogin) {
        this.instagramLogin = instagramLogin;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSkypeLogin() {
        return skypeLogin;
    }

    public void setSkypeLogin(String skypeLogin) {
        this.skypeLogin = skypeLogin;
    }

    public List<String> getUserInstruments() {
        return userInstruments;
    }

    public void setUserInstruments(List<String> userInstruments) {
        this.userInstruments = userInstruments;
    }
}
