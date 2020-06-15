package com.reload.petsstore.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;

import com.reload.petsstore.R;
import com.reload.petsstore.auth.login.LoginActivity;
import com.reload.petsstore.auth.signup.SignUpActivity;
import com.reload.petsstore.common.SessionMangment;
import com.reload.petsstore.homecategory.HomeActivity;

public class SplashActivity extends AppCompatActivity {
    SessionMangment mSessionMangment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mSessionMangment = new SessionMangment(this);
        splashTimer();
    }

    private void splashTimer() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (mSessionMangment.isLoggedIn()) {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));

                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));

                }
            }
        }, 3000);

    }
}
