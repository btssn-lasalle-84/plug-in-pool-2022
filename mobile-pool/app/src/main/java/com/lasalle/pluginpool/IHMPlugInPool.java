package com.lasalle.pluginpool;

/**
 * @file IHMPlugInPool.java
 * @brief Déclaration de la classe IHMPlugInPool
 * @author MERAS Pierre
 */

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;

import java.util.Set;
import java.util.Vector;

/**
 * @class IHMPlugInPool
 * @brief L'activité principale
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class IHMPlugInPool extends AppCompatActivity
{
    /**
     * Constantes
     */
    private static final String TAG = "_IHMPlugInPool_";  //!< TAG pour les logs

    /**
     * Attributs
     */
    private BaseDeDonnees baseDeDonnees = null;

    /**
     * Ressources IHM
     */
    private Button boutonHistoriqueRencontre;//!< Le bouton permettant d'accèder à l'historique des rencontres
    private Button boutonCreerJoueur;//!< Le bouton permettant de créer un joueur
    private Button boutonNouvelleRencontre;//!< Le bouton permettant de démarrer une nouvelle rencontre
    private AppCompatButton boutonEtatBluetooth;//!< Le bouton permettant d'afficher l'état du bluetooth

    /**
     * @brief Méthode appelée à la création de l'activité
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate()");

        baseDeDonnees = new BaseDeDonnees(this);
        // Test BDD
        Vector<Joueur> joueurs = baseDeDonnees.getJoueurs();

        initialiserRessourcesIHM();
        initialiserRessourcesBluetooth();
    }

    /**
     * @brief Méthode appelée au démarrage après le onCreate() ou un restart après un onStop()
     */
    @Override
    protected void onStart()
    {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    /**
     * @brief Méthode appelée après onStart() ou après onPause()
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    /**
     * @brief Méthode appelée après qu'une boîte de dialogue s'est affichée (on reprend sur un onResume()) ou avant onStop() (activité plus visible)
     */
    @Override
    protected void onPause()
    {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    /**
     * @brief Méthode appelée lorsque l'activité n'est plus visible
     */
    @Override
    protected void onStop()
    {
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    /**
     * @brief Méthode appelée à la destruction de l'application (après onStop() et détruite par le système Android)
     */
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    /**
     * @brief Initialise les ressources graphiques de l'activité
     */
    private void initialiserRessourcesIHM()
    {
        boutonHistoriqueRencontre = (Button)findViewById(R.id.boutonHistoriqueRencontre);
        boutonCreerJoueur = (Button)findViewById(R.id.boutonCreerJoueur);
        boutonNouvelleRencontre = (Button)findViewById(R.id.boutonNouvelleRencontre);
        boutonEtatBluetooth = (AppCompatButton)findViewById(R.id.boutonEtatBluetooth);

        boutonNouvelleRencontre.setOnClickListener(
        new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(IHMPlugInPool.this, IHMNouvelleRencontre.class);
                startActivity(intent);
            }
        });

        boutonCreerJoueur.setOnClickListener(
        new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(IHMPlugInPool.this, IHMCreerJoueur.class);
                startActivity(intent);
            }
        });
    }

    /**
     * @brief Initialise les ressources bluetooth de l'application
     */

    @SuppressLint("MissingPermission")
    private void initialiserRessourcesBluetooth()
    {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Log.d(TAG, "initialiserRessourcesBluetooth()");

        if(bluetoothAdapter == null)
        {
            boutonEtatBluetooth.setBackgroundResource(R.drawable.bords_ronds);
            Log.d(TAG, "initialiserRessourcesBluetooth() Etat bluetooth : " + bluetoothAdapter.isEnabled());
        }
        else
        {
            if (!bluetoothAdapter.isEnabled())
            {
                boutonEtatBluetooth.setBackgroundResource(R.drawable.bords_ronds);
                Log.d(TAG, "initialiserRessourcesBluetooth() Etat bluetooth : " + bluetoothAdapter.isEnabled());
                bluetoothAdapter.enable();
                boutonEtatBluetooth.setBackgroundResource(R.drawable.bords_ronds_verts);
            }
            else
            {
                boutonEtatBluetooth.setBackgroundResource(R.drawable.bords_ronds_verts);
                Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
                Log.d(TAG, "initialiserRessourcesBluetooth() Etat bluetooth : " + bluetoothAdapter.isEnabled());
                for (BluetoothDevice blueDevice : devices)
                {
                    Log.d(TAG, "initialiserRessourcesBluetooth() - Périphérique = [" + blueDevice.getAddress() + "] "  + blueDevice.getName());
                }
            }
        }
    }
}
