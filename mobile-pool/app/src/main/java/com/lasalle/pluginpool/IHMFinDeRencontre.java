package com.lasalle.pluginpool;

/**
 * @file IHMFinDeRencontre.java
 * @brief Déclaration de la classe IHMFinDeRencontre
 * @author MERAS Pierre
 */

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * @class IHMFinDeRencontre
 * @brief L'activité de fin de rencontre
 */

public class IHMFinDeRencontre extends AppCompatActivity
{
    /**
     * Constantes
     */
    private static final String TAG = "_IHMFinDeRencontre_";  //!< TAG pour les logs

    /**
     * Ressources IHM
     */
    private Button boutonEnregistrerRencontre;//!< Le bouton permettant l'enregistrement des données de la rencontre'
    private Button boutonRejouer;//!< Le bouton permettant de rejouer une rencontre avec les mêmes paramètres'


    /**
     * @brief Méthode appelée à la création de l'activité
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ihm_fin_de_rencontre);
        Log.d(TAG, "onCreate()");
        initialiserRessourcesIHMFinDeRencontre();
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
    private void initialiserRessourcesIHMFinDeRencontre()
    {
        boutonEnregistrerRencontre = (Button)findViewById(R.id.boutonEnregistrerRencontre);
        boutonRejouer = (Button)findViewById(R.id.boutonRejouer);

        boutonEnregistrerRencontre.setOnClickListener(
            new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    /*Enregistrer la rencontre*/
                }
            });

        boutonRejouer.setOnClickListener(
            new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    /*Intent intent = new Intent(IHMNouvelleRencontre.this, IHMRencontreEnCours.class);
                    startActivity(intent);*/
                }
            });
    }
}