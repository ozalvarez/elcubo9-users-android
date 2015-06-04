package com.ec9.exceptions;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by Oswaldo on 11/27/2014.
 */
public class RuleExceptionMessage {
    public final static String E401 = "1x001";
    private String Message;
    private String Code;

    public RuleExceptionMessage() {
    }

    public RuleExceptionMessage(String message, String code) {
        this.Message = message;
        this.Code = code;
    }

    public void process(Activity activity) {
        if (!TextUtils.isEmpty(getMessage()))
            Toast.makeText(activity, getMessage(), Toast.LENGTH_SHORT).show();
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }
}
