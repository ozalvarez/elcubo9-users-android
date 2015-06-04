package com.ec9.bll;

import android.app.Activity;
import android.text.TextUtils;

import com.ec9.BaseActivity;
import com.ec9.data.LoginAccountBinding;
import com.ec9.data.account.ObtainLocalAccessTokenBinding;
import com.ec9.exceptions.RuleException;
import com.ec9.exceptions.RuleExceptionType;
import com.facebook.Session;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oswaldo on 11/17/2014.
 */
public class BaseBLL {

    public String URL = "http://elcubo9-api.azurewebsites.net/";
    //private final String URL="http://ozw2s:26382/";


    protected String getJSON(HttpResponse response) throws RuleException {
        try {
            HttpEntity he = response.getEntity();
            int statusCode = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            if (statusCode == 200) {
                return EntityUtils.toString(entity);
            } else {
                if (statusCode == 500)
                    throw new RuleException("500");
                if (statusCode == 400)
                    throw new RuleException(EntityUtils.toString(entity));
                if (statusCode == 401)
                    throw new RuleException(RuleExceptionType.E401);
                else
                    throw new RuleException("Other Code Exception");
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuleException(e.getMessage());
        } catch (ClientProtocolException e) {
            throw new RuleException(e.getMessage());
        } catch (IOException e) {
            throw new RuleException(e.getMessage());
        }
    }

    public String getJSON(String apiURI, Activity activity) throws RuleException {
        HttpClient httpClient = new DefaultHttpClient();
        try {

            HttpGet httpGet = new HttpGet(URL + apiURI);
            httpGet.addHeader("Content-Type", "application/json");
            httpGet.addHeader("Accept", "JSON");
            if (activity != null) {
                LoginAccountBinding token = BaseActivity.getToken(activity);
                if (!TextUtils.isEmpty(token.getAccess_token()))
                    httpGet.addHeader("Authorization", "Bearer " + token.getAccess_token());
            }
            return getJSON(httpClient.execute(httpGet));
        } catch (IOException e) {
            throw new RuleException(e.getMessage());
        } catch (RuleException e) {
            if (e.is401()) {
                if (refreshToken(activity))
                    return getJSON(apiURI, activity);
            }
            throw e;
        }
    }

    public String postJSON(String apiURI, String jsonParams, Activity activity) throws RuleException {
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(URL + apiURI);
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.addHeader("Accept", "JSON");
            if (activity != null) {
                LoginAccountBinding token = BaseActivity.getToken(activity);
                if (!TextUtils.isEmpty(token.getAccess_token()))
                    httpPost.addHeader("Authorization", "Bearer " + token.getAccess_token());
            }
            if (!jsonParams.isEmpty()) {
                httpPost.setEntity(new StringEntity(jsonParams, "UTF-8"));
            }
            return getJSON(httpClient.execute(httpPost));
        } catch (IOException e) {
            throw new RuleException(e.getMessage());
        } catch (RuleException e) {
            if (e.is401()) {
                if (refreshToken(activity)) ;
                return postJSON(apiURI, jsonParams, activity);
            }
            throw e;
        }
    }

    public Integer postInteger(String apiURI, String jsonParams, Activity activity) throws RuleException {
        String entity = postJSON(apiURI, jsonParams, activity);
        return Integer.parseInt(entity);
    }

    private String postRefreshToken(String refreshToken) throws RuleException {

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("grant_type", "refresh_token"));
        params.add(new BasicNameValuePair("refresh_token", refreshToken));
        params.add(new BasicNameValuePair("client_id", "android_app"));
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

    private Boolean refreshToken(Activity activity) throws RuleException {
        LoginAccountBinding loginModel = BaseActivity.getToken(activity);
        Session _Session = BaseActivity.getSessionFB(activity);
        if (_Session != null && _Session.isOpened()) {
            ObtainLocalAccessTokenBinding oatModel = new AccountBLL().obtainAccessToken("Facebook", _Session.getAccessToken());
            if (!TextUtils.isEmpty(oatModel.getAccess_token())) {
                BaseActivity.addToken(oatModel.getAccess_token(), null, activity);
                return true;
            } else {
                BaseActivity.signOff(activity);
                return false;
            }
        } else if (!TextUtils.isEmpty(loginModel.getRefresh_token())) {
            String _JSON = postRefreshToken(loginModel.getRefresh_token());
            LoginAccountBinding newLoginModel = new GsonBuilder().create().fromJson(_JSON, LoginAccountBinding.class);
            if (!TextUtils.isEmpty(newLoginModel.getAccess_token())) {
                BaseActivity.addToken(newLoginModel.getAccess_token(), newLoginModel.getRefresh_token(), activity);
                return true;
            } else {
                BaseActivity.signOff(activity);
                return false;
            }
        } else {
            BaseActivity.signOff(activity);
            return false;
        }
    }
}
