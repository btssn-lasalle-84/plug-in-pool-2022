package com.lasalle.pluginpool;

/**
 * @file IHMRencontreEnCours.java
 * @brief Déclaration de la classe IHMRencontreEnCours
 * @author MERAS Pierre
 */

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * @class IHMRencontreEnCours
 * @brief L'activité pour une rencontre en cours
 */

public class IHMRencontreEnCours extends AppCompatActivity
{
    /**
     * Constantes
     */
    private static final String TAG = "_IHMRencontreEnCours_";  //!< TAG pour les logs

    /**
     * Ressources IHM
     */
    private Button boutonQuitterRencontre;//!< Le bouton permettant d'arreter la rencontre
    private Button boutonFaute;//!< Le bouton permettant de signaler une faute lors du tour du joueur
    private Button boutonJoueurSuivant;//!< Le bouton permettant de passer la main au joueur suivant

    /**
     * @brief Méthode appelée à la création de l'activité
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ihm_rencontre_en_cours);
        Log.d(TAG, "onCreate()");
        initialiserRessourcesIHMRencontreEnCours();
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
    private void initialiserRessourcesIHMRencontreEnCours()
    {
        boutonQuitterRencontre = (Button)findViewById(R.id.boutonQuitterRencontre);
        boutonFaute = (Button)findViewById(R.id.boutonFaute);
        boutonJoueurSuivant = (Button)findViewById(R.id.boutonJoueurSuivant);

        boutonQuitterRencontre.setOnClickListener(
            new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    Intent intent = new Intent(IHMRencontreEnCours.this, IHMNouvelleRencontre.class);
                    startActivity(intent);
                }
            });

        boutonFaute.setOnClickListener(
            new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    /*Signalement d'une faute*/
                }
            });

        boutonJoueurSuivant.setOnClickListener(
            new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    /*Passage au joueur suivant*/
                }
            });
    }
}