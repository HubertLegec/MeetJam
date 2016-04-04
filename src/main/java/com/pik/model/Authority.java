package com.pik.model;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created by Hubert on 02.04.2016.
 */
public class Authority implements GrantedAuthority {
    public static final String USER = "USER";
    public static final String ADMIN = "ADMIN";
    private String value;

    public Authority(String value){
        this.value = value;
    }

    @Override
    public String getAuthority() {
        return value;
    }
}
