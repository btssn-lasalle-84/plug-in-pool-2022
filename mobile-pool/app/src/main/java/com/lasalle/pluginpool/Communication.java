package com.lasalle.pluginpool;

/**
 * @file Communication.java
 * @brief Déclaration de la classe Communication
 * @author Pierre MERAS
 */

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;
import android.widget.Toast;
import java.util.Set;
import android.content.*;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @class Communication
 * @brief Classe permettant d'utiliser le bluetooth
 */

@SuppressLint("MissingPermission")
public class Communication
{
    /**
     * Constantes
     */
    private static final String TAG = "_Communication_";//<! TAG pour les logs
    private final static int REQUEST_CODE_ENABLE_BLUETOOTH = 0;

    /**
     * Attributs
     */
    private AppCompatActivity ihm;
    private BluetoothAdapter bluetoothAdapter = null;

    /**
     * @brief Constructeur
     */
    @SuppressLint("MissingPermission")
    public Communication(AppCompatActivity ihm)
    {
        this.ihm = ihm;
        initialiser();
    }

    /**
     * @brief Méthode à l'initialisation de la connexion bluetooth
     */

    @SuppressLint("MissingPermission")
    private void initialiser()
    {
        Log.d(TAG, "initialiserCommunication()");
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null)
        {
            Toast.makeText(this.ihm.getApplicationContext(), "Bluetooth non activé !", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if (!bluetoothAdapter.isEnabled())
            {
                bluetoothAdapter.enable();
            }
            else
            {
                Toast.makeText(this.ihm.getApplicationContext(), "Bluetooth activé", Toast.LENGTH_SHORT).show();
            }
            Log.d(TAG, "Etat bluetooth : " + bluetoothAdapter.isEnabled());
        }
        Set<BluetoothDevice> devices;

        devices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice blueDevice : devices)
        {
            Toast.makeText(this.ihm.getApplicationContext(), "Device = " + blueDevice.getName(), Toast.LENGTH_SHORT).show();
        }
    }
}
