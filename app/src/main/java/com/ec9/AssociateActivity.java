package com.ec9;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ec9.bll.AccountBLL;
import com.ec9.data.LoginAccountBinding;
import com.ec9.exceptions.RuleExceptionMessage;
import com.ec9.data.account.RegisterExternalBinding;
import com.ec9.exceptions.RuleException;


public class AssociateActivity extends Activity {
    private Activity ThisActivity = AssociateActivity.this;
    EditText etName;
    EditText etEmail;
    private String ExternalToken;
    private String Provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_associate);

        Intent intent = getIntent();
        Provider = intent.getStringExtra("Provider");
        ExternalToken = intent.getStringExtra("ExternalToken");

        TextView tvAssociate = (TextView) findViewById(R.id.tvAssociate);
        tvAssociate.setText( getResources().getString(R.string.loginWith)+ Provider);

        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);

        Button btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setText(getResources().getString(R.string.registerWith) + " " + Provider);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etEmail.getText().toString()) || !android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches())
                    Toast.makeText(ThisActivity , getResources().getString(R.string.emailInvalid), Toast.LENGTH_SHORT).show();
                else if (etName.getText().toString().equals(""))
                    Toast.makeText(ThisActivity , getResources().getString(R.string.nameRequired), Toast.LENGTH_SHORT).show();
                else
                    new HttpRegister().execute();
            }
        });


    }

    private void StartIntern() {
        Intent intent = new Intent(AssociateActivity.this, InternActivity.class);
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
            try {
                return new AccountBLL().registerExternal(new RegisterExternalBinding(etName.getText().toString(), etEmail.getText().toString(), Provider, ExternalToken));
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
                BaseActivity.addToken(loginAccountBinding.getAccess_token(),loginAccountBinding.getRefresh_token(), AssociateActivity.this);
                BaseActivity.StartIntern(ThisActivity);
            }
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

}
