package com.ec9;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ec9.bll.AccountBLL;
import com.ec9.data.LoginAccountBinding;
import com.ec9.exceptions.RuleExceptionMessage;
import com.ec9.data.account.ObtainLocalAccessTokenBinding;
import com.ec9.data.account.RegisterModel;
import com.ec9.exceptions.RuleException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;


public class RegisterActivity extends FragmentActivity {
    private Activity ThisActivity = RegisterActivity.this;
    private UiLifecycleHelper uiHelper;
    private Session FBSession;
    private LoginButton loginBtn;
    private String Provider;
    EditText etEmail;
    EditText etName;
    EditText etPassword;
    EditText etConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(this, statusCallback);
        uiHelper.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etName = (EditText) findViewById(R.id.etName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);

        loginBtn = (LoginButton) findViewById(R.id.fb_login_button);
        loginBtn.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                if (user != null) {
                    Provider = "Facebook";
                    new HttpObtainLocalAccessToken().execute();
                }
            }
        });
        Button btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etEmail.getText().toString()) || !android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches())
                    Toast.makeText(ThisActivity, getResources().getString(R.string.emailInvalid), Toast.LENGTH_SHORT).show();
                else if (etName.getText().toString().equals(""))
                    Toast.makeText(ThisActivity, getResources().getString(R.string.nameRequired), Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(etPassword.getText().toString()))
                    Toast.makeText(ThisActivity, getResources().getString(R.string.passwordRequired), Toast.LENGTH_SHORT).show();
                else if (!etConfirmPassword.getText().toString().equals(etPassword.getText().toString()))
                    Toast.makeText(ThisActivity, getResources().getString(R.string.confirmPasswordInvalid), Toast.LENGTH_SHORT).show();
                else if (etConfirmPassword.getText().toString().length() < 6)
                    Toast.makeText(ThisActivity, getResources().getString(R.string.passwordLength), Toast.LENGTH_SHORT).show();
                else
                    new HttpRegister().execute();
            }
        });
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
        ThisActivity.finish();
    }

    public class HttpRegister extends AsyncTask<Void, Void, LoginAccountBinding> {
        private RuleExceptionMessage ruleExceptionMessage;
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(ThisActivity, getResources().getString(R.string.signingIn), getResources().getString(R.string.loading), true, false);
        }

        @Override
        protected LoginAccountBinding doInBackground(Void... params) {
            String _Email = etEmail.getText().toString();
            String _Name = etName.getText().toString();
            String _Password = etPassword.getText().toString();
            String _ConfirmPassword = etConfirmPassword.getText().toString();
            try {
                return new AccountBLL().register(new RegisterModel(_Name, _Email, _Password, _ConfirmPassword));
            } catch (RuleException e) {
                ruleExceptionMessage = e.getRuleExceptionMessage(ThisActivity);
                return null;
            }
        }

        @Override
        protected void onPostExecute(LoginAccountBinding loginAccountBinding) {
            if (ruleExceptionMessage != null) {
                Toast.makeText(ThisActivity, ruleExceptionMessage.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                BaseActivity.addToken(loginAccountBinding.getAccess_token(),loginAccountBinding.getRefresh_token(), ThisActivity);
                BaseActivity.StartIntern(ThisActivity);
            }
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
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
                    Toast.makeText(ThisActivity, ruleExceptionMessage.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                BaseActivity.addToken(_ObtainLocalAccessTokenBinding.getAccess_token(),null, ThisActivity);
                BaseActivity.StartIntern(ThisActivity);
            }
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }
}
