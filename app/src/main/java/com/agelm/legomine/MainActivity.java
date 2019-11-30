package com.agelm.legomine;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.OpenCVLoader;

import it.unive.dais.legodroid.lib.EV3;
import it.unive.dais.legodroid.lib.GenEV3;
import it.unive.dais.legodroid.lib.comm.BluetoothConnection;
import it.unive.dais.legodroid.lib.plugs.GyroSensor;
import it.unive.dais.legodroid.lib.plugs.LightSensor;
import it.unive.dais.legodroid.lib.plugs.Plug;
import it.unive.dais.legodroid.lib.plugs.TachoMotor;
import it.unive.dais.legodroid.lib.plugs.TouchSensor;
import it.unive.dais.legodroid.lib.plugs.UltrasonicSensor;
import it.unive.dais.legodroid.lib.util.Consumer;
import it.unive.dais.legodroid.lib.util.Prelude;
import it.unive.dais.legodroid.lib.util.ThrowingConsumer;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = Prelude.ReTAG("MainActivity");

    private TextView textView;
    private final Map<String, Object> statusMap = new HashMap<>();
    @Nullable
    private TachoMotor ruota_dx;   // this is a class field because we need to access it from multiple methods
    private TachoMotor ruota_sx;
    private TachoMotor pinza;
    private TextView errorView;

    private Button openConn, closeConn, apriPinza, chiudiPinza;


    private void updateStatus(@NonNull Plug p, String key, Object value) {
        Log.d(TAG, String.format("%s: %s: %s", p, key, value));
        statusMap.put(key, value);
        //runOnUiThread(() -> textView.setText(statusMap.toString()));
    }

    private static class MyCustomApi extends EV3.Api {

        private MyCustomApi(@NonNull GenEV3<? extends EV3.Api> ev3) {
            super(ev3);
        }

        public void mySpecialCommand() { /* do something special */ }
    }

    // quick wrapper for accessing field 'motor' only when not-null; also ignores any exception thrown
    private void applyRuotaSx(@NonNull ThrowingConsumer<TachoMotor, Throwable> f) {
        if (ruota_sx != null)
            Prelude.trap(() -> f.call(ruota_sx));
    }
    private void applyRuotaDx(@NonNull ThrowingConsumer<TachoMotor, Throwable> f) {
        if (ruota_dx != null)
            Prelude.trap(() -> f.call(ruota_dx));
    }

    private void applyPinza(@NonNull ThrowingConsumer<TachoMotor, Throwable> f) {
        if (pinza != null)
            Prelude.trap(() -> f.call(pinza));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        errorView = findViewById((R.id.errorView));
        EV3 ev3;

        // Caricamento OpenCV
        if (!OpenCVLoader.initDebug()) {
            Log.e("AndroidIngSwOpenCV", "Unable to load OpenCV");
        } else {
            Log.d("AndroidIngSwOpenCV", "OpenCV loaded");
        }

        try {
            BluetoothConnection conn = new BluetoothConnection("AGELM");
            BluetoothConnection.BluetoothChannel channel = conn.connect();
            ev3 = new EV3(channel);

            Button stopButton = findViewById(R.id.stopButton);
            stopButton.setOnClickListener(v -> {
                ev3.cancel();   // fire cancellation signal to the EV3 task
            });

            Button startButton = findViewById(R.id.startButton);
            startButton.setOnClickListener(v -> Prelude.trap(() -> ev3.run(this::legoMain)));

        } catch (IOException e) {
            errorView.setText(e.toString());
        }
    }

    // main program executed by EV3

    @SuppressLint("ClickableViewAccessibility")
    private void legoMain(EV3.Api api) {
        // pulsanti per il movimento (DEBUG)
        Button apriPinza = findViewById(R.id.apriPinza);
        Button chiudiPinza = findViewById(R.id.chiudiPinza);
        Button avantiButt = findViewById(R.id.muoviAvanti);
        Button sxButt = findViewById(R.id.muoviSx);
        Button dxButt = findViewById(R.id.muoviDx);
        Button indietroButt = findViewById(R.id.muoviIndietro);

        // sensori
        final GyroSensor gyroSensor = api.getGyroSensor(EV3.InputPort._1);

        // motori sulle corrispondenti porte
        ruota_dx = api.getTachoMotor(EV3.OutputPort.B);
        ruota_sx = api.getTachoMotor(EV3.OutputPort.C);
        pinza = api.getTachoMotor(EV3.OutputPort.A);

        try {
            applyRuotaSx(TachoMotor::resetPosition);
            applyRuotaDx(TachoMotor::resetPosition);

            // PINZA
            apriPinza.setOnTouchListener((view, motionEvent) -> {
                try {
                    pinza.setPolarity(TachoMotor.Polarity.FORWARD);
                } catch (IOException e) {
                    errorView.setText(e.toString());
                }
                switch(motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        applyPinza(p -> {
                            p.setPower(50);
                            p.start();
                        });
                        return true;
                    case MotionEvent.ACTION_UP:
                        applyPinza(TachoMotor::stop);
                        return true;
                }
                return true;
            });

            chiudiPinza.setOnTouchListener((view, motionEvent) -> {
                try {
                    pinza.setPolarity(TachoMotor.Polarity.BACKWARDS);
                } catch (IOException e) {
                    errorView.setText(e.toString());
                }
                switch(motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        applyPinza(p -> {
                            p.setPower(50);
                            p.start();
                        });
                        return true;
                    case MotionEvent.ACTION_UP:
                        applyPinza(TachoMotor::stop);
                        return true;
                }
                return true;
            });
            // AVANTI
            avantiButt.setOnTouchListener((view, motionEvent) -> {
                switch(motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Prelude.trap(() -> {
                            ruota_sx.setPower(80);
                            ruota_dx.setPower(80);
                            ruota_sx.start();
                            ruota_dx.start();
                        });
                        return true;
                    case MotionEvent.ACTION_UP:
                        Prelude.trap(() -> {
                            ruota_sx.stop();
                            ruota_dx.stop();
                        });
                        return true;
                }
                return true;
            });
            // INDIETRO
            indietroButt.setOnTouchListener((view, motionEvent) -> {
                switch(motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Prelude.trap(() -> {
                            ruota_sx.setPower(-80);
                            ruota_dx.setPower(-80);
                            ruota_sx.start();
                            ruota_dx.start();
                        });
                        return true;
                    case MotionEvent.ACTION_UP:
                        Prelude.trap(() -> {
                            ruota_sx.stop();
                            ruota_dx.stop();
                        });
                        return true;
                }
                return true;
            });
            // DX
            dxButt.setOnTouchListener((view, motionEvent) -> {
                switch(motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Prelude.trap(() -> {
                            ruota_sx.setPower(80);
                            ruota_dx.setPower(0);
                            ruota_sx.start();
                            ruota_dx.start();
                        });
                        return true;
                    case MotionEvent.ACTION_UP:
                        Prelude.trap(() -> {
                            ruota_sx.stop();
                            ruota_dx.stop();
                        });
                        return true;
                }
                return true;
            });
            // SX
            sxButt.setOnTouchListener((view, motionEvent) -> {
                switch(motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Prelude.trap(() -> {
                            ruota_sx.setPower(0);
                            ruota_dx.setPower(80);
                            ruota_sx.start();
                            ruota_dx.start();
                        });
                        return true;
                    case MotionEvent.ACTION_UP:
                        Prelude.trap(() -> {
                            ruota_sx.stop();
                            ruota_dx.stop();
                        });
                        return true;
                }
                return true;
            });
/*
            while (!api.ev3.isCancelled()) {    // loop until cancellation signal is fired
                try {
                    // values returned by getters are boxed within a special Future object
                    Future<Float> gyro = gyroSensor.getAngle();
                    updateStatus(gyroSensor, "gyro angle", gyro.get()); // call get() for actually reading the value - this may block!

                } catch (IOException | InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
*/
        } finally {
            applyRuotaSx(TachoMotor::stop);
            applyRuotaDx(TachoMotor::stop);
            applyPinza(TachoMotor::stop);
        }
    }

    private void legoMainCustomApi(MyCustomApi api) {
        final String TAG = Prelude.ReTAG("legoMainCustomApi");
        // specialized methods can be safely called
        api.mySpecialCommand();
        // stub the other main
        legoMain(api);
    }

}