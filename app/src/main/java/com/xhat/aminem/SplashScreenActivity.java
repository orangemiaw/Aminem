package com.xhat.aminem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreenActivity extends AppCompatActivity {
    Animation anim;
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // hide the action bar
        getSupportActionBar().hide();

        logo = findViewById(R.id.logo);

        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in); // Create the animation.
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(SplashScreenActivity.this, IntroActivity.class));
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        logo.startAnimation(anim);

//        new Handler().postDelayed(new Runnable() {
//            // Using handler with postDelayed called runnable run method
//            @Override
//            public void run() {
//                Intent i = new Intent(SplashScreenActivity.this, IntroActivity.class);
//                startActivity(i);
//
//                // close this activity
//                finish();
//            }
//        }, 5*1000); // wait for 5 seconds
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
