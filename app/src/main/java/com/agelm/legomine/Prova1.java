package com.agelm.legomine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import it.unive.dais.legodroid.lib.EV3;
import it.unive.dais.legodroid.lib.plugs.TachoMotor;

public class Prova1 extends AppCompatActivity {
    private int num;
    private ComMine c;
    private Robot r;

    private String TAG = "Prova 1";

    private CameraBridgeViewBase mOpenCvCameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prova1);

        Intent i=getIntent();

        r = (Robot) i.getSerializableExtra("Robot");

        r.setDimx(i.getIntExtra("dimx",12));
        r.setDimy(i.getIntExtra("dimy",12));

        r.setX(i.getIntExtra("posx",0));
        r.setY(i.getIntExtra("posy",0));
        num = i.getIntExtra("num",0);

        c = new ComMine();

        final Ball[] target = new Ball[1];

        /*Imposta lo schermo a sempre acceso*/
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        /*Carica le librerie di OpenCV in maniera sincrona*/
        if (!OpenCVLoader.initDebug()) {
            Log.e(TAG, "Unable to load OpenCV");
        } else {
            Log.d(TAG, "OpenCV loaded");
        }
        mOpenCvCameraView = findViewById(R.id.OpenCvView);
        tOpenCv(c);

        try{
            r.getBrick().run(this::avvioRobot);
        }catch (Exception e){}
    }

    private void tOpenCv(ComMine c){
        /*Configura l'elemento della camera*/
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setMaxFrameSize(640, 480);
        mOpenCvCameraView.setCvCameraViewListener(new CameraBridgeViewBase.CvCameraViewListener2() {
            @Override
            public void onCameraViewStarted(int width, int height) {
                Log.d(TAG, "Camera Started");
            }

            @Override
            public void onCameraViewStopped() {
                Log.d(TAG, "Camera Stopped");
            }

            /*Viene eseguito ad ogni frame, con inputFrame l'immagine corrente*/
            @Override
            public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
                /*Salva il frame corrente su un oggetto Mat, ossia una matrice bitmap*/

                Mat frame = inputFrame.rgba();
                try{

                    BallFinder ballFinder = new BallFinder(frame, true);
                    ballFinder.setViewRatio(0.0f);
                    ballFinder.setMinArea(2000);
                    ballFinder.setOrientation("portrait");
                    ArrayList<Ball> f = ballFinder.findBalls();

                    for (Ball b : f) {
                        Log.e("ball", String.format("X:%f Y:%f Rad:%f Col:%s", b.center.x, b.center.y, b.radius, b.color));

                    }
                    /*ordina le palle per dimensione del raggio, in modo da andare a prendere prima quella più vicina (si spera)*/
                    Comparator<Ball> ballComparator = (ball1, ball2) -> (int)(ball1.radius - ball2.radius);
                    f.sort(ballComparator);
                    Collections.reverse(f);

                    c.setRadius(f.get(0).radius);
                    c.setC(f.get(0).color);

                    Bitmap img = Bitmap.createBitmap(frame.cols(),frame.rows(),Bitmap.Config.ARGB_8888);
                    Utils.matToBitmap(frame, img);

                    c.setB(img);
                }catch (Exception e){
                    c.setRadius(0);
                }
                return frame;
            }
        });

        /*Abilita la visualizzazione dell'immagine sullo schermo*/
        mOpenCvCameraView.enableView();
    }

    public void changeIntent(int punti, ArrayList<Cella> m){
        ArrayList<Integer> x = new ArrayList<>(), y=new ArrayList<>();
        ArrayList<String> colore=new ArrayList<>(), bitmap = new ArrayList<>();
        int j=0;

        Intent intent = new Intent(this,Finale.class);
        intent.putExtra("tempo",c.getTempo());
        intent.putExtra("punti",punti);
        intent.putExtra("mine",num);
        intent.putExtra("dimx",r.getDimx());
        intent.putExtra("dimy",r.getDimy());

        for(int i=0;i<m.size();i++)
            if(m.get(i).isBall()){
                x.add(m.get(i).getX());
                y.add(m.get(i).getY());
                colore.add(m.get(i).getColore());
                bitmap.add(saveToInternalStorage(m.get(i).getB(),"Image"+j));
                j++;
            }

        intent.putExtra("x",x);
        intent.putExtra("y",y);
        intent.putExtra("colore",colore);
        intent.putExtra("bitmap",bitmap);

        startActivity(intent);
    }

    private String saveToInternalStorage(Bitmap bitmapImage, String fileName){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,fileName+".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    private void avvioRobot(EV3.Api api) {
        TachoMotor ruota_dx = api.getTachoMotor(EV3.OutputPort.B), ruota_sx = api.getTachoMotor(EV3.OutputPort.C),
                pinza = api.getTachoMotor(EV3.OutputPort.A);

        r.setRuota_dx(ruota_dx);
        r.setRuota_sx(ruota_sx);
        r.setPinza(pinza);

        Thread t = new ThreadOpenCv(mOpenCvCameraView,c, this, r);
        t.start();
    }

    public int getNum(){
        return num;
    }

}
