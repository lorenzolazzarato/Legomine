package com.agelm.legomine;

import org.opencv.android.CameraBridgeViewBase;

public class ThreadOpenCv extends Thread{

    private CameraBridgeViewBase mOpenCvCameraView;
    private ComMine c;
    private Prova1 p;

    public ThreadOpenCv(CameraBridgeViewBase open, ComMine c, Prova1 p){
        mOpenCvCameraView = open;
        this.c=c;
        this.p=p;
    }

    public void run(){
        try {
            Thread.sleep(5000);
        }catch (Exception e){

        }

        mOpenCvCameraView.disableView();
        try {
            Thread.sleep(2000);
        }catch (Exception e){

        }
        p.changeIntent(c.getRadius());
    }
}
