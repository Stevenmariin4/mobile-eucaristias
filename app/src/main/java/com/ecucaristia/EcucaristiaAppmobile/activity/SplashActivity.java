package com.ecucaristia.EcucaristiaAppmobile.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.ecucaristia.EcucaristiaAppmobile.MainActivity;
import com.ecucaristia.EcucaristiaAppmobile.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent inten = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(inten);
                finish();
            }
        }, 4000);
    }
}
