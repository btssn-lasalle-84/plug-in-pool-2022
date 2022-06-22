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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;

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
    private static Map<String, PeripheriqueBluetooth> tables = new HashMap<String, PeripheriqueBluetooth>();
    private static String nom;
    private static String nomTable;
    private static String adresse;
    private Handler handler = null;
    private static BluetoothAdapter bluetoothAdapter = null;
    private static BluetoothDevice device = null;
    private BluetoothSocket socket = null;
    private InputStream receiveStream = null;
    private OutputStream sendStream = null;
    private TReception tReception = null;

    /**
     * @brief Constructeurs
     */
    private PeripheriqueBluetooth(BluetoothDevice device, String nom, String adresse)
    {
        this.device = device;
        this.nom = nom;
        this.adresse = adresse;
        Log.d(TAG, "PeripheriqueBluetooth() table : " + this.device + " " + this.nom + " " + this.adresse);
    }

    /**
     * Méthode pour permettre l'instanciation de plusieurs objets de cette classe (multiton)
     * @param handler
     * @return
     */
    public static PeripheriqueBluetooth getInstance(int indice, Handler handler)
    {
        Log.d(TAG, "getInstance()");
        tables = rechercherTables("pool-");
        if (tables.isEmpty())
        {
            Log.d(TAG, "Aucun périphérique !");
        }
        tables = rechercherTables("pool-");
        if(tables.isEmpty())
            return null;

        if (indice > tables.size())
        {
            Log.d(TAG,"Ajout d'un périphérique");
            PeripheriqueBluetooth p = tables.get(indice);
            p.setHandler(handler);
            tables.put(PeripheriqueBluetooth.nomTable, p);
        }
        Log.d(TAG,"Récupération PeripheriqueBluetooth id : " + indice);
        return tables.get(indice);
    }

    /**
     * @brief Méthode pour affecter un handler à un objet tReception
     * @param handler
     */
    public void setHandler(Handler handler)
    {
        this.handler = handler;
        if(tReception != null)
            tReception.setHandlerUI(handler);
    }

    /**
     * @brief Méthode pour forcer l'activation du bluetooth
     */
    @SuppressLint("MissingPermission")
    private static void activerBluetooth()
    {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled())
        {
            Log.d(TAG,"Activation bluetooth");
            bluetoothAdapter.enable();
        }
    }

    /**
     * Méthode pour rechercher une table en particulier
     * @param prefixeTable
     * @return
     */
    @SuppressLint("MissingPermission")
    public static Map<String, PeripheriqueBluetooth> rechercherTables(String prefixeTable)
    {
        activerBluetooth();
        Set<BluetoothDevice> appareilsAppaires = bluetoothAdapter.getBondedDevices();

        Log.d(TAG,"Recherche bluetooth : " + prefixeTable);
        tables.clear();
        for (BluetoothDevice appareil : appareilsAppaires)
        {
            if (appareil.getName().contains(prefixeTable))
            {
                device = appareil;
                nom = device.getName();
                adresse = device.getAddress();
                //PeripheriqueBluetooth p = new PeripheriqueBluetooth(device, nom, adresse);
                //Log.d(TAG, "rechercherTables() table : " + device + " " + nom + " " + adresse);
                //tables.put(nom, p);
            }
        }

        PeripheriqueBluetooth p1 = new PeripheriqueBluetooth(null, "aa", "aa:aa");
        tables.put("a", p1);
        Log.d(TAG, "rechercherTables() tables : " + tables.values());
        PeripheriqueBluetooth p2 = new PeripheriqueBluetooth(null, "bb", "bb:bb");
        tables.put("b", p2);
        Log.d(TAG, "rechercherTables() tables : " + tables.values());
        PeripheriqueBluetooth p3 = new PeripheriqueBluetooth(null, "cc", "cc:cc");
        tables.put("c", p3);
        Log.d(TAG, "rechercherTables() tables : " + tables.values());
        return tables;
    }

    /**
     * @brief Méthode pour la création d'un socket
     */
    @SuppressLint("MissingPermission")
    public void creerSocket()
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
            if(handler != null)
            {
                Message message = new Message();
                message.what = CODE_CREATION_SOCKET;
                message.obj = this.nom;
                handler.sendMessage(message);
            }
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
            creerSocket();
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

                    if(handler != null)
                    {
                        Message message = new Message();
                        message.what = CODE_CONNEXION_SOCKET;
                        message.obj = nom;
                        handler.sendMessage(message);
                    }

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
                    if(handler != null)
                    {
                        Message message = new Message();
                        message.what = CODE_DECONNEXION_SOCKET;
                        message.obj = nom;
                        handler.sendMessage(message);
                    }
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
