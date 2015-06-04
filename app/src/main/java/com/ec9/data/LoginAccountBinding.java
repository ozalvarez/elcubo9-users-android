package com.ec9.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Oswaldo on 11/16/2014.
 */
public class LoginAccountBinding {
    private String access_token;
    private String refresh_token;

    public String getAccess_token() {
        return access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }
}
