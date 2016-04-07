package com.pik.model.dto;

import java.util.ArrayList;
import java.util.List;

public class AuthenticationResponseDTO {
    public boolean valid;
    public String token;
    public List<String> messages = new ArrayList<>();
}
