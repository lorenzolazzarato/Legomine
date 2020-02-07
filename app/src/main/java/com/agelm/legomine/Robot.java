package com.agelm.legomine;

import java.io.IOException;

import it.unive.dais.legodroid.lib.EV3;
import it.unive.dais.legodroid.lib.comm.BluetoothConnection;
import it.unive.dais.legodroid.lib.plugs.TachoMotor;
import it.unive.dais.legodroid.lib.util.Prelude;

/* Questa classe è un singleton utilizzato per accedere al brick EV3 da diverse classi
all'interno del progetto.
 */
public class Robot {
    private static EV3 brick;
    private static TachoMotor ruota_dx;   // this is a class field because we need to access it from multiple methods
    private static TachoMotor ruota_sx;
    private static TachoMotor pinza;

    public static TachoMotor getRuota_dx() {
        return ruota_dx;
    }

    public static TachoMotor getRuota_sx() {
        return ruota_sx;
    }

    public static TachoMotor getPinza() {
        return pinza;
    }

    private Robot() {
        BluetoothConnection conn = new BluetoothConnection("AGELM");
        BluetoothConnection.BluetoothChannel channel = null;
        try {
            channel = conn.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        brick = new EV3(channel);
        Prelude.trap(() -> brick.run(Robot::getSensors));
    }

    public static EV3 getInstance() {
        if(brick == null) {
            new Robot();
        }
        return brick;
    }

    public static void getSensors(EV3.Api api) {
        ruota_dx = api.getTachoMotor(EV3.OutputPort.B);
        ruota_sx = api.getTachoMotor(EV3.OutputPort.C);
        pinza = api.getTachoMotor(EV3.OutputPort.A);
    }
}
