package com.harshitsourav.framework.api.models;

public class AuthResponse {
    private String token;
    private String reason;

    public AuthResponse() {
    }

    public String getToken() {
        return token;
    }

    public String getReason() {
        return reason;
    }

    public void setToken() {
        this.token = token;
    }

    public void setReason() {
        this.reason = reason;
    }

}
