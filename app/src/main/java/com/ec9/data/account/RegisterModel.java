package com.ec9.data.account;

/**
 * Created by Oswaldo on 11/27/2014.
 */
public class RegisterModel {
    private String Name;
    private String Email;
    private String Password;
    private String ConfirmPassword;

    public RegisterModel(String name, String email, String password, String confirmPassword) {
        Name = name;
        Email = email;
        Password = password;
        ConfirmPassword = confirmPassword;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getConfirmPassword() {
        return ConfirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        ConfirmPassword = confirmPassword;
    }
}
