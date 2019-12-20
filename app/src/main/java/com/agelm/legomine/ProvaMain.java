package com.agelm.legomine;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import it.unive.dais.legodroid.lib.EV3;
import it.unive.dais.legodroid.lib.comm.BluetoothConnection;

public class ProvaMain extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        try {
            Thread.sleep(2000);
        }catch (Exception e){

        }

        setContentView(R.layout.activity_setting);

        EV3 ev3;

        try {
            BluetoothConnection conn = new BluetoothConnection("AGELM");
            BluetoothConnection.BluetoothChannel channel = conn.connect();
            ev3 = new EV3(channel);

            if(findViewById(R.id.prova1RB).isSelected()){
                Prova1 p = new Prova1(ev3);
            }
            else if(findViewById(R.id.prova2RB).isSelected()){
                Prova2 p = new Prova2(ev3);
            }
            else{
                Prova3 p = new Prova3  (ev3);
            }
        } catch (IOException e) {
        }

    }


    public void settingProva1(View view){
        TextView t = findViewById(R.id.numeroPallineTW);
        t.setTextColor(Color.parseColor("#000000"));

        t = findViewById(R.id.criptazioneTW);
        t.setTextColor(Color.parseColor("#3C4752"));

        EditText e = findViewById(R.id.numPallineTB);
        e.setEnabled(true);

        e = findViewById(R.id.chiaveTB);
        e.setEnabled(false);
    }

    public void settingProva2(View view){
        TextView t = findViewById(R.id.numeroPallineTW);
        t.setTextColor(Color.parseColor("#3C4752"));

        t = findViewById(R.id.criptazioneTW);
        t.setTextColor(Color.parseColor("#3C4752"));

        EditText e = findViewById(R.id.numPallineTB);
        e.setEnabled(false);

        e = findViewById(R.id.chiaveTB);
        e.setEnabled(false);
    }

    public void settingProva3(View view){
        TextView t = findViewById(R.id.numeroPallineTW);
        t.setTextColor(Color.parseColor("#3C4752"));

        t = findViewById(R.id.criptazioneTW);
        t.setTextColor(Color.parseColor("#000000"));

        EditText e = findViewById(R.id.numPallineTB);
        e.setEnabled(false);

        e = findViewById(R.id.chiaveTB);
        e.setEnabled(true);
    }
}
