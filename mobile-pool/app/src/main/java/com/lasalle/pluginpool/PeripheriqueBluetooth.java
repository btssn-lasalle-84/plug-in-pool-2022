package com.lasalle.pluginpool;

/**
 * @file PeripheriqueBluetooth.java
 * @brief Déclaration de la classe PeripheriqueBluetooth
 * @author MERAS Pierre
 */

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
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
    public final static int CODE_CREATION_SOCKET = 0;
    public final static int CODE_CONNEXION_SOCKET = 1;
    public final static int CODE_RECEPTION_TRAME = 2;
    public final static int CODE_DECONNEXION_SOCKET = 3;

    /**
     * Variables
     */
    private String nom;
    private String adresse;
    private Handler handler = null;
    private static BluetoothAdapter bluetoothAdapter = null;
    private BluetoothDevice device = null;
    private BluetoothSocket socket = null;
    private InputStream receiveStream = null;
    private OutputStream sendStream = null;
    private TReception tReception;

    /**
     * @brief Constructeurs
     */
    @SuppressLint("MissingPermission")
    public PeripheriqueBluetooth(android.os.Handler handler)
    {
        activerBluetooth();

        this.device = null;
        this.nom = "";
        this.adresse = "";
        this.handler = handler;
    }

    @SuppressLint("MissingPermission")
    private void activerBluetooth()
    {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled())
        {
            Log.d(TAG,"Activation bluetooth");
            bluetoothAdapter.enable();
        }
    }

    @SuppressLint("MissingPermission")
    public boolean rechercherTable(String idTable)
    {
        Set<BluetoothDevice> appareilsAppaires = bluetoothAdapter.getBondedDevices();

        Log.d(TAG,"Recherche bluetooth : " + idTable);
        for (BluetoothDevice appareil : appareilsAppaires)
        {
            Log.d(TAG,"Nom : " + appareil.getName() + " | Adresse : " + appareil.getAddress());
            if (appareil.getName().equals(idTable) || appareil.getAddress().equals(idTable))
            {
                device = appareil;
                this.nom = device.getName();
                this.adresse = device.getAddress();
                creerSocket();
                return true; // trouvé !
            }
        }

        return false;
    }

    @SuppressLint("MissingPermission")
    private void creerSocket()
    {
        if (device == null)
            return;
        try
        {
            Log.d(TAG, "Création socket");
            socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            receiveStream = socket.getInputStream();
            sendStream = socket.getOutputStream();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Log.d(TAG, "Erreur création socket !");
            socket = null;
        }

        if (socket != null)
        {
            tReception = new TReception(handler, socket);
            Message message = new Message();
            message.what = CODE_CREATION_SOCKET;
            message.obj = this.nom;
            handler.sendMessage(message);
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

    public BluetoothSocket getSocket()
    {
        return this.socket;
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
            return socket.isConnected();
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
        if (socket == null)
            return;
        new Thread()
        {
            @SuppressLint("MissingPermission")
            @Override public void run()
            {
                try
                {
                    Log.d(TAG, "Connexion bluetooth");
                    socket.connect();

                    Message message = new Message();
                    message.what = CODE_CONNEXION_SOCKET;
                    message.obj = nom;
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
    public void deconnecter()
    {
        if (device == null || socket == null)
            return;

        Log.d(TAG,"Déconnexion du module " + this.nom + " | Adresse : " + this.adresse);
        new Thread()
        {
            @Override public void run()
            {
                try
                {
                    tReception.arreter();
                    socket.close();
                    Message message = new Message();
                    message.what = CODE_DECONNEXION_SOCKET;
                    message.obj = nom;
                    handler.sendMessage(message);
                }
                catch (IOException e)
                {
                    Log.e(TAG,"Erreur fermeture du socket");
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * @brief Renvoie l'objet sous forme de chaîne de caractères
     */
    public String toString()
    {
        return "\nNom : " + nom + "\nAdresse : " + adresse;
    }
}
