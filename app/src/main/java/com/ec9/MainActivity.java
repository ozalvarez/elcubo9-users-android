package com.ec9;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends Activity {
    private Activity ThisActivity = MainActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThisActivity, LoginActivity.class);
                startActivity(intent);
            }
        });

        Button btnSignup = (Button) findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThisActivity, RegisterActivity.class);
                startActivity(intent);
            }
        });
        goIntern();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        goIntern();
    }

    private void goIntern() {
        if (!TextUtils.isEmpty(BaseActivity.getToken(ThisActivity).getAccess_token())) {
            Intent intent = new Intent(ThisActivity, InternActivity.class);
            startActivity(intent);
            ThisActivity.finish();
        }
    }
}
