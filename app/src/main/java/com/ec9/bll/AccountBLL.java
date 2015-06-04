package com.ec9.bll;

import android.app.Activity;

import com.ec9.data.LoginAccountBinding;
import com.ec9.data.account.LoginModel;
import com.ec9.data.account.ObtainLocalAccessTokenBinding;
import com.ec9.data.account.RegisterExternalBinding;
import com.ec9.data.account.RegisterModel;
import com.ec9.exceptions.RuleException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oswaldo on 11/15/2014.
 */
public class AccountBLL extends BaseBLL {

    public String postLogin(LoginModel model) throws RuleException {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("grant_type", "password"));
        params.add(new BasicNameValuePair("username", model.getUsername()));
        params.add(new BasicNameValuePair("password", model.getPassword()));

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(URL + "token");
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            return getJSON(httpClient.execute(httpPost));
        } catch (RuleException e) {
            throw e;
        } catch (UnsupportedEncodingException e) {
            throw new RuleException(e.getMessage());
        } catch (ClientProtocolException e) {
            throw new RuleException(e.getMessage());
        } catch (IOException e) {
            throw new RuleException(e.getMessage());
        }
    }



    public LoginAccountBinding login(LoginModel model) throws RuleException {
        Gson gson = new GsonBuilder().create();
        String _JSON = postLogin(model);
        return gson.fromJson(_JSON, LoginAccountBinding.class);
    }

    public ObtainLocalAccessTokenBinding obtainAccessToken(String provider, String externalAccessToken) throws RuleException {
        String _JSON = getJSON("api/account/ObtainLocalAccessToken?provider=" + provider + "&externalAccessToken=" + externalAccessToken, null);
        return new GsonBuilder().create().fromJson(_JSON, ObtainLocalAccessTokenBinding.class);
    }

    public LoginAccountBinding registerExternal(RegisterExternalBinding registerExternalBinding) throws RuleException {
        Gson gson = new GsonBuilder().create();
        String _JSON = postJSON("api/account/registerexternal", gson.toJson(registerExternalBinding), null);
        return gson.fromJson(_JSON, LoginAccountBinding.class);
    }

    public LoginAccountBinding register(RegisterModel model) throws RuleException {
        Gson gson = new GsonBuilder().create();
        postJSON("api/account/register", gson.toJson(model), null);
        return login(new LoginModel(model.getEmail(), model.getPassword()));
    }
}
