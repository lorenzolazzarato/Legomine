package com.agelm.legomine;
import it.unive.dais.legodroid.lib.EV3;

public class Prova1{

    public Prova1(EV3 ev3){
        try{
            ev3.run(this::start);
        }catch (Exception e){

        }
    }

    private void start(EV3.Api api){

    }
}
