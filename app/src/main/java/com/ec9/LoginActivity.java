package com.ec9;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ec9.bll.AccountBLL;
import com.ec9.data.LoginAccountBinding;
import com.ec9.data.account.LoginModel;
import com.ec9.data.account.ObtainLocalAccessTokenBinding;
import com.ec9.exceptions.RuleException;
import com.ec9.exceptions.RuleExceptionMessage;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;


public class LoginActivity extends FragmentActivity {
    private Activity ThisActivity = LoginActivity.this;

    private LoginButton btnLoginFb;
    private UiLifecycleHelper uiHelper;
    private EditText etEmail;
    private EditText etPassword;

    private String Provider;

    public Session FBSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        uiHelper = new UiLifecycleHelper(this, statusCallback);
        uiHelper.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        btnLoginFb = (LoginButton) findViewById(R.id.btnLoginFb);
        btnLoginFb.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                if (user != null) {
                    Provider = "Facebook";
                    new HttpObtainLocalAccessToken().execute();
                } else {
                }
            }
        });

        Button btlLogin = (Button) findViewById(R.id.btnLogin);
        btlLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HttpLogin().execute();
            }
        });

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);

    }

    private Session.StatusCallback statusCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state,
                         Exception exception) {
            if (state.isOpened()) {
                FBSession = session;
            } else if (state.isClosed()) {

            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        uiHelper.onSaveInstanceState(savedState);
    }


    private void StartAssociate() {
        Intent intent = new Intent(ThisActivity, AssociateActivity.class);
        intent.putExtra("Provider", Provider);
        intent.putExtra("ExternalToken", FBSession.getAccessToken());
        startActivity(intent);
        LoginActivity.this.finish();
    }

    public class HttpObtainLocalAccessToken extends AsyncTask<Void, Void, ObtainLocalAccessTokenBinding> {
        private RuleExceptionMessage ruleExceptionMessage;
        private ProgressDialog dialog;

        public HttpObtainLocalAccessToken() {

        }

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(ThisActivity, getResources().getString(R.string.loggingIn), getResources().getString(R.string.loading), true, false);
        }

        @Override
        protected ObtainLocalAccessTokenBinding doInBackground(Void... params) {
            try {
                return new AccountBLL().obtainAccessToken(Provider, FBSession.getAccessToken());
            } catch (RuleException e) {
                ruleExceptionMessage = e.getRuleExceptionMessage(ThisActivity);
                return null;
            }

        }

        @Override
        protected void onPostExecute(ObtainLocalAccessTokenBinding _ObtainLocalAccessTokenBinding) {
            if (ruleExceptionMessage != null) {
                if (ruleExceptionMessage.getCode().equals("0x000")) {
                    StartAssociate();
                } else
                    Toast.makeText(LoginActivity.this, ruleExceptionMessage.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                BaseActivity.addToken(_ObtainLocalAccessTokenBinding.getAccess_token(), null, LoginActivity.this);
                BaseActivity.StartIntern(ThisActivity);
            }
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            try {
                HttpObtainLocalAccessToken.this.finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    public class HttpLogin extends AsyncTask<Void, Void, LoginAccountBinding> {
        private RuleExceptionMessage ruleExceptionMessage;
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(ThisActivity, getResources().getString(R.string.loggingIn), getResources().getString(R.string.loading), true, false);
        }

        @Override
        protected LoginAccountBinding doInBackground(Void... params) {

            try {
                return new AccountBLL().login(new LoginModel(etEmail.getText().toString(), etPassword.getText().toString()));
            } catch (RuleException e) {
                ruleExceptionMessage = e.getRuleExceptionMessage(ThisActivity);
                return null;
            }
        }

        @Override
        protected void onPostExecute(LoginAccountBinding loginAccountBinding) {
            if (ruleExceptionMessage != null) {
                Toast.makeText(ThisActivity, getResources().getString(R.string.logInFailed), Toast.LENGTH_SHORT).show();
            } else {
                BaseActivity.addToken(loginAccountBinding.getAccess_token(), loginAccountBinding.getRefresh_token(), ThisActivity);
                BaseActivity.StartIntern(ThisActivity);
            }
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            try {
                HttpLogin.this.finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }
}

