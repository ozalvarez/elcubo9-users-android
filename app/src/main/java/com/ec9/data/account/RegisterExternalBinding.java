package com.ec9.data.account;

/**
 * Created by Oswaldo on 11/27/2014.
 */
public class RegisterExternalBinding {
    private String Name;
    private String Email;
    private String Provider;
    private String ExternalAccessToken;

    public RegisterExternalBinding(String name, String email, String provider, String externalAccessToken) {
        Name = name;
        Email = email;
        Provider = provider;
        ExternalAccessToken = externalAccessToken;
    }
}
