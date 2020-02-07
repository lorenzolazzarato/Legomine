package com.agelm.legomine;

import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.LinearLayout;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import java.util.ArrayList;

public class ThreadOpenCv extends Thread{

    private CameraBridgeViewBase mOpenCvCameraView;
    private String TAG;
    private ComMine c;

    public ThreadOpenCv(CameraBridgeViewBase open, String TAG, ComMine c){
        mOpenCvCameraView = open;
        this.TAG=TAG;
        this.c=c;
    }

    public void run(){

        final Ball[] target = new Ball[1];

        /*Configura l'elemento della camera*/
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
                /*ordina le palle per dimensione del raggio, in modo da andare a prendere prima quella più vicina (si spera)
                Comparator<Ball> ballComparator = (ball1, ball2) -> (int)(ball1.radius - ball2.radius);
                f.sort(ballComparator);
                Collections.reverse(f);
                target[0] = f.get(0); // la palla target, quella più vicina*/

                c.setRadius(1);
                mOpenCvCameraView.disableView();
                return frame;
            }
        });



        /*Abilita la visualizzazione dell'immagine sullo schermo*/
        mOpenCvCameraView.enableView();
    }
}
