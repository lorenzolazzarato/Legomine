package com.agelm.legomine;

import org.opencv.android.CameraBridgeViewBase;

import java.util.ArrayList;
import java.util.Collections;

import it.unive.dais.legodroid.lib.plugs.TachoMotor;

public class ThreadOpenCv extends Thread{

    private CameraBridgeViewBase mOpenCvCameraView;
    private ComMine c;
    private Prova1 p;
    private Robot r;
    private final int max=159;

    public ThreadOpenCv(CameraBridgeViewBase open, ComMine c, Prova1 p, Robot r){
        mOpenCvCameraView = open;
        this.c=c;
        this.p=p;
        this.r=r;
    }

    public void run(){
        TachoMotor dx=r.getRuota_dx(), sx=r.getRuota_sx(), pz=r.getPinza();
        boolean inCarico=false;
        int num=p.getNum();
        ArrayList<Integer> matrice = new ArrayList<>(Collections.nCopies(r.getDimx()*r.getDimy(),0));

        try {
            Thread.sleep(3000);

            svolta(inCarico,dx,sx);

            Thread.sleep(3000);
        }catch (Exception e){

        }

        while(num>0){
            startRobot(dx,sx);
            while(c.getRadius()<max){}

            stopRobot(dx,sx);
            try {
                Thread.sleep(500);
            }catch (Exception e){

            }

            chiudiPinza(pz);
            inCarico=true;

            svolta(inCarico,dx,sx);
            svolta(inCarico,dx,sx);

            startRobot(dx,sx);

            try {
                Thread.sleep(3000);
            }catch (Exception e){

            }

            stopRobot(dx,sx);

            num=0;

        }

        p.changeIntent(1.1);
    }

    public void startRobot(TachoMotor dx, TachoMotor sx){
        try{
            sx.setPower(50);
            dx.setPower(50);
            sx.start();
            dx.start();
        }catch (Exception e){}
    }

    public void stopRobot(TachoMotor dx, TachoMotor sx){
        try{
            sx.stop();
            dx.stop();
        }catch (Exception e){}
    }

    public void chiudiPinza(TachoMotor pinza){
        try{
            pinza.setPower(-50);
            pinza.start();
            Thread.sleep(500);
            pinza.stop();
        }catch (Exception e){}
    }

    private void svolta(boolean ball, TachoMotor dx, TachoMotor sx){
        if(ball){
            try{
                sx.setPower(75);
                dx.setPower(-40);
                sx.start();
                dx.start();

                Thread.sleep(400);

                sx.stop();
                dx.stop();
            }catch (Exception e){}
        }
        else{
            try{
                sx.setPower(50);
                dx.setPower(-30);
                sx.start();
                dx.start();

                Thread.sleep(500);

                sx.stop();
                dx.stop();
            }catch (Exception e){}
        }
    }

}
