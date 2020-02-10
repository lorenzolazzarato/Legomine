package com.agelm.legomine;

import java.io.IOException;
import java.io.Serializable;

import it.unive.dais.legodroid.lib.EV3;
import it.unive.dais.legodroid.lib.comm.BluetoothConnection;
import it.unive.dais.legodroid.lib.plugs.TachoMotor;
import it.unive.dais.legodroid.lib.util.Prelude;

/* Questa classe è un singleton utilizzato per accedere al brick EV3 da diverse classi
all'interno del progetto.
 */
public class Robot implements Serializable {
    private static EV3 brick;
    private static TachoMotor ruota_dx;   // this is a class field because we need to access it from multiple methods
    private static TachoMotor ruota_sx;
    private static TachoMotor pinza;
    private int dimx, dimy, x, y;

    public  Robot(EV3 e) {
        brick=e;
    }

    public EV3 getBrick(){
        return brick;
    }

    public TachoMotor getRuota_dx() {
        return ruota_dx;
    }

    public TachoMotor getRuota_sx() {
        return ruota_sx;
    }

    public TachoMotor getPinza() {
        return pinza;
    }

    public void setRuota_dx(TachoMotor dx){
        ruota_dx=dx;
    }

    public void setRuota_sx(TachoMotor sx) {
        ruota_sx=sx;
    }

    public void setPinza(TachoMotor p){
        pinza=p;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public void setX(int x){
        x=x;
    }

    public void setY(int y){
        y=y;
    }

    public int getDimx(){
        return dimx;
    }

    public int getDimy(){
        return dimy;
    }

    public void setDimx(int x){
        dimx=x;
    }

    public void setDimy(int y){
        dimy=y;
    }
}
