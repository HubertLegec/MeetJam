package com.pik.account.authentication;

import java.util.ArrayList;
import java.util.List;

public class AuthenticationResponseDTO {
    private String token = null;
    private List<String> messages = new ArrayList<>();

    public AuthenticationResponseDTO(String token) {
        this.token = token;
    }

    public AuthenticationResponseDTO(List<String> messages) {
        this.messages = messages;
    }

    public AuthenticationResponseDTO() {}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
}
