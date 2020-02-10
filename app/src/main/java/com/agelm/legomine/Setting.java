package com.agelm.legomine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import it.unive.dais.legodroid.lib.EV3;
import it.unive.dais.legodroid.lib.comm.BluetoothConnection;

public class Setting extends AppCompatActivity {

    private EV3 ev3=null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Button b=findViewById(R.id.avvioB);
        b.setClickable(false);
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

        } catch (Exception e) {
        }
    }

    /*Avvio delle prove e controllo inserimento valori nelle EditBox*/
    public void avvio(View view){

        EditText dx, dy, px, py;
        dx = findViewById(R.id.dimXTB);
        dy = findViewById(R.id.dimYTB);
        px = findViewById(R.id.posXTB);
        py = findViewById(R.id.posYTB);

        if(contrText(dx) && contrText(dy) && contrText(px) && contrText(py)){
            if(((RadioButton)findViewById(R.id.prova1RB)).isChecked()){
                EditText n = findViewById(R.id.numPallineTB);
                if(contrText(n)){
                    int dimx = parse(dx), dimy = parse(dy), posx = parse(px), posy = parse(py), num = parse(n);
                    Intent intent = new Intent(this, Prova1.class);
                    intent.putExtra("dimx",dimx);
                    intent.putExtra("dimy",dimy);
                    intent.putExtra("posx",posx);
                    intent.putExtra("posy",posy);
                    intent.putExtra("num",num);
                    intent.putExtra("Ev3",ev3);
                    startActivity(intent);
                }
            }
            else if(((RadioButton)findViewById(R.id.prova2RB)).isChecked()){
                Prova2 p = new Prova2(ev3);
            }
            else{
                EditText c = findViewById(R.id.chiaveTB);
                Prova3 p = new Prova3  (ev3);
            }
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

    private int parse(EditText e){
        int i = Integer.parseInt(e.getText().toString());
        return i;
    }

    private boolean contrText(EditText e){
        if(e.getText().toString().compareTo("")==0) {
            e.setError("Campo obbligatorio");
            return false;
        }
        try{
            int i = Integer.parseInt(e.getText().toString());
        }catch (Exception ex){
            e.setError("Il testo inserito deve essere un numero intero positivo");
            return false;
        }

        return true;
    }
}
