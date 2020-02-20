package com.agelm.legomine;

import android.graphics.Bitmap;

import java.io.Serializable;

import it.unive.dais.legodroid.lib.plugs.TachoMotor;

public class Cella{
    private int x,y;
    private ComMine c;
    private boolean ball=false, visita=false;
    private String colore;
    private Bitmap b;

    public Cella(int x, int y, ComMine c){
        this.x=x;
        this.y=y;
        this.c=c;
    }

    public boolean vai(TachoMotor sx, TachoMotor dx, int max){
        int cont=0;
        boolean flag=false;
        c.setTempo(c.getTempo()+2500);

        try {
            sx.setPower(25);
            dx.setPower(25);
            sx.start();
            dx.start();


            while(cont<500){
                Thread.sleep(5);
                cont++;
                if(c.getRadius()<=max+4 && c.getRadius()>=max-30)
                    flag=true;
            }

            sx.stop();
            dx.stop();
        }catch (Exception e){}

        return flag;
    }

    public void indietro(TachoMotor sx, TachoMotor dx){
        c.setTempo(c.getTempo()+2600);
        try {
            sx.setPower(-25);
            dx.setPower(-25);
            sx.start();
            dx.start();

            Thread.sleep(2600);

            sx.stop();
            dx.stop();
        }catch (Exception e){}
    }

    public void gira(boolean ball, TachoMotor dx, TachoMotor sx){
        if(!ball){
            try{
                c.setTempo(c.getTempo()+1300);

                sx.setPower(25);
                dx.setPower(-10);
                sx.start();
                dx.start();

                Thread.sleep(1300);

                sx.stop();
                dx.stop();
            }catch (Exception e){}
        }else{
            try{
                c.setTempo(c.getTempo()+1300);

                sx.setPower(30);
                dx.setPower(-20);
                sx.start();
                dx.start();

                Thread.sleep(1300);

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

    public void setColore(String c){
        this.colore=c;
    }

    public String getColore(){
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
