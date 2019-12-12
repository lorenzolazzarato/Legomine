package com.agelm.legomine;

import android.util.Log;
import android.widget.Button;

import org.opencv.android.OpenCVLoader;

import java.io.IOException;

import it.unive.dais.legodroid.lib.EV3;
import it.unive.dais.legodroid.lib.comm.BluetoothConnection;
import it.unive.dais.legodroid.lib.plugs.GyroSensor;
import it.unive.dais.legodroid.lib.plugs.TachoMotor;
import it.unive.dais.legodroid.lib.util.Prelude;

public class Function {

    private EV3 ev3;
    private TachoMotor ruota_dx;
    private TachoMotor ruota_sx;
    private TachoMotor pinza;
    private GyroSensor gyroSensor;

    public Function(){

    }

    public void giroscopio(){

    }

    public void movimentoSincrono(){

    }

    public void svolta(){

    }

    public void pinza(){

    }

    public void scattaFoto(){

    }

    public void trovaPalline(){
        if (!OpenCVLoader.initDebug()) {
            Log.e("AndroidIngSwOpenCV", "Unable to load OpenCV");
        } else {
            Log.d("AndroidIngSwOpenCV", "OpenCV loaded");
        }

    }

    public void coordinate(){

    }

    public void connessione(){


    }

    public void crittografia(){

    }
}
