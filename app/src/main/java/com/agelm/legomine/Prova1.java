package com.agelm.legomine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;
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

        ev3 = (EV3) getIntent().getSerializableExtra("Ev3");

        int x = getIntent().getIntExtra("dimx",12),y  = getIntent().getIntExtra("dimy",12);

        matrice = new ArrayList<>(Collections.nCopies(x*y,0));

        posx = getIntent().getIntExtra("posx",0);
        posy = getIntent().getIntExtra("posy",0);
        num = getIntent().getIntExtra("num",0);




        final Ball[] target = new Ball[1];

        /*Imposta lo schermo a sempre acceso*/
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        /*Carica le librerie di OpenCV in maniera sincrona*/
        if (!OpenCVLoader.initDebug()) {
            Log.e(TAG, "Unable to load OpenCV");
        } else {
            Log.d(TAG, "OpenCV loaded");
        }

        /*Configura l'elemento della camera*/
        mOpenCvCameraView = findViewById(R.id.OpenCvView);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setMaxFrameSize(640, 480);
        mOpenCvCameraView.setCvCameraViewListener(new CameraBridgeViewBase.CvCameraViewListener2() {
            @Override
            public void onCameraViewStarted(int width, int height) {
                Log.d(TAG, "Camera Started");
            }

            @Override
            public void onCameraViewStopped() {
                Log.d(TAG, "Camera Stopped");
            }

            /*Viene eseguito ad ogni frame, con inputFrame l'immagine corrente*/
            @Override
            public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
                /*Salva il frame corrente su un oggetto Mat, ossia una matrice bitmap*/
                Mat frame = inputFrame.rgba();

                BallFinder ballFinder = new BallFinder(frame, true);
                ballFinder.setViewRatio(0.0f);
                ballFinder.setMinArea(2000);
                ballFinder.setOrientation("portrait");
                ArrayList<Ball> f = ballFinder.findBalls();

                for (Ball b : f) {
                    Log.e("ball", String.format("X:%f Y:%f Rad:%f Col:%s", b.center.x, b.center.y, b.radius, b.color));

                }
                /*ordina le palle per dimensione del raggio, in modo da andare a prendere prima quella più vicina (si spera)*/
                Comparator<Ball> ballComparator = (ball1, ball2) -> (int)(ball1.radius - ball2.radius);
                f.sort(ballComparator);
                Collections.reverse(f);
                target[0] = f.get(0); // la palla target, quella più vicina

                mOpenCvCameraView.disableView();

                return frame;
            }
        });

        Intent intent = new Intent(this,Finale.class);
        intent.putExtra("radius",target[0].radius);

        /*Abilita la visualizzazione dell'immagine sullo schermo*/
        mOpenCvCameraView.enableView();

        /*recupera le palline*/
        while(true) { /*da definire fino a quando*/
            /*metto la palla al centro della visione del robot.*/
            if(target[0].center.y >= 240) {
                while(target[0].center.y > 240) {
                    /*gira il robot a sx*/
                    Prelude.trap(() -> {
                        ruota_dx.setPower(20);
                        ruota_dx.start();
                    });
                }
                Prelude.trap(() -> ruota_dx.stop());
            } else {
                while(target[0].center.y < 240) {
                    /*gira il robot a dx*/
                    Prelude.trap(() -> {
                        ruota_sx.setPower(20);
                        ruota_sx.start();
                    });
                }
                Prelude.trap(() -> ruota_sx.stop());
            }
            /*la palla è centrata, vai dritto*/
            /*come lo fermi quando arriva davanti alla palla?*/
            Prelude.trap(() -> {
                ruota_sx.setPower(50);
                ruota_dx.setPower(50);
                ruota_sx.start();
                ruota_dx.start();
            });

        }
    }

}
