package com.agelm.legomine;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import it.unive.dais.legodroid.lib.EV3;
import it.unive.dais.legodroid.lib.comm.BluetoothConnection;

public class ProvaMain extends AppCompatActivity {

    private EV3 ev3=null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        try {
            Thread.sleep(2000);
        }catch (Exception e){

        }
        setContentView(R.layout.activity_setting);
    }

    /*Creazione connessione con il robot*/
    public void connetti(View view){
        try {
            BluetoothConnection conn = new BluetoothConnection("AGELM");
            BluetoothConnection.BluetoothChannel channel = conn.connect();
            ev3 = new EV3(channel);
            Button b = findViewById(R.id.connettiB);
            b.setClickable(false);
            b = findViewById(R.id.avvioB);
            b.setClickable(true);
        } catch (IOException e) {
        }
    }

    /*Avvio delle prove e controllo inserimento valori nelle EditBox*/
    public void avvio(View view){

        EditText dx, dy, px, py;
        dx = findViewById(R.id.dimXTB);
        dy = findViewById(R.id.dimYTB);
        px = findViewById(R.id.posXTB);
        py = findViewById(R.id.posYTB);




        if(findViewById(R.id.prova1RB).isSelected()){
            EditText n = findViewById(R.id.numPallineTB);
            //Prova1 p = new Prova1(ev3);
        }
        else if(findViewById(R.id.prova2RB).isSelected()){
            Prova2 p = new Prova2(ev3);
        }
        else{
            EditText c = findViewById(R.id.chiaveTB);
            Prova3 p = new Prova3  (ev3);
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
