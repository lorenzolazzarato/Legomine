package com.agelm.legomine;

import android.graphics.Bitmap;

public class ComMine {
    private double radius=0, time=0;
    private char c;
    private Bitmap b;

    public ComMine(){}

    public double getRadius(){
        return radius;
    }

    public void setRadius(float radius){
        this.radius=radius;
    }

    public void setC(char c){
        this.c=c;
    }

    public char getC(){
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
}
