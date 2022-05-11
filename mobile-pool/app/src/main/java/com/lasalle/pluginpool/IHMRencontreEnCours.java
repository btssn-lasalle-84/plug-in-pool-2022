package com.lasalle.pluginpool;

/**
 * @file IHMRencontreEnCours.java
 * @brief Déclaration de la classe IHMRencontreEnCours
 * @author MERAS Pierre
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Vector;

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
     * Attributs
     */
    private PeripheriqueBluetooth peripheriqueBluetooth = null;
    private Handler handler = null;
    private Rencontre rencontre = null;

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

        // récupération de données
        rencontre = (Rencontre) getIntent().getSerializableExtra(IHMNouvelleRencontre.ID_INTENT_RENCONTRE);
        // Exemple : les joueurs de cette rencontre
        Vector<Joueur> joueurs = rencontre.getJoueurs();
        for (int i = 0; i < joueurs.size(); i++)
        {
            Log.d(TAG, "[onCreate] Joueur : " + joueurs.get(i).getPrenom() + " " + joueurs.get(i).getNom());
        }

        initialiserRessourcesIHMRencontreEnCours();
        gererHandler();
        initialiserRessourcesBluetooth();
    }

    /**
     * @brief Initialise les ressources bluetooth
     */
    private void initialiserRessourcesBluetooth()
    {
        Log.d(TAG,"initialiserRessourcesBluetooth()");
        peripheriqueBluetooth = PeripheriqueBluetooth.getInstance(handler);
        Log.d(TAG,"[initialiserRessourcesBluetooth] connecte = " + peripheriqueBluetooth.estConnecte());
        if(!peripheriqueBluetooth.estConnecte())
            peripheriqueBluetooth.connecter();
        else
            peripheriqueBluetooth.envoyer(Protocole.trameCommencer);
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
                    // ou : peripheriqueBluetooth.envoyer(Protocole.trameAnnuler);
                    peripheriqueBluetooth.envoyer(Protocole.trameArreter);
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

    /**
     * @brief Gère le handler pour la communication
     */
    private void gererHandler()
    {
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
                        peripheriqueBluetooth.envoyer(Protocole.trameCommencer);
                        break;
                    case PeripheriqueBluetooth.CODE_DECONNEXION_SOCKET:
                        Log.d(TAG, "[Handler] DECONNEXION_SOCKET = " + message.obj.toString());
                        break;
                    case PeripheriqueBluetooth.CODE_RECEPTION_TRAME:
                        Log.d(TAG, "[Handler] RECEPTION_TRAME = " + message.obj.toString());
                        gererMessage(message.obj.toString());
                        break;
                }
            }
        };
    }

    /**
     * @brief Gère le message reçu pour détecter une mauvaise trame
     */
    private void gererMessage(String message)
    {
        // format général : $PLUG;{TYPE};{DONNEES;}\r\n
        // debug
        String[] champs = message.split(Protocole.delimiteurChamp);
        for(int i = 0; i < champs.length; i++)
        {
            Log.v(TAG, "[gererMessage] champs[" + i + "] = " + champs[i]);
        }

        // vérification
        if(!champs[Protocole.CHAMP_ENTETE_TRAME].equals(Protocole.delimiteurDebut))
        {
            Log.d(TAG, "[gererMessage]  Erreur : en tête invalide !");
            return;
        }

        switch (champs[Protocole.CHAMP_TYPE_TRAME])
        {
            case Protocole.EMPOCHE:
                // $PLUG;EMPOCHE;{COULEUR};{BLOUSE};\r\n
                Log.d(TAG, "Trame EMPOCHE : Couleur = " + champs[Protocole.CHAMP_COULEUR] + "-> Blouse = " + champs[Protocole.CHAMP_BLOUSE]);
                rencontre.jouerRencontre(champs[Protocole.CHAMP_COULEUR], champs[Protocole.CHAMP_BLOUSE]);
                break;
            case Protocole.FAUTE:
                // $PLUG;FAUTE;{COULEUR};{BLOUSE};\r\n
                Log.d(TAG, "Trame FAUTE : Couleur = " + champs[Protocole.CHAMP_COULEUR] + " -> Blouse = " + champs[Protocole.CHAMP_BLOUSE]);
                rencontre.faute(champs[Protocole.CHAMP_COULEUR], champs[Protocole.CHAMP_BLOUSE]);
                break;
            case Protocole.SUIVANT:
                //
                Log.d(TAG, "Trame NEXT");
                break;
            case Protocole.ACK:
                //
                Log.d(TAG, "Trame ACK");
                break;
            case Protocole.ERREUR:
                //
                Log.d(TAG, "Trame ERREUR");
                peripheriqueBluetooth.envoyer(Protocole.trameArreter);
                break;
        }
    }
}