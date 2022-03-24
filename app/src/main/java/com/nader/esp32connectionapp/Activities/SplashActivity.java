package com.nader.esp32connectionapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.nader.esp32connectionapp.R;

public class SplashActivity extends AppCompatActivity {
    private final int splashTimer=3000;
    private Handler handler=new Handler();
    private Runnable runnable=new Runnable() {
        @Override
        public void run() {
            Intent i=new Intent(SplashActivity.this, SignInActivity.class);
            startActivity(i);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        handler.postAtTime(runnable,System.currentTimeMillis()+splashTimer);
        handler.postDelayed(runnable,splashTimer);
    }
}