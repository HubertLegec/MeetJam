package com.pik.account.profiledetails;

import com.pik.common.InstrumentType;

import java.util.ArrayList;
import java.util.List;

public class AccountDetails {
    private List<InstrumentType> userInstruments = new ArrayList<>();
    private String skypeLogin;
    private String instagramLogin;
    private String phoneNumber;
    private String description;

    public List<InstrumentType> getUserInstruments() {
        return userInstruments;
    }

    public void addUserInstruments(InstrumentType userInstrument) {
        this.userInstruments.add(userInstrument);
    }

    public void setUserInstruments(List<InstrumentType> userInstruments) { this.userInstruments = userInstruments; }

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

    public String getInstagramLogin() {
        return instagramLogin;
    }

    public void setInstagramLogin(String instagramLogin) {
        this.instagramLogin = instagramLogin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
