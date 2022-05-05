package com.lasalle.pluginpool;

/**
 * @file TReception.java
 * @brief Déclaration de la classe TReception
 * @author MERAS Pierre
 */

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * @class TReception
 * @brief Classe permettant de gérer la reception bluetooth
 */

public class TReception extends Thread
{
    /**
     * Constante
     */
    private static final String TAG = "_TReception_";  //!< TAG pour les logs

    /**
     * Attributs
     */
    Handler handlerUI;
    InputStream receiveStream = null;
    OutputStream sendStream = null;
    BluetoothSocket socket = null;
    private boolean fini;

    /**
     * @brief Constructeur
     */
    public TReception(Handler h, BluetoothSocket socket)
    {
        this.handlerUI = h;
        this.fini = false;
        this.socket = socket;
        try
        {
            this.receiveStream = socket.getInputStream();
            this.sendStream = socket.getOutputStream();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override public void run()
    {
        Log.d(TAG, "Démarrage réception bluetooth");
        BufferedReader reception = new BufferedReader(new InputStreamReader(receiveStream));
        while(!fini)
        {
            try
            {
                String trame = "";
                if(reception.ready())
                {
                    trame = reception.readLine();;
                }
                if(trame.length() > 0)
                {
                    Log.d(TAG, "run() trame : " + trame);
                    Message message = Message.obtain();
                    message.what = PeripheriqueBluetooth.CODE_RECEPTION_TRAME;
                    message.obj = trame;
                    handlerUI.sendMessage(message);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            try
            {
                Thread.sleep(250);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "Arrêt réception bluetooth");
    }

    public void arreter()
    {
        if(!fini)
        {
            fini = true;
        }
        try
        {
            Thread.sleep(250);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
