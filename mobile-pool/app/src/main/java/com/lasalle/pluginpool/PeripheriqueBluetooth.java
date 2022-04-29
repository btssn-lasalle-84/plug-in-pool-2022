package com.lasalle.pluginpool;

/**
 * @file PeripheriqueBluetooth.java
 * @brief Déclaration de la classe PeripheriqueBluetooth
 * @author MERAS Pierre
 */

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * @class PeripheriqueBluetooth
 * @brief Classe permettant de créer des périphériques bluetooth
 */
public class PeripheriqueBluetooth extends Thread
{
    /**
     * Constantes
     */
    private static final String TAG = "_PeripheriqueBluetooth_";  //!< TAG pour les logs
    public final static int CODE_CONNEXION = 0;
    public final static int CODE_RECEPTION = 1;
    public final static int CODE_DECONNEXION = 2;

    /**
     * Variables
     */
    private AppCompatActivity ihm;
    private String nom;
    private String adresse;
    private Handler handler = null;
    private BluetoothDevice device = null;
    private BluetoothSocket socket = null;
    private InputStream receiveStream = null;
    private OutputStream sendStream = null;
    private TReception tReception;

    /**
     * @brief Constructeurs
     */

    @SuppressLint("MissingPermission")
    public PeripheriqueBluetooth(BluetoothDevice device, android.os.Handler handler)
    {
        if (device != null)
        {
            this.device = device;
            this.nom = device.getName();
            this.adresse = device.getAddress();
            this.handler = handler;
        }
        else
        {
            this.device = null;
            this.nom = "Aucun";
            this.adresse = "";
            this.handler = handler;
        }

        try
        {
            socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            receiveStream = socket.getInputStream();
            sendStream = socket.getOutputStream();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            socket = null;
        }

        if (socket != null)
        {
            tReception = new TReception(handler, socket);
        }
    }

    /**
     * @brief Accesseurs
     */
    public String getNom()
    {
        return nom;
    }

    public String getAdresse()
    {
        return adresse;
    }

    /**
     * @brief Mutateurs
     */
    public void setNom(String nom)
    {
        this.nom = nom;
    }

    /**
     * @brief Récuperer l'état de la connexion
     */
    public boolean estConnecte()
    {
        if(socket == null)
            return false;
        else
            return true;
    }

    /**
     * @brief Envoie des données sur la connexion bluetooth
     */
    public void envoyer(String data)
    {
        if(socket == null)
            return;

        new Thread()
        {
            @Override public void run()
            {
                try
                {
                    if(socket.isConnected())
                    {
                        Log.d(TAG, "envoyer() : " + data);
                        sendStream.write(data.getBytes());
                        sendStream.flush();
                    }
                }
                catch (IOException e)
                {
                    System.out.println("<Socket> error send");
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * @brief Débute la connexion bluetooth
     */
    public void connecter()
    {
        new Thread()
        {
            @SuppressLint("MissingPermission")
            @Override public void run()
            {
                try
                {
                    socket.connect();

                    Message message = Message.obtain();
                    message.arg1 = CODE_CONNEXION;
                    handler.sendMessage(message);

                    tReception.start();
                }
                catch (IOException e)
                {
                    System.out.println("<Socket> error connect");
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * @brief Met fin à la connexion bluetooth
     */
    public boolean deconnecter()
    {
        try
        {
            tReception.arreter();
            socket.close();
            return true;
        }
        catch (IOException e)
        {
            System.out.println("<Socket> error close");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @brief Renvoie l'objet sous forme de chaîne de caractères
     */
    public String toString()
    {
        return "\nNom : " + nom + "\nAdresse : " + adresse;
    }
}
