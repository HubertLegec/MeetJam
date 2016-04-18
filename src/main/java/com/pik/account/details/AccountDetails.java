package com.pik.account.details;

import java.util.ArrayList;
import java.util.List;

public class AccountDetails {
    private List<InstrumentType> userInstruments = new ArrayList<>();
    private String skypeLogin;
    private String phoneNumber;
    private String description;

    public AccountDetails(List<InstrumentType> userInstruments, String skypeLogin, String phoneNumber, String description) {
        this.userInstruments = userInstruments;
        this.skypeLogin = skypeLogin;
        this.phoneNumber = phoneNumber;
        this.description = description;
    }

    public AccountDetails() { }

    public List<InstrumentType> getUserInstruments() {
        return userInstruments;
    }

    public void addUserInstruments(InstrumentType userInstrument) {
        this.userInstruments.add(userInstrument);
    }
}
