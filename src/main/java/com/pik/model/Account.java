package com.pik.model;

import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class Account implements UserDetails{
    @Id
    private BigInteger id;
    private String login;
    private String password;
    private String email;
    private List<Authority> authorities = new ArrayList<>();


    Account(){}

    public Account(String username, String password, String email) {
        this.login = username;
        this.password = password;
        this.email = email;
        authorities.add(new Authority(Authority.USER));
    }


    public String getLogin() {
        return login;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
