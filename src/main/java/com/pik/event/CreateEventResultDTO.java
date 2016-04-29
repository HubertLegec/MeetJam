package com.pik.event;

import java.util.ArrayList;
import java.util.List;

public class CreateEventResultDTO {
    private String id;
    private List<String> messages = new ArrayList<>();

    public CreateEventResultDTO() {
    }

    public CreateEventResultDTO(String id) {
        this.id = id;
    }

    public CreateEventResultDTO(List<String> messages) {
        this.id = null;
        this.messages = messages;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
}
