package com.agelm.legomine;

import org.opencv.android.CameraBridgeViewBase;

public class ThreadOpenCv extends Thread{

    private CameraBridgeViewBase mOpenCvCameraView;
    private ComMine c;

    public ThreadOpenCv(CameraBridgeViewBase open, ComMine c){
        mOpenCvCameraView = open;
        this.c=c;
    }

    public void run(){
        try {
            Thread.sleep(5000);
        }catch (Exception e){

        }

        c.setSet(true);

        mOpenCvCameraView.disableView();
    }
}
