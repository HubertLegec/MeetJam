package com.pik.model.dto;


public class AccountDTO {

    public String login;
    public String email;
    public String password;

    public AccountDTO(String login, String email, String password) {
        this.login = login;
        this.email = email;
        this.password = password;
    }

    public AccountDTO() {}
}
