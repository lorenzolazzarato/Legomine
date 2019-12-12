package com.agelm.legomine;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ProvaMain extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        try {
            Thread.sleep(2000);
        }catch (Exception e){

        }

        setContentView(R.layout.activity_setting);
    }
}
