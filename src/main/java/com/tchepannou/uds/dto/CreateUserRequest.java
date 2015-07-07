package com.tchepannou.uds.dto;

import org.hibernate.validator.constraints.NotBlank;

public class CreateUserRequest {
    //-- Attributes
    @NotBlank(message="login")
    private String login;

    @NotBlank(message="password")
    private String password;

    private long partyId;

    //-- Getter/Setter
    public long getPartyId() {
        return partyId;
    }

    public void setPartyId(long partyId) {
        this.partyId = partyId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
