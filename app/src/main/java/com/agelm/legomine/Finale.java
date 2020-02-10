package com.agelm.legomine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Finale extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_risultati);

        TextView t = findViewById(R.id.numPuntiTW);
        t.setText(""+getIntent().getDoubleExtra("radius",0));

        GridLayout g=findViewById(R.id.grid);
        g.setColumnCount(12);
        Button b;

        for(int i=0; i<144; i++){
            b=new Button(this);
            b.setWidth(100);
            b.setHeight(100);
            b.setId(144-i);
            b.setText(""+i);
            g.addView(b);
        }
    }

    public void setting(View view){
        Intent intent = new Intent(this,Setting.class);
        startActivity(intent);
    }

    public void esci(View view){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
}
