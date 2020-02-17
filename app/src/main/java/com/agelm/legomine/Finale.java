package com.agelm.legomine;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import it.unive.dais.legodroid.lib.plugs.LightSensor;

public class Finale extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_risultati);

        Intent in=getIntent();
        int x,y;

        TextView t = findViewById(R.id.numPuntiTW);
        t.setText(in.getIntExtra("punti",0));

        t = findViewById(R.id.numTempoTW);
        t.setText(""+in.getDoubleExtra("tempo",0));

        t = findViewById(R.id.numMineTW);
        t.setText(in.getIntExtra("mine",0));

        x=in.getIntExtra("x",0);
        y=in.getIntExtra("y",0);

        GridLayout g=findViewById(R.id.grid);
        g.setColumnCount(12);
        g.setRowCount(12);
        Button b;

        int cont1=9, cont2=1;

        for(int i=0; i<x*y; i++){
            b=new Button(this);
            b.setId(cont1-cont2);
            if(cont1==cont2){
                cont1+=9;
                cont2=1;
            }else
                cont2++;

            b.setText(""+i);
            b.setClickable(false);
            g.addView(b);
        }

        LinearLayout l=findViewById(R.id.layoutFoto);

        for(int i=0;i<x*y;i++){
            Cella c = (Cella)in.getSerializableExtra(""+i);
            if(c.isBall()){
                b=findViewById(x*c.getY()+c.getX());
                if(c.getColore()=='b')
                    b.setBackgroundColor(Color.BLUE);
                else if(c.getColore()=='r')
                    b.setBackgroundColor(Color.RED);
                else
                    b.setBackgroundColor(Color.YELLOW);

                ImageView im=new ImageView(this);
                im.setImageBitmap(c.getB());

                l.addView(im);
            }
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
