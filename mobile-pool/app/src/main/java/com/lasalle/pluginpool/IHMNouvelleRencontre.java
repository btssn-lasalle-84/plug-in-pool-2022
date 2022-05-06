package com.lasalle.pluginpool;

/**
 * @file IHMNouvelleRencontre.java
 * @brief Déclaration de la classe IHMNouvelleRencontre
 * @author MERAS Pierre
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

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
     * Attributs
     */
    private PeripheriqueBluetooth peripheriqueBluetooth = null;
    private BaseDeDonnees baseDeDonnees = null;
    private Handler handler = null;

    /**
     * Ressources IHM
     */
    private Button boutonLancerRencontre;//!< Le bouton permettant d'accèder à l'historique des rencontres
    private ListView listeJoueurs;

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
        //peripheriqueBluetooth.deconnecter();
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
        boutonLancerRencontre.setEnabled(false);
        listeJoueurs = (ListView)findViewById(R.id.listeJoueursParametres);
        ouvrirBaseDeDonnees();
        listerJoueurs();
        boutonLancerRencontre.setOnClickListener(
        new View.OnClickListener()
        {
            public void onClick(View v)
            {
                initialiserRessourcesBluetooth();
                Intent intent = new Intent(IHMNouvelleRencontre.this, IHMRencontreEnCours.class);
                startActivity(intent);
            }
        });
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

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_checked, nomsJoueurs);
        listeJoueurs.setAdapter(adapter);

        this.listeJoueurs.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        this.listeJoueurs.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                CheckedTextView v = (CheckedTextView)view;
                boolean estCochee = v.isChecked();
                listeJoueurs.setItemChecked(i, estCochee);
                Joueur joueur = joueurs.get(i);
                Log.d(TAG, "listerJoueurs() : " + joueur.getNom() + " " + joueur.getPrenom());
            }
        });
    }
}