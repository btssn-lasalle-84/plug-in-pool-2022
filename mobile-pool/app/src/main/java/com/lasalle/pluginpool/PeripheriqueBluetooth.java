package com.lasalle.pluginpool;

import java.util.logging.Handler;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;

public class PeripheriqueBluetooth
{
    private String nom;
    private String adresse;
    private Handler handler = null;
    private BluetoothDevice device = null;

    @SuppressLint("MissingPermission")
    public PeripheriqueBluetooth(BluetoothDevice device, Handler handler)
    {
        if(device != null)
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

        // TODO
    }

    public String getNom()
    {
        return nom;
    }

    public String getAdresse()
    {
        return adresse;
    }

    public boolean estConnecte()
    {
        // TODO

        return false;
    }

    public void setNom(String nom)
    {
        this.nom = nom;
    }

    public String toString()
    {
        return "\nNom : " + nom + "\nAdresse : " + adresse;
    }

    public void envoyer(String data)
    {
        // TODO
    }

    public void connecter()
    {
        // TODO
    }

    public boolean deconnecter()
    {
        // TODO
        return false;
    }
}
