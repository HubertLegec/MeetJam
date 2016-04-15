package com.pik.account.registration;

import java.util.ArrayList;
import java.util.List;

public class ResultMessageDTO {
    public List<String> messages = new ArrayList<>();

    public ResultMessageDTO() {
    }

    public ResultMessageDTO(String message) {
        messages.add(message);
    }
}
