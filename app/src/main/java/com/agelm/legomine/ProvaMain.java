package com.agelm.legomine;

import android.os.Bundle;

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
}
