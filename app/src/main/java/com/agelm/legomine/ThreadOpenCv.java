package com.agelm.legomine;

import org.opencv.android.CameraBridgeViewBase;

import java.util.ArrayList;

import it.unive.dais.legodroid.lib.plugs.TachoMotor;

public class ThreadOpenCv extends Thread{

    private CameraBridgeViewBase mOpenCvCameraView;
    private ComMine c;
    private Prova1 p;
    private Robot r;
    private final int max=159;
    private TachoMotor dx, sx, pz;

    public ThreadOpenCv(CameraBridgeViewBase open, ComMine c, Prova1 p, Robot r){
        mOpenCvCameraView = open;
        this.c=c;
        this.p=p;
        this.r=r;
    }

    public void run(){
        dx=r.getRuota_dx();
        sx=r.getRuota_sx();
        pz=r.getPinza();
        boolean visita=false;
        int num=p.getNum(),x =r.getX(),y=r.getY(),start, i,trasl, punti=0;
        ArrayList<Cella> matrice = new ArrayList<>();

        if(x==0 && y!=0){
            matrice=inserisci(false,r.getDimy()-1,0,-1);
            start=r.getDimy()-1-y;
            trasl=r.getDimy();
        }else if(y==0 && (x+1)!=r.getDimx()){
            matrice=inserisci(true,0,0,1);
            start=x;
            trasl=r.getDimx();
        }else if((x+1)==r.getDimx() && (y+1)!=r.getDimy()){
            matrice=inserisci(false,r.getDimx()-1,0,1);
            start=y;
            trasl=r.getDimy();
        }else{
            matrice=inserisci(true,r.getDimy()-1,r.getDimx()-1,-1);
            start=r.getDimx()-1-x;
            trasl=r.getDimx();
        }

        matrice.get(start).vai(sx,dx,max);

        matrice.get(start).gira(false, dx,sx);
        matrice.get(start).setVisita();
        i=start;

        while(num>0){
            if(!visita){
                if(i!=start && (((i+1)/trasl)%2!=0 && (i+1)%trasl==0) || ((i/trasl)%2!=0 && i%trasl==0)){
                    matrice.get(i).gira(false,sx,dx);
                }else if((((i+1)/trasl)%2==0 && (i+1)%trasl==0 && i!=start)||((i/trasl)%2==0 && i%trasl==0 && i!=start)){
                    matrice.get(i).gira(false,dx,sx);
                }

                if(matrice.get(i).vai(dx, sx, max)){
                    mOpenCvCameraView.disableView();
                    matrice.get(i).setVisita();
                    matrice.get(i+1).setBall();
                    matrice.get(i+1).setColore(c.getC());

                    if(matrice.get(i+1).getColore().compareTo("red")==0)
                        punti+=1;
                    else if(matrice.get(i+1).getColore().compareTo("blue")==0)
                        punti+=2;
                    else
                        punti+=3;

                    matrice.get(i+1).setB(c.getB());

                    chiudiPinza(pz);
                    if(((i+1)/trasl)%2!=0 && (i+1)%trasl==0){
                        matrice.get(i).gira(true,dx,sx);
                    }else if(((i+1)/trasl)%2==0 && (i+1)%trasl==0){
                        matrice.get(i).gira(true,sx,dx);
                    }
                    else{

                        matrice.get(i+1).gira(true,dx,sx);
                        matrice.get(i+1).gira(true,dx,sx);
                    }
                    tornaStart(i+1, start, matrice, trasl);
                    mOpenCvCameraView.enableView();
                    i=start;
                    c.setRadius(0);
                    num--;
                }else {
                    i++;
                    matrice.get(i).setVisita();
                }
            }else {
                matrice.get(i).gira(false,sx,dx);
                while(!matrice.get(i).vai(dx,sx,max)){
                    i--;
                }
                chiudiPinza(pz);

                matrice.get(i+1).setBall();
                matrice.get(i+1).setColore(c.getC());

                if(matrice.get(i+1).getColore().compareTo("red")==0)
                    punti+=1;
                else if(matrice.get(i+1).getColore().compareTo("blue")==0)
                    punti+=2;
                else
                    punti+=3;

                matrice.get(i+1).setB(c.getB());

                matrice.get(i+1).gira(true, dx,sx);
                matrice.get(i+1).gira(true, dx,sx);
                matrice.get(i+1).vai(sx,dx,max);

                while (i!=start){
                    matrice.get(i).vai(sx,dx,max);
                    i++;
                }

                matrice.get(i).gira(true,dx,sx);
                matrice.get(i).vai(sx,dx,max);
                apriPinza(pz);
                matrice.get(i).indietro(sx,dx);
                matrice.get(i).gira(false,sx,dx);

            }

            for(int n=start; n<r.getDimy()*r.getDimx();n++){
                if(n!=start && !visita){
                    visita=false;
                }else if(matrice.get(n).isVisita()){
                    visita=true;
                }
                else {
                    visita=false;
                }
            }

        }

        p.changeIntent(punti,matrice);

    }

    private void chiudiPinza(TachoMotor pinza){
        c.setTempo(c.getTempo()+400);
        try{
            pinza.setPower(-50);
            pinza.start();
            Thread.sleep(400);
            pinza.stop();
        }catch (Exception e){}
    }

    private void apriPinza(TachoMotor pinza){
        c.setTempo(c.getTempo()+400);
        try{
            pinza.setPower(50);
            pinza.start();
            Thread.sleep(400);
            pinza.stop();
        }catch (Exception e){}
    }

    private void tornaStart(int i, int start, ArrayList<Cella> m, int trasl){
        while(i!=start){
            if((i/trasl)%2!=0 && i%trasl==0){
                m.get(i).gira(true, dx,sx);
                m.get(i).vai(sx,dx,max);
                i--;
                m.get(i).gira(true, dx,sx);
            }else if((i/trasl)%2==0 && i%trasl==0){
                m.get(i).gira(true, sx,dx);
                m.get(i).vai(sx,dx,max);
                i--;
                m.get(i).gira(true, sx,dx);
            }else{
                m.get(i).vai(sx,dx,max);
                i--;
            }
        }
        m.get(i).gira(true, sx,dx);
        m.get(i).vai(sx,dx,max);
        apriPinza(pz);

        m.get(i).indietro(dx,sx);
        m.get(i).gira(false,sx,dx);
    }

    private ArrayList<Cella> inserisci(boolean orizzontale, int i, int j, int num){
        ArrayList<Cella> m=new ArrayList<>();
        int num2=num;

        for(int n=0; n<r.getDimy()*r.getDimx();n++){
            if(orizzontale){
                m.add(new Cella(i,j,c));
                i+=num;
                if(i==r.getDimx()){
                    i-=num;
                    num=num*(-1);
                    j+=num2;
                }
                if(i<0){
                    i-=num;
                    num=num*(-1);
                    j+=num2;
                }

            }else{
                m.add(new Cella(j,i,c));
                i+=num;
                if(i==r.getDimy()){
                    i-=num;
                    num=num*(-1);
                    j+=num2*(-1);
                }
                if(i==0){
                    i-=num;
                    num=num*(-1);
                    j+=num2*(-1);
                }

            }
        }

        return m;
    }

}
