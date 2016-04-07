package com.pik.model.dto;


import java.util.LinkedList;
import java.util.List;

public class ResultMessageDTO {

    public List<String> messages;

    public ResultMessageDTO(List<String> messages) {
        this.messages = messages;
    }
    public ResultMessageDTO(String message) {
        messages= new LinkedList<>();
        messages.add(message);
    }
}
