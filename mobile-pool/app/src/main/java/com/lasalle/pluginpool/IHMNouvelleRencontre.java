package com.lasalle.pluginpool;

/**
 * @file IHMNouvelleRencontre.java
 * @brief Déclaration de la classe IHMNouvelleRencontre
 * @author MERAS Pierre
 */

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

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
    public static final String ID_INTENT_RENCONTRE = "Rencontre"; //!< Identifiant de données dans l'Intent
    public static final int NB_JOUEURS = 2; //!< Le nombre de joueurs pour une rencontre
    public static final int NB_MANCHES_GAGNANTES = 5; //!< Le nombre de manches gagnantes par défaut

    /**
     * Attributs
     */
    private PeripheriqueBluetooth peripheriqueBluetooth = null;
    private BaseDeDonnees baseDeDonnees = null;
    private Handler handler = null;
    private Rencontre rencontre = null;
    private Vector<Joueur> joueursRencontre;
    private int nbManchesGagnantes;

    /**
     * Ressources IHM
     */
    private Button boutonLancerRencontre;//!< Le bouton de lancement
    private EditText nbManches;
    private ListView listeJoueurs;
    private List<Joueur> joueurs;

    /**
     * @brief Méthode appelée à la création de l'activité
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ihm_nouvelle_rencontre);
        Log.d(TAG, "onCreate()");
        initialiserRessourcesIHMNouvelleRencontre();
        gererHandler();
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
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initialiserRessourcesIHMNouvelleRencontre()
    {
        Log.d(TAG, "initialiserRessourcesIHMNouvelleRencontre()");
        boutonLancerRencontre = (Button)findViewById(R.id.boutonLancerRencontre);
        nbManches = (EditText)findViewById(R.id.editTextNombreManches);
        nbManches.setText(Integer.toString(NB_MANCHES_GAGNANTES));
        // Il faut être connecté à la table et avoir deux joueurs sélectionnés
        boutonLancerRencontre.setEnabled(false);
        listeJoueurs = (ListView)findViewById(R.id.listeJoueursParametres);
        ouvrirBaseDeDonnees();
        listerJoueurs();
        boutonLancerRencontre.setOnClickListener(
        new View.OnClickListener()
        {
            public void onClick(View v)
            {
                initialiserRencontre();
                ajouterJoueursSelectionnes();
                ajouterNbManches();
                rencontre.setHorodatageDebut();
                Log.d(TAG, "nbManches : " + rencontre.getNbManchesGagnantes());
                final Intent intent = new Intent(IHMNouvelleRencontre.this, IHMRencontreEnCours.class);
                // passage de données entre activités
                intent.putExtra(ID_INTENT_RENCONTRE, rencontre);
                startActivity(intent);
            }
        });
    }

    /**
     * @brief Ajoute les joueurs sélectionnés pour cette rencontre
     */
    private void ajouterNbManches()
    {
        nbManchesGagnantes = Integer.parseInt(nbManches.getText().toString());
        rencontre.setNbManchesGagnantes(nbManchesGagnantes);
        Log.d(TAG, "ajouterNbManches () : nbManchesGagnantes : " + nbManchesGagnantes);
    }

    /**
     * @brief Ajoute les joueurs sélectionnés pour cette rencontre
     */
    private void ajouterJoueursSelectionnes()
    {
        //Log.d(TAG, "[ajouterJoueursSelectionnes] listeJoueurs : " + listeJoueurs.getCount());
        //Log.d(TAG, "[ajouterJoueursSelectionnes] joueurs : " + joueurs.size());
        for (int i = 0; i < listeJoueurs.getCount(); i++)
        {
            if (listeJoueurs.isItemChecked(i))
            {
                //Log.d(TAG, "[ajouterJoueursSelectionnes] joueur selectionné : " + i);
                if(joueurs.get(i) != null)
                {
                    Log.d(TAG, "[ajouterJoueursSelectionnes] Joueur : " + joueurs.get(i).getPrenom() + " " + joueurs.get(i).getNom());
                    joueursRencontre.add(new Joueur(joueurs.get(i).getNom(), joueurs.get(i).getPrenom()));
                }
            }
        }
        rencontre.setJoueurs(joueursRencontre);
    }

    /**
     * @brief Initialise un objet Rencontre
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initialiserRencontre()
    {
        joueursRencontre = new Vector<Joueur>();
        rencontre = new Rencontre(joueursRencontre, NB_MANCHES_GAGNANTES);
    }

    /**
     * @brief Initialise les ressources bluetooth
     */
    private void initialiserRessourcesBluetooth()
    {
        Log.d(TAG,"initialiserRessourcesBluetooth()");
        peripheriqueBluetooth = PeripheriqueBluetooth.getInstance(handler);
        if(peripheriqueBluetooth.rechercherTable(Protocole.nomTable))
        {
            peripheriqueBluetooth.connecter();
        }
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
     * @brief Gère le handler pour la communication
     */
    private void gererHandler()
    {
        Log.d(TAG,"gererHandler()");
        this.handler = new Handler(this.getMainLooper())
        {
            @Override
            public void handleMessage(@NonNull Message message)
            {
                Log.d(TAG, "[Handler] id du message = " + message.what);
                Log.d(TAG, "[Handler] contenu du message = " + message.obj.toString());

                switch (message.what)
                {
                    case PeripheriqueBluetooth.CODE_CREATION_SOCKET:
                        Log.d(TAG, "[Handler] CREATION_SOCKET = " + message.obj.toString());
                        break;
                    case PeripheriqueBluetooth.CODE_CONNEXION_SOCKET:
                        Log.d(TAG, "[Handler] CODE_CONNEXION_SOCKET = " + message.obj.toString());
                        if(listeJoueurs.getCheckedItemCount() == NB_JOUEURS && peripheriqueBluetooth.estConnecte())
                            boutonLancerRencontre.setEnabled(true);
                        break;
                    case PeripheriqueBluetooth.CODE_DECONNEXION_SOCKET:
                        Log.d(TAG, "[Handler] DECONNEXION_SOCKET = " + message.obj.toString());
                        boutonLancerRencontre.setEnabled(false);
                        break;
                    case PeripheriqueBluetooth.CODE_RECEPTION_TRAME:
                        Log.d(TAG, "[Handler] RECEPTION_TRAME = " + message.obj.toString());
                        break;
                }
            }
        };
    }

    /**
     * @brief Liste les joueurs disponibles
     */
    private void
    listerJoueurs()
    {
        Log.d(TAG, "listerJoueurs()");
        joueurs = baseDeDonnees.getJoueurs();
        List<String> nomsJoueurs = new ArrayList<String>();

        for(int i = 0; i < joueurs.size(); ++i)
        {
            Joueur joueur = joueurs.get(i);
            nomsJoueurs.add(joueur.getNom() + " " + joueur.getPrenom());
        }

        afficherJoueurs(nomsJoueurs);

        this.listeJoueurs.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                selectionnerJoueur((CheckedTextView) view, i);
            }
        });
    }

    private void selectionnerJoueur(CheckedTextView view, int i)
    {
        if(listeJoueurs.getCheckedItemCount() > NB_JOUEURS)
        {
            Toast.makeText(IHMNouvelleRencontre.this, "2 Joueurs maximum !", Toast.LENGTH_SHORT).show();
            listeJoueurs.setItemChecked(i, false);
        }
        else
        {
            CheckedTextView v = view;
            boolean estCochee = v.isChecked();
            listeJoueurs.setItemChecked(i, estCochee);
            if(listeJoueurs.getCheckedItemCount() == NB_JOUEURS && peripheriqueBluetooth.estConnecte())
                boutonLancerRencontre.setEnabled(true);
            else
                boutonLancerRencontre.setEnabled(false);
        }
    }

    private void afficherJoueurs(List<String> nomsJoueurs)
    {
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_checked, nomsJoueurs);
        listeJoueurs.setAdapter(adapter);
        this.listeJoueurs.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }
}