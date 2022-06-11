package com.lasalle.pluginpool;

/**
 * @file IHMHistoriqueDesRencontres.java
 * @brief Déclaration de la classe IHMHistoriqueDesRencontres
 * @author MERAS Pierre
 */

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
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
    private static final String TAG = "_IHMHistorique_";  //!< TAG pour les logs
    private static final String DESCRIPTION_MANCHES = "Joueurs \t Score \t Précision";

    /**
     * Variables
     */
    private ListView listeHistoriqueRencontres;
    private BaseDeDonnees baseDeDonnees = null;

    /**
     * Ressources IHM
     */
    private Button boutonPurgerHistorique;
    private ImageButton boutonAccueil;

    /**
     * @brief Méthode appelée à la création de l'activité
     */
    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
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
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initialiserRessourcesIHMHistoriqueDesRencontres()
    {
        Log.d(TAG, "initialiserRessourcesIHMHistoriqueDesRencontres()");
        boutonAccueil = (ImageButton)findViewById(R.id.boutonAcceuil);
        listeHistoriqueRencontres = (ListView)findViewById(R.id.listeHistoriqueRencontres);
        boutonPurgerHistorique = (Button)findViewById(R.id.boutonPurgerHistorique);
        ouvrirBaseDeDonnees();
        listerRencontres();

        boutonPurgerHistorique.setOnClickListener(
        new View.OnClickListener()
        {
            public void onClick(View v)
            {
                new AlertDialog.Builder(IHMHistoriqueDesRencontres.this, R.style.Theme_PlugInPool_BoiteDialogue)
                    .setIcon(android.R.drawable.ic_delete)
                    .setTitle("Suppression")
                    .setMessage("Êtes-vous sûr de vouloir supprimer toutes les rencontres ?")
                    .setPositiveButton("Oui", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int j)
                        {
                            baseDeDonnees.purgerRencontres();
                            listerRencontres();
                            Log.d(TAG, "Rencontre supprimées");
                        }
                    }).setNegativeButton("Retour", null).show();
            }
        });

        boutonAccueil.setOnClickListener(
        new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(IHMHistoriqueDesRencontres.this, IHMPlugInPool.class);
                startActivity(intent);
            }
        });
    }

    /**
     * @brief Méthode permettant de lister les rencontre dans la base de données
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void listerRencontres()
    {
        Log.d(TAG, "listerRencontres()");
        Vector<Rencontre> listeRencontre = baseDeDonnees.getRencontres();
        List<String> rencontres = new ArrayList<String>();

        for(int i = 0; i < listeRencontre.size(); ++i)
        {
            ajouterRencontreListe(listeRencontre, rencontres, i);
        }

        ArrayAdapter adapter = new ArrayAdapter(IHMHistoriqueDesRencontres.this, android.R.layout.simple_list_item_1, rencontres);
        listeHistoriqueRencontres.setAdapter(adapter);

        //Détails de la rencontre (les manches)
        listeHistoriqueRencontres.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                final int itemSelection = position;
                ArrayAdapter<String> adapteurManches = listerManches(listeRencontre.get(itemSelection));

                new AlertDialog.Builder(IHMHistoriqueDesRencontres.this, R.style.Theme_PlugInPool_BoiteDialogue)
                    .setTitle("Description de la rencontre")
                    .setPositiveButton("Supprimer", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int j)
                        {
                            //Validation de la suppression d'une rencontre
                            confirmerSuppressionRencontre(rencontres, itemSelection, listeRencontre, adapter);
                        }
                    })
                    .setNegativeButton("Retour", null)
                    .setAdapter(adapteurManches, null)
                    .show();
                return true;
            }
        });
    }

    /**
     * @brief Méthode pour confirmer la suppression d'une rencontre dans la base de données
     * @param rencontres
     * @param itemSelection
     * @param listeRencontre
     * @param adapter
     */
    private void confirmerSuppressionRencontre(List<String> rencontres, int itemSelection, Vector<Rencontre> listeRencontre, ArrayAdapter adapter)
    {
        new AlertDialog.Builder(IHMHistoriqueDesRencontres.this, R.style.Theme_PlugInPool_BoiteDialogue)
            .setIcon(android.R.drawable.ic_delete)
            .setTitle("Suppression")
            .setMessage("Êtes-vous sûr de vouloir supprimer cette rencontre ?")
            .setPositiveButton("Oui", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int j)
                {
                    rencontres.remove(itemSelection);
                    baseDeDonnees.supprimerRencontre(listeRencontre.get(itemSelection));
                    adapter.notifyDataSetChanged();
                    Log.d(TAG, "Rencontre supprimée");
                }
            }).setNegativeButton("Retour", null).show();
    }

    /**
     * @brief Ajoute les rencontres sur l'affichage dans l'historique
     * @param listeRencontre
     * @param rencontres
     * @param i
     */
    private void ajouterRencontreListe(Vector<Rencontre> listeRencontre, List<String> rencontres, int i)
    {
        Log.d(TAG, "ajouterRencontreListe()");
        Rencontre rencontre = new Rencontre(listeRencontre.get(i));
        int scoreJoueur1 = compterManchesGagnees(rencontre);
        DecimalFormat format = new DecimalFormat("00.00");
        Log.d(TAG, "ajouterRencontreListe() : " + listeRencontre.size());
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

    /**
     * @brief Permet de lister les rencontre dans un adaptateur
     * @param rencontre
     * @return l'adaptateur des rencontres
     */
    private ArrayAdapter<String> listerManches(Rencontre rencontre)
    {
        Log.d(TAG, "afficherManches()");
        List<String> manches = new ArrayList<String>();
        for(int i = 0; i < rencontre.getManches().size(); ++i)
        {
            manches.add(
                (i + 1) + " - Gagnant : " +
                trouverGagnant(rencontre, i) + " | " +
                rencontre.getManches().get(i).getPointsJoueur1() + " - " +
                rencontre.getManches().get(i).getPointsJoueur2() + " | " +
                rencontre.getManches().get(i).getPrecisionJoueur1() + "% - " +
                rencontre.getManches().get(i).getPrecisionJoueur2() + "%"
            );
        }
        return new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, manches);
    }

    /**
     * @brief Permet de trouver le gagnant d'une manche grace aux points
     * @param rencontre
     * @param i
     * @return le gagnant de la manche
     */
    private String trouverGagnant(Rencontre rencontre, int i)
    {
        Log.d(TAG, "trouverGagnant()");
        String gagnantManche = rencontre.getJoueurs().get(0).getNom() + " " + rencontre.getJoueurs().get(0).getPrenom();

        if(rencontre.getManches().get(i).getPointsJoueur1() < rencontre.getManches().get(i).getPointsJoueur2())
        {
            gagnantManche = rencontre.getJoueurs().get(1).getNom() + " " + rencontre.getJoueurs().get(1).getPrenom();
        }
        return gagnantManche;
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