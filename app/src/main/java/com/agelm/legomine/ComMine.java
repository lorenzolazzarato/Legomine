package com.agelm.legomine;

import android.graphics.Bitmap;

public class ComMine {
    private double radius=0, time=0;
    private String c;
    private Bitmap b;
    private double tempo;

    public ComMine(){
        tempo=0;
    }

    public double getRadius(){
        return radius;
    }

    public void setRadius(float radius){
        this.radius=radius;
    }

    public void setC(String c){
        this.c=c;
    }

    public String getC(){
        return c;
    }

    public void setTime(double time){
        this.time=time;
    }

    public double getTime(){
        return time;
    }

    public void setB(Bitmap b){
        this.b=b;
    }

    public Bitmap getB(){
        return b;
    }

    public void  setTempo(Double t){
        tempo=t;
    }

    public Double getTempo(){
        return tempo;
    }
}
