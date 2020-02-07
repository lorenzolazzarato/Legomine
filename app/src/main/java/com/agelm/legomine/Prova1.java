package com.agelm.legomine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import it.unive.dais.legodroid.lib.EV3;
import it.unive.dais.legodroid.lib.plugs.TachoMotor;
import it.unive.dais.legodroid.lib.util.Prelude;

public class Prova1 extends AppCompatActivity {


    private EV3 ev3;
    private TachoMotor ruota_sx;
    private TachoMotor ruota_dx;
    private TachoMotor pinza;
    private int posx, posy, num;
    private ArrayList<Integer> matrice;

    private String TAG = "Prova 1";

    private CameraBridgeViewBase mOpenCvCameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prova1);

        /*ev3 = (EV3) getIntent().getSerializableExtra("Ev3");

        int x = getIntent().getIntExtra("dimx",12),y  = getIntent().getIntExtra("dimy",12);

        matrice = new ArrayList<>(Collections.nCopies(x*y,0));

        posx = getIntent().getIntExtra("posx",0);
        posy = getIntent().getIntExtra("posy",0);
        num = getIntent().getIntExtra("num",0);*/


        ComMine c = new ComMine();

        final Ball[] target = new Ball[1];

        /*Imposta lo schermo a sempre acceso*/
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        /*Carica le librerie di OpenCV in maniera sincrona*/
        if (!OpenCVLoader.initDebug()) {
            Log.e(TAG, "Unable to load OpenCV");
        } else {
            Log.d(TAG, "OpenCV loaded");
        }
        mOpenCvCameraView = findViewById(R.id.OpenCvView);
        Thread t = new ThreadOpenCv(mOpenCvCameraView, TAG, c);
        t.start();

        /*while(c.getRadius()==0);*/

        t.stop();
        changeIntent(c.getRadius());

        /*recupera le palline
        while(true) { //da definire fino a quando
            //metto la palla al centro della visione del robot.
            if(target[0].center.y >= 240) {
                while(target[0].center.y > 240) {
                    //gira il robot a sx
                    Prelude.trap(() -> {
                        ruota_dx.setPower(20);
                        ruota_dx.start();
                    });
                }
                Prelude.trap(() -> ruota_dx.stop());
            } else {
                while(target[0].center.y < 240) {
                    //gira il robot a dx
                    Prelude.trap(() -> {
                        ruota_sx.setPower(20);
                        ruota_sx.start();
                    });
                }
                Prelude.trap(() -> ruota_sx.stop());
            }
            //la palla è centrata, vai dritto
            //come lo fermi quando arriva davanti alla palla?
            Prelude.trap(() -> {
                ruota_sx.setPower(50);
                ruota_dx.setPower(50);
                ruota_sx.start();
                ruota_dx.start();
            });

        }*/

    }

    private void changeIntent(float radius){
        Intent intent = new Intent(this,Finale.class);
        intent.putExtra("radius",radius);
        startActivity(intent);
    }

}
