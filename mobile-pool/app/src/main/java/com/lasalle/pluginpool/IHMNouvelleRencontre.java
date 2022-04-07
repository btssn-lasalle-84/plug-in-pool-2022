package com.lasalle.pluginpool;

/**
 * @file IHMNouvelleRencontre.java
 * @brief Déclaration de la classe IHMNouvelleRencontre
 * @author MERAS Pierre
 */

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * @class IHMNouvelleRencontre
 * @brief L'activité pour une nouvelle rencontre
 */

public class IHMNouvelleRencontre extends AppCompatActivity
{
    /**
     * Constantes
     */
    private static final String TAG = "_IHMNouvelleRencontre_";  //!< TAG pour les logs

    /**
     * Ressources IHM
     */
    private Button boutonLancerRencontre;//!< Le bouton permettant d'accèder à l'historique des rencontres

    /**
     * @brief Méthode appelée à la création de l'activité
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ihm_nouvelle_rencontre);
        Log.d(TAG, "onCreate()");
        initialiserRessourcesIHMNouvelleRencontre();
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
    private void initialiserRessourcesIHMNouvelleRencontre()
    {
        boutonLancerRencontre = (Button)findViewById(R.id.boutonLancerRencontre);

        boutonLancerRencontre.setOnClickListener(
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