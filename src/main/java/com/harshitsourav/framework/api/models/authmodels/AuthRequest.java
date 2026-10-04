package com.harshitsourav.framework.api.models.authmodels;

public class AuthRequest {
    private String username;
    private String password;

    // Constructors
    public AuthRequest() {
    }

    public AuthRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "AuthRequest[username=" + username + ", password=" + password + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AuthRequest)) {
            return false;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthRequest other = (AuthRequest) o;
        return username.equals(other.username) && password.equals(other.password);
    }

}
