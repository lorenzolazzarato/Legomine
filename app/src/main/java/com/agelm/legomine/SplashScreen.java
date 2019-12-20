package com.agelm.legomine;

import android.content.Intent;
import android.os.Handler;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;


public class SplashScreen extends Activity { // prova123

    int SPLASH_TIME_OUT = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        ImageView iv= (ImageView) findViewById(R.id.imageView);
        iv.setImageResource(R.drawable.logo_legomine);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
