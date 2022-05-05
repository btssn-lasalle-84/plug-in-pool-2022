package com.lasalle.pluginpool;

/**
 * @file IHMCreerJoueur.java
 * @brief Déclaration de la classe IHMCreerJoueur
 * @author MERAS Pierre
 */

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * @class IHMCreerJoueur
 * @brief L'activité pour créer un joueur
 */

public class IHMCreerJoueur extends AppCompatActivity
{
    /**
     * Constantes
     */
    private static final String TAG = "_IHMCreerJoueur_";  //!< TAG pour les logs

    /**
     * Attributs
     */
    private BaseDeDonnees baseDeDonnees = null;

    /**
     * Ressources IHM
     */
    private Button boutonValiderCreationJoueur;//!< Le bouton permettant la validation de la création d'un joueur
    private TextView editTextNomJoueur;//!< Le champ permettant la saisie du nom d'un joueur
    private TextView exitTextPrenomJoueur;//!< Le champ permettant la saisie du prénom d'un joueur
    private ListView listeJoueurs;//!< La liste permettant l'affichage des joueurs

    /**
     * @brief Méthode appelée à la création de l'activité
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ihm_creer_joueur);
        Log.d(TAG, "onCreate()");

        baseDeDonnees = new BaseDeDonnees(this);
        baseDeDonnees.ouvrir();
        // Test BDD
        Vector<Joueur> joueurs = baseDeDonnees.getJoueurs();

        initialiserRessourcesIHMCreerJoueur();
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
    private void initialiserRessourcesIHMCreerJoueur()
    {
        Log.d(TAG, "initialiserRessourcesIHMCreerJoueur()");
        boutonValiderCreationJoueur = (Button)findViewById(R.id.boutonValiderCreationJoueur);
        editTextNomJoueur = (EditText)findViewById(R.id.editTextNomJoueur);
        exitTextPrenomJoueur = (EditText)findViewById(R.id.editTextPrenomJoueur);
        listeJoueurs = (ListView)findViewById(R.id.listeJoueurs);

        listerJoueurs();

        boutonValiderCreationJoueur.setOnClickListener(
        new View.OnClickListener()
        {
            public void onClick(View v)
            {
                creerJoueur();
            }
        });
    }

    /**
     * @brief Méthode pour créer un joueur
     */
    private void creerJoueur()
    {
        Log.d(TAG, "creerJoueur()");
        String nomJoueur = editTextNomJoueur.getText().toString().toUpperCase();
        String prenomJoueur = exitTextPrenomJoueur.getText().toString();
        Joueur nouveauJoueur = new Joueur(nomJoueur, prenomJoueur);
        if(nouveauJoueur.getPrenom().equals("") || nouveauJoueur.getNom().equals(""))
        {
            Toast.makeText(IHMCreerJoueur.this, "Impossible de créer le joueur !", Toast.LENGTH_SHORT).show();
            return;
        }
        baseDeDonnees.ouvrir();
        baseDeDonnees.insererJoueur(nouveauJoueur);
        listerJoueurs();
    }

    /**
     * @brief Méthode pour lister les joueurs
     */
    private void listerJoueurs()
    {
        Log.d(TAG, "listerJoueurs()");
        List<Joueur> joueurs = baseDeDonnees.getJoueurs();
        List<String> nomsJoueurs = new ArrayList<String>();

        for(int i = 0; i < joueurs.size(); ++i)
        {
            Joueur joueur = joueurs.get(i);
            nomsJoueurs.add(joueur.getNom() + " " + joueur.getPrenom());
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, nomsJoueurs);
        listeJoueurs.setAdapter(adapter);

        /**
         * @brief Suppresion d'un joueur
         */
        listeJoueurs.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                final int itemSelection = position;
                new AlertDialog.Builder(IHMCreerJoueur.this, R.style.Theme_PlugInPool_BoiteDialogue)
                    .setIcon(android.R.drawable.ic_delete)
                    .setTitle("Suppression")
                    .setMessage("Êtes-vous sûr de vouloir supprimer ce joueur ?")
                    .setPositiveButton("Oui", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int j)
                        {
                            nomsJoueurs.remove(itemSelection);
                            baseDeDonnees.supprimerJoueur(joueurs.get(itemSelection));
                            adapter.notifyDataSetChanged();
                            Log.d(TAG, "Joueur supprimé");
                        }
                    }).setNegativeButton("Retour", null).show();
                return true;
            }
        });
    }
}