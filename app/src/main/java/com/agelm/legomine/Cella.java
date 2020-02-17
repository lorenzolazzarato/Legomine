package com.agelm.legomine;

import android.graphics.Bitmap;

import java.io.Serializable;

import it.unive.dais.legodroid.lib.plugs.TachoMotor;

public class Cella implements Serializable {
    private int x,y;
    private ComMine c;
    private boolean ball=false, visita=false;
    private char colore;
    private Bitmap b;

    public Cella(int x, int y, ComMine c){
        this.x=x;
        this.y=y;
        this.c=c;
    }

    public boolean vai(boolean ball, TachoMotor sx, TachoMotor dx, int max){
        int cont=0;
        try {
            sx.setPower(50);
            dx.setPower(50);
            sx.start();
            dx.start();

            while(c.getRadius()<max && cont<100){
                Thread.sleep(10);
                cont++;
            }

            sx.stop();
            dx.stop();
        }catch (Exception e){}

        if(cont==100)
            return false;
        return true;
    }

    public void indietro(TachoMotor sx, TachoMotor dx){
        try {
            sx.setPower(-50);
            dx.setPower(-50);
            sx.start();
            dx.start();

            Thread.sleep(1000);

            sx.stop();
            dx.stop();
        }catch (Exception e){}
    }

    public void gira(boolean ball, TachoMotor dx, TachoMotor sx){
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

    public void setBall(){
        ball=true;
    }

    public boolean isBall(){
        return ball;
    }

    public void setVisita(){
        visita=true;
    }

    public boolean isVisita(){
        return visita;
    }

    public void setColore(char c){
        this.colore=c;
    }

    public char getColore(){
        return colore;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public void setB(Bitmap b){
        this.b=b;
    }

    public Bitmap getB(){
        return b;
    }
}
