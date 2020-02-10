package com.agelm.legomine;

public class ComMine {
    private double radius=0;
    private boolean set=false;

    public ComMine(){}

    public double getRadius(){
        return radius;
    }

    public void setRadius(float radius){
        this.radius=radius;
    }

    public void setSet(boolean set){
        this.set=set;
    }

    public boolean getSet(){
        return set;
    }
}
