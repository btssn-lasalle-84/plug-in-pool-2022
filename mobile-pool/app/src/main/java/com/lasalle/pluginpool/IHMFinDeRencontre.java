package com.lasalle.pluginpool;

/**
 * @file IHMFinDeRencontre.java
 * @brief Déclaration de la classe IHMFinDeRencontre
 * @author MERAS Pierre
 */

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
    private static final String RENCONTRE = "RENCONTRE";
    private static final String EGALITE = "Egalité !";

    /**
     * Variables
     */
    private Rencontre rencontre;

    /**
     * Ressources IHM
     */
    private Button boutonEnregistrerRencontre;//!< Le bouton permettant l'enregistrement des données de la rencontre'
    private Button boutonRejouer;//!< Le bouton permettant de rejouer une rencontre avec les mêmes paramètres'
    private Joueur joueurGagnant;
    private TextView texteJoueurGagnant;
    private TextView texteGagnant;
    private TextView nbManchesGagnantes;
    private TextView dureeRencontre;

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
        initialiserGagnantIHMFinDeRencontre();
        initialiserNbManchesIHMFinDeRencontre();
        initialiserDureeIHMFinDeRencontre();
        initialiserStatistiquesFinDeRencontre();
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
        texteJoueurGagnant = (TextView)findViewById(R.id.texteJoueurGagnant);
        texteGagnant = (TextView)findViewById(R.id.texteGagnant);
        nbManchesGagnantes = (TextView)findViewById(R.id.texteNbManches);
        dureeRencontre = (TextView)findViewById(R.id.dureeRencontre);

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

    @SuppressLint("SetTextI18n")
    private void initialiserGagnantIHMFinDeRencontre()
    {
        Log.d(TAG, "initialiserScoresIHMFinDeRencontre()");
        rencontre = (Rencontre)getIntent().getSerializableExtra(RENCONTRE);

        if(rencontre.getJoueurs().get(0).getNbManchesGagnees() > rencontre.getJoueurs().get(1).getNbManchesGagnees())
        {
            joueurGagnant = rencontre.getJoueurs().get(0);
            texteJoueurGagnant.setText(joueurGagnant.getNom() + " " + joueurGagnant.getPrenom());
        }
        else if(rencontre.getJoueurs().get(0).getNbManchesGagnees() < rencontre.getJoueurs().get(1).getNbManchesGagnees())
        {
            joueurGagnant = rencontre.getJoueurs().get(1);
            texteJoueurGagnant.setText(joueurGagnant.getNom() + " " + joueurGagnant.getPrenom());
        }
        else
        {
            texteGagnant.setVisibility(View.INVISIBLE);
            texteJoueurGagnant.setText(EGALITE);
        }
    }

    /**
     * @brief Méthode pour afficher les manches remportées à la fin d'une rencontre
     */
    @SuppressLint("SetTextI18n")
    private void initialiserNbManchesIHMFinDeRencontre()
    {
        Log.d(TAG, "initialiserNbManchesIHMFinDeRencontre()");
        if(texteJoueurGagnant.getText().toString().equals(EGALITE))
        {
            nbManchesGagnantes.setVisibility(View.INVISIBLE);
        }
        else
        {
            nbManchesGagnantes.setVisibility(View.VISIBLE);
            nbManchesGagnantes.setText(joueurGagnant.getNbManchesGagnees() + " / " + rencontre.getNbManchesGagnantes() + "manche(s) gagnée(s)");
        }
    }

    /**
     * @brief Méthode pour afficher la durée de la rencontre
     */
    private void initialiserDureeIHMFinDeRencontre()
    {
        Log.d(TAG, "initialiserDureeIHMFinDeRencontre()");
        dureeRencontre.setText(rencontre.getHorodatage());
    }

    /**
     * @brief Méthode pour afficher les statistiques des joueurs
     */
    private void initialiserStatistiquesFinDeRencontre()
    {
        Log.d(TAG, "initialiserStatistiquesFinDeRencontre()");
    }
}