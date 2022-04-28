package com.lasalle.pluginpool;

/**
 * @file RecepteurBluetooth.java
 * @brief Déclaration de la classe RecepteurBluetooth
 * @author MERAS Pierre
 */

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * @class RecepteurBluetooth
 * @brief Classe permettant de détecter de nouveaux périphériques
 */

public class RecepteurBluetooth extends BroadcastReceiver
{
    public RecepteurBluetooth()
    {
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent)
    {
        String action = intent.getAction();
        if (BluetoothDevice.ACTION_FOUND.equals(action))
        {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            Toast.makeText(context, "Nouveau périphérique : " + device.getName(), Toast.LENGTH_SHORT).show();
        }
    }
}