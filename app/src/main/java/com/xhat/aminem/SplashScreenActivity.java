package com.xhat.aminem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // hide the action bar
        getSupportActionBar().hide();

        new Handler().postDelayed(new Runnable() {
            // Using handler with postDelayed called runnable run method
            @Override
            public void run() {
                Intent i = new Intent(SplashScreenActivity.this, IntroActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, 5*1000); // wait for 5 seconds
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
