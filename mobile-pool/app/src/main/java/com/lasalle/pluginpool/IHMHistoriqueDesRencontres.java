package com.lasalle.pluginpool;

/**
 * @file IHMHistoriqueDesRencontres.java
 * @brief Déclaration de la classe IHMHistoriqueDesRencontres
 * @author MERAS Pierre
 */

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * @class IHMHistoriqueDesRencontres
 * @brief L'activité pour créer un joueur
 */

public class IHMHistoriqueDesRencontres extends AppCompatActivity
{
    /**
     * Constantes
     */
    private static final String TAG = "_IHMHistoriqueRencontres_";  //!< TAG pour les logs

    /**
     * Ressources IHM
     */

    /**
     * @brief Méthode appelée à la création de l'activité
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ihm_historique_des_rencontres);
        Log.d(TAG, "onCreate()");
        initialiserRessourcesIHMHistoriqueDesRencontres();
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
    private void initialiserRessourcesIHMHistoriqueDesRencontres()
    {
        /*Bouton accueil*/
    }
}