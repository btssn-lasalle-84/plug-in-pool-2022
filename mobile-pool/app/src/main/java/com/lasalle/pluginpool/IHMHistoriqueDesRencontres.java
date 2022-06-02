package com.lasalle.pluginpool;

/**
 * @file IHMHistoriqueDesRencontres.java
 * @brief Déclaration de la classe IHMHistoriqueDesRencontres
 * @author MERAS Pierre
 */

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

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
     * Variables
     */
    private ListView listeHistoriqueRencontres;
    private BaseDeDonnees baseDeDonnees = null;

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

    /**
     * @brief Méthode permettant d'obtenir un accès à la base de données
     */
    private void ouvrirBaseDeDonnees()
    {
        baseDeDonnees = new BaseDeDonnees(this);
        baseDeDonnees.ouvrir();
    }

    /**
     * @brief Méthode permettant d'initialiser les ressources de l'IHM
     */
    private void initialiserRessourcesIHMHistoriqueDesRencontres()
    {
        Log.d(TAG, "initialiserRessourcesIHMHistoriqueDesRencontres()");
        listeHistoriqueRencontres = (ListView)findViewById(R.id.listeHistoriqueRencontres);
        ouvrirBaseDeDonnees();
        listerRencontres();
        /*Bouton accueil*/
    }

    /**
     * @brief Méthode permettant de lister les rencontre dans la base de données
     */
    private void listerRencontres()
    {
        Log.d(TAG, "listerRencontres()");
        Vector<Rencontre> listeRencontre = baseDeDonnees.getRencontres();
        List<String> rencontres = new ArrayList<String>();

        for(int i = 0; i < listeRencontre.size(); ++i)
        {
            Rencontre rencontre = new Rencontre(listeRencontre.get(i));
            int scoreJoueur1 = compterManchesGagnees(rencontre);
            DecimalFormat format = new DecimalFormat("00.00");
            Log.d(TAG, "nom = " + rencontre.getJoueurs().get(0).getNom() + " prenom = " + rencontre.getJoueurs().get(0).getPrenom());
            Log.d(TAG, "nom = " + rencontre.getJoueurs().get(1).getNom() + " prenom = " + rencontre.getJoueurs().get(1).getPrenom());
            rencontres.add(
                rencontre.getJoueurs().get(0).getNom() + " " +
                rencontre.getJoueurs().get(0).getPrenom() +  " | " +
                scoreJoueur1 + " - " +
                (rencontre.getManches().size() - scoreJoueur1) + " | " +
                rencontre.getJoueurs().get(1).getNom() + " " +
                rencontre.getJoueurs().get(1).getPrenom() + " | " +
                format.format(rencontre.calculerPrecisionMoyenneJoueur1()) + " % - " +
                format.format(rencontre.calculerPrecisionMoyenneJoueur2()) + " %"
            );
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, rencontres);
        listeHistoriqueRencontres.setAdapter(adapter);
    }

    /**
     * @brief Permet de compter les manches gagnées de chaque joueur
     */
    private int compterManchesGagnees(Rencontre rencontre)
    {
        int mancheGagnees = 0;
        for(int i = 0; i < rencontre.getManches().size(); ++i)
        {
            Log.d(TAG, "compterManchesGagnees() : Rencontre : " + i + " J1 = " +
                    rencontre.getManches().get(i).getPointsJoueur1() + " J2 = " +
                    rencontre.getManches().get(i).getPointsJoueur2());
            if(rencontre.getManches().get(i).getPointsJoueur1() > rencontre.getManches().get(i).getPointsJoueur2())
            {
                mancheGagnees++;
            }
        }
        Log.d(TAG, "nbManchesGagnées : " + mancheGagnees);
        return mancheGagnees;
    }
}