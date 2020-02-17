package com.agelm.legomine;

public class Timer extends Thread{
    private ComMine c;

    public Timer(ComMine c){
        this.c=c;
    }

    public void run(){
        while(true){
            try{
                Thread.sleep(10);
                c.setTime(c.getTime()+0.01);
            }catch (Exception e){

            }
        }
    }
}
