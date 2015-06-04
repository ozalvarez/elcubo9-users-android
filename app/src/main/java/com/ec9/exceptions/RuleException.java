package com.ec9.exceptions;

import android.app.Activity;
import android.text.TextUtils;

import com.ec9.BaseActivity;
import com.ec9.bll.AccountBLL;
import com.ec9.data.LoginAccountBinding;
import com.ec9.data.account.ObtainLocalAccessTokenBinding;
import com.facebook.Session;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

/**
 * Created by Oswaldo on 11/16/2014.
 */

public class RuleException extends Exception {
    public String RuleExceptionMessage;
    public String RuleExceptionCode;
    private RuleExceptionType Type;

    public RuleException(String messageJson) {
        super(messageJson);
        try {
            RuleExceptionMessage _RuleExceptionMessage = new GsonBuilder().create().fromJson(messageJson, RuleExceptionMessage.class);
            if (_RuleExceptionMessage == null)
                RuleExceptionMessage = messageJson;
            else {
                RuleExceptionMessage = _RuleExceptionMessage.getMessage();
                RuleExceptionCode = _RuleExceptionMessage.getCode();
            }
        } catch (JsonSyntaxException e) {
            RuleExceptionMessage = e.getMessage();
            RuleExceptionCode = "1x000";
        }
    }

    public RuleException(RuleExceptionType type) {
        Type = type;
    }

    public RuleExceptionMessage getRuleExceptionMessage(Activity activity) {
        return new RuleExceptionMessage(RuleExceptionMessage, RuleExceptionCode);
    }

    public Boolean is401() {
        if (Type == Type.E401)
            return true;
        return false;
    }

}
