package com.pik.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class AuthenticationResponseDTO {
    @JsonIgnore
    public boolean valid;
    public String token;
    public List<String> messages = new ArrayList<>();
}
