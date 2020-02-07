package com.agelm.legomine;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Finale extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_risultati);

        TextView t = findViewById(R.id.numPuntiTW);
        t.setText(getIntent().getIntExtra("radius",0));
    }
}
