package com.agelm.legomine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Finale extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_risultati);

        Intent in=getIntent();
        int x,y, num;

        TextView t = findViewById(R.id.numPuntiTW);
        t.setText(""+in.getIntExtra("punti",0));

        t = findViewById(R.id.numTempoTW);
        t.setText(""+(in.getDoubleExtra("tempo",0)/1000));

        t = findViewById(R.id.numMineTW);
        num=in.getIntExtra("mine",0);
        t.setText(""+num);

        x=in.getIntExtra("dimx",0);
        y=in.getIntExtra("dimy",0);

        GridLayout g=findViewById(R.id.grid);
        g.setColumnCount(x);
        g.setRowCount(y);
        Button b;

        int cont1=x, cont2=1;

        for(int i=0; i<x*y; i++){
            b=new Button(this);
            b.setId((x*y-(cont1-cont2+1)));
            if(x==cont2){
                cont1+=x;
                cont2=1;
            }else
                cont2++;

            b.setClickable(false);
            g.addView(b);
        }

        LinearLayout l=findViewById(R.id.layoutFoto);
        ArrayList<Integer> ax = in.getIntegerArrayListExtra("x"), ay=in.getIntegerArrayListExtra("y");
        ArrayList<String> colore=in.getStringArrayListExtra("colore"), bitmap = in.getStringArrayListExtra("bitmap");

        for(int i=0;i<num;i++){
            b=findViewById(x*ay.get(i)+ax.get(i));
            b.setText("x:"+ax.get(i)+" y:"+ay.get(i));
            if(colore.get(i).compareTo("blue")==0)
                b.setBackgroundColor(Color.BLUE);
            else if(colore.get(i).compareTo("red")==0)
                b.setBackgroundColor(Color.RED);
            else
                b.setBackgroundColor(Color.YELLOW);

            l.addView(loadImageFromStorage(bitmap.get(i),"Image"+i  ),i);

        }


    }

    private ImageView loadImageFromStorage(String path, String fileName) {
        ImageView img= new ImageView(this);
        try {
            File f=new File(path, fileName+".jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(300,300);
            params.setMargins(20,0,0,0);
            img.setImageBitmap(b);
            img.setLayoutParams(params);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return img;

    }

    public void setting(View view){
        Intent intent = new Intent(this,Setting.class);
        startActivity(intent);
    }

    public void esci(View view){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
}
