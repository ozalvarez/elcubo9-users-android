package com.ec9.data.account;

/**
 * Created by Oswaldo on 11/27/2014.
 */
public class LoginModel {
    private String grant_type;
    private String username;
    private String password;

    public LoginModel(String username, String password) {
        this.grant_type="password";
        this.username = username;
        this.password = password;
    }

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
}
