package com.ec9.bll;

import android.app.Activity;

import com.ec9.BaseActivity;
import com.ec9.data.CustomerBinding;
import com.ec9.data.LoginAccountBinding;
import com.ec9.data.account.ObtainLocalAccessTokenBinding;
import com.ec9.exceptions.RuleException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oswaldo on 11/17/2014.
 */
public class CustomerBLL extends BaseBLL {

    private static List<CustomerBinding> customers;
    public static List<CustomerBinding> getCurrentCustomers() {
        return customers;
    }

    public static Boolean cutomerLoaded(){
        return !customers.isEmpty();
    }


    public List<CustomerBinding> getCustomers(Activity activity) throws RuleException {
        String _JSON=getJSON("api/customer", activity);
        return new Gson().fromJson(_JSON,new TypeToken<List<CustomerBinding>>(){}.getType());
    }
}
