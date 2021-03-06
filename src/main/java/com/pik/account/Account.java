package com.pik.account;

import com.pik.account.profiledetails.AccountDetails;
import groovy.transform.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Document
@ToString(includeNames = true, excludes = {"id"})
public class Account implements UserDetails {
    @Id
    private String id;
    private String login;
    private String password;
    private String email;
    private List<Authority> authorities = new ArrayList<>();
    private AccountDetails details = new AccountDetails();

    public Account(String login, String password, String email) {
        this.details = new AccountDetails();
        this.login = login;
        this.password = password;
        this.email = email;
        authorities.add(new Authority(Authority.USER));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AccountDetails getDetails() {
        return details;
    }

    public void setDetails(AccountDetails details) {
        this.details = details;
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
