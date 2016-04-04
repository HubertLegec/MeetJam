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


    public Account(){}

    public Account(String username, String password, String email) {
        this.login = username;
        this.password = password;
        this.email = email;
        authorities.add(new Authority(Authority.USER));
    }

    public Account(String username, String password, String email, Authority authority) {
        this.login = username;
        this.password = password;
        this.email = email;
        authorities.add(authority);
    }


    public BigInteger getId() {
        return id;
    }
    public void setId(BigInteger id) {
        this.id = id;
    }
    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void addAuthority(Authority authority){
        authorities.add(authority);
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public void setPassword(String password) {
        this.password = password;
    }

}
