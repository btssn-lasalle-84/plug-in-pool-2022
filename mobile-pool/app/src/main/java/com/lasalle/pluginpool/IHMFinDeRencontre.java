package com.lasalle.pluginpool;

/**
 * @file IHMFinDeRencontre.java
 * @brief Déclaration de la classe IHMFinDeRencontre
 * @author MERAS Pierre
 */

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Vector;

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
    private BaseDeDonnees baseDeDonnees = null;

    /**
     * Ressources IHM
     */
    private Button boutonEnregistrerRencontre;//!< Le bouton permettant l'enregistrement des données de la rencontre'
    private Button boutonRejouer;//!< Le bouton permettant de rejouer une rencontre avec les mêmes paramètres'
    private ImageButton boutonAccueil;
    private Joueur joueurGagnant;
    private TextView texteJoueurGagnant;
    private TextView texteGagnant;
    private TextView nbManchesGagnantes;
    private TextView dureeRencontre;
    private TextView precisionJoueur1;
    private TextView precisionJoueur2;
    private TextView nbfautesJoueur1;
    private TextView nbfautesJoueur2;
    private TextView nomJoueurStatistiques1;
    private TextView nomJoueurStatistiques2;
    private Joueur joueur1;
    private Joueur joueur2;

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
        joueur1 = rencontre.getJoueurs().get(0);
        joueur2 = rencontre.getJoueurs().get(1);
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
        boutonAccueil = (ImageButton)findViewById(R.id.boutonAcceuil);
        texteJoueurGagnant = (TextView)findViewById(R.id.texteJoueurGagnant);
        texteGagnant = (TextView)findViewById(R.id.texteGagnant);
        nbManchesGagnantes = (TextView)findViewById(R.id.texteNbManches);
        dureeRencontre = (TextView)findViewById(R.id.dureeRencontre);
        nomJoueurStatistiques1 = (TextView)findViewById(R.id.nomJoueurStatistiques1);
        nomJoueurStatistiques2 = (TextView)findViewById(R.id.nomJoueurStatistiques2);
        precisionJoueur1 = (TextView)findViewById(R.id.precision1);
        precisionJoueur2 = (TextView)findViewById(R.id.precision2);
        nbfautesJoueur1 = (TextView)findViewById(R.id.nbFautes1);
        nbfautesJoueur2 = (TextView)findViewById(R.id.nbFautes2);

        rencontre = (Rencontre)getIntent().getSerializableExtra(RENCONTRE);
        ouvrirBaseDeDonnees();

        boutonEnregistrerRencontre.setOnClickListener(
        new View.OnClickListener()
        {
            public void onClick(View v)
            {
                enregistrerRencontre();
            }
        });

        boutonRejouer.setOnClickListener(
        new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Vector<Joueur> joueurs = new Vector<>();
                joueurs.add(new Joueur(rencontre.getJoueurs().get(0).getNom(), rencontre.getJoueurs().get(0).getPrenom()));
                joueurs.add(new Joueur(rencontre.getJoueurs().get(1).getNom(), rencontre.getJoueurs().get(1).getPrenom()));
                Rencontre nouvelleRencontre = new Rencontre(BaseDeDonnees.ID_RENCONTRE_DEFAUT, joueurs, new Vector<Manche>(), rencontre.getNbManchesGagnantes());
                Intent intent = new Intent(IHMFinDeRencontre.this, IHMRencontreEnCours.class);
                intent.putExtra(RENCONTRE, nouvelleRencontre);
                startActivity(intent);
            }
        });

        boutonAccueil.setOnClickListener(
        new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(View v)
            {
                Intent intent = new Intent(IHMFinDeRencontre.this, IHMPlugInPool.class);
                startActivity(intent);
            }
        });
    }

    /**
     * @brief Méthode permettant d'obtenir un accès à la base de données
     */
    private void ouvrirBaseDeDonnees()
    {
        baseDeDonnees = new BaseDeDonnees(this);
        baseDeDonnees.ouvrir();
    }

    /**
     * @brief Méthode pour afficher le nom du joueur Gagnant sur l'IHM
     */
    @SuppressLint("SetTextI18n")
    private void initialiserGagnantIHMFinDeRencontre()
    {
        Log.d(TAG, "initialiserScoresIHMFinDeRencontre()");

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
    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void initialiserStatistiquesFinDeRencontre()
    {
        Log.d(TAG, "initialiserStatistiquesFinDeRencontre()");

        joueur1 = rencontre.getJoueurs().get(0);
        joueur2 = rencontre.getJoueurs().get(1);
        Log.d(TAG, "joueur1.getNbBillesTouchees() : " + joueur1.getNbBillesTouchees() + " joueur1.getNbCoupsTires() : " + joueur1.getNbCoupsTires());
        Log.d(TAG, "joueur2.getNbBillesTouchees() : " + joueur2.getNbBillesTouchees() + " joueur2.getNbCoupsTires() : " + joueur2.getNbCoupsTires());
        Log.d(TAG, "joueur1 precision : " + joueur1.getPrecision());
        Log.d(TAG, "joueur2 precision : " + joueur2.getPrecision());

        nomJoueurStatistiques1.setText(joueur1.getNom() + " " + joueur1.getPrenom()  + " :");
        nomJoueurStatistiques2.setText(joueur2.getNom() + " " + joueur2.getPrenom()  + " :");
        nbfautesJoueur1.setText(String.valueOf(joueur1.getNbFautes()));
        nbfautesJoueur2.setText(String.valueOf(joueur2.getNbFautes()));
        precisionJoueur1.setText(String.format("%.2f", joueur1.getPrecision()) + "%");
        precisionJoueur2.setText(String.format("%.2f", joueur2.getPrecision()) + "%");
    }

    /**
     * @brief Méthode appelée au début de la rencontre pour l'enregistrer dans la base de données
     */
    private void enregistrerRencontre()
    {
        Log.d(TAG, "enregistrerRencontre()");
        baseDeDonnees.enregistrerRencontre(rencontre);
        while(rencontre.getManches().size() > 0)
        {
            enregistrerManche();
        }
    }

    /**
     * @brief Méthode appelée à la fin d'une manche pour l'enregistrer dans la base de données
     */
    private void enregistrerManche()
    {
        Log.d(TAG, "enregistrerManche() : nbManches : " + rencontre.getManches().size());
        baseDeDonnees.enregistrerManche(rencontre.getManches().lastElement());
        rencontre.getManches().remove(rencontre.getManches().lastElement());
    }
}