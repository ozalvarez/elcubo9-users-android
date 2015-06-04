package com.ec9;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.ec9.data.LoginAccountBinding;
import com.facebook.Session;

/**
 * Created by Oswaldo on 11/17/2014.
 */
public class BaseActivity extends Activity {
    public final static String TOKEN = "com.oswaldo.myfirstapp.TOKEN";
    public final static String REFRESH_TOKEN = "com.oswaldo.myfirstapp.REFRESH_TOKEN";
    private static LoginAccountBinding token = new LoginAccountBinding();


    public static LoginAccountBinding getToken(Activity activity) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        token.setAccess_token(sharedPref.getString(TOKEN, null));
        token.setRefresh_token(sharedPref.getString(REFRESH_TOKEN, null));
        return token;
    }

    public static void addToken(String token, String refreshToken, Activity activity) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(TOKEN, token);
        if (!TextUtils.isEmpty(refreshToken))
            editor.putString(REFRESH_TOKEN, refreshToken);
        editor.commit();
    }

    public static void StartIntern(Activity activity) {
        Intent intent = new Intent(activity, InternActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);
        activity.finish();
    }

    public static void signOff(Activity activity) {
        //CLEAR DATA
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(TOKEN);
        editor.remove(REFRESH_TOKEN);
        editor.commit();

        //CLEAR FACEBOOK SESSION
        Session _Session = getSessionFB(activity);
        if (_Session != null)
            _Session.closeAndClearTokenInformation();

        //OPEN MAIN-HOME
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }
    public static Session getSessionFB(Activity activity){
        Session _Session = Session.getActiveSession();
        if(_Session==null && activity!=null){
            _Session= Session.openActiveSessionFromCache(activity);
        }
        return _Session;
    }
}
