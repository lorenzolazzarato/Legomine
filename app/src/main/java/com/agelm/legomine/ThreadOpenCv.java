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
    private ComMine c;
    private boolean set=false;

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

    public boolean getSet(){
        return set;
    }
}
