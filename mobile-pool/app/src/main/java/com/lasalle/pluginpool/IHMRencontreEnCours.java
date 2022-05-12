package com.lasalle.pluginpool;

/**
 * @file IHMRencontreEnCours.java
 * @brief Déclaration de la classe IHMRencontreEnCours
 * @author MERAS Pierre
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
    private static final int JOUEUR_1 = 0;  //!< indice du joueur n°1
    private static final int JOUEUR_2 = 1;  //!< indice du joueur n°2

    /**
     * Attributs
     */
    private PeripheriqueBluetooth peripheriqueBluetooth = null;
    private BaseDeDonnees baseDeDonnees = null;
    private Handler handler = null;
    private Rencontre rencontre = null;
    Vector<Joueur> joueurs;
    private int premierJoueur;

    /**
     * Ressources IHM
     */
    private Button boutonQuitterRencontre;//!< Le bouton permettant d'arreter la rencontre
    private Button boutonFaute;//!< Le bouton permettant de signaler une faute lors du tour du joueur
    private Button boutonJoueurSuivant;//!< Le bouton permettant de passer la main au joueur suivant
    private TextView texteJoueur1;
    private TextView texteJoueur2;
    private TextView curseur1;
    private TextView curseur2;

    /**
     * @brief Méthode appelée à la création de l'activité
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ihm_rencontre_en_cours);
        Log.d(TAG, "onCreate()");

        // Récupération de données
        rencontre = (Rencontre) getIntent().getSerializableExtra(IHMNouvelleRencontre.ID_INTENT_RENCONTRE);
        // Exemple : les joueurs de cette rencontre
        joueurs = rencontre.getJoueurs();
        for (int i = 0; i < joueurs.size(); i++)
        {
            Log.d(TAG, "[onCreate] Joueur : " + joueurs.get(i).getPrenom() + " " + joueurs.get(i).getNom());
        }

        ouvrirBaseDeDonnees();
        initialiserRessourcesIHMRencontreEnCours();
        gererHandler();
        initialiserRessourcesBluetooth();
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
        texteJoueur1 = (TextView)findViewById(R.id.texteJoueur1);
        texteJoueur2 = (TextView)findViewById(R.id.texteJoueur2);
        curseur1 = (TextView)findViewById(R.id.curseur1);
        curseur2 = (TextView)findViewById(R.id.curseur2);

        listerRessourcesRencontre();

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
     * @brief Liste les joueurs et leurs scores
     */
    @SuppressLint("SetTextI18n")
    private void listerRessourcesRencontre()
    {
        texteJoueur1.setText(rencontre.getJoueurs().get(JOUEUR_1).getNom() + " " + rencontre.getJoueurs().get(JOUEUR_1).getPrenom());
        texteJoueur2.setText(rencontre.getJoueurs().get(JOUEUR_2).getNom() + " " + rencontre.getJoueurs().get(JOUEUR_2).getPrenom());
    }

    /**
     * @brief Initialise les scores des joueurs
     */
    private void initialiserScores()
    {
        if(premierJoueur == JOUEUR_1)
        {
            curseur1.setVisibility(View.VISIBLE);
            curseur2.setVisibility(View.INVISIBLE);
        }
        else if(premierJoueur == JOUEUR_2)
        {
            curseur1.setVisibility(View.INVISIBLE);
            curseur2.setVisibility(View.VISIBLE);
        }
    }

    /**
    * @brief Change le curseur du joueur en cours sur l'IHM
    */
    @SuppressLint("SetTextI18n")
    private void changerJoueurIHM()
    {
        if(curseur1.getVisibility() == View.INVISIBLE)
        {
            curseur1.setVisibility(View.VISIBLE);
            curseur2.setVisibility(View.INVISIBLE);
        }
        else if(curseur2.getVisibility() == View.INVISIBLE)
        {
            curseur1.setVisibility(View.INVISIBLE);
            curseur2.setVisibility(View.VISIBLE);
        }
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
                Log.d(TAG, "Trame EMPOCHE : Couleur = " + champs[Protocole.CHAMP_COULEUR] + " -> Blouse = " + champs[Protocole.CHAMP_BLOUSE]);
                if(joueurs.get(JOUEUR_1).getNbBillesEmpochees() == 0 && joueurs.get(JOUEUR_2).getNbBillesEmpochees() == 0)
                {
                    final String[] listeJoueurs = {joueurs.get(JOUEUR_1).getNom() + " " + joueurs.get(JOUEUR_1).getPrenom(),
                                                   joueurs.get(JOUEUR_2).getNom() + " " + joueurs.get(JOUEUR_2).getPrenom()};
                    new AlertDialog.Builder(IHMRencontreEnCours.this, R.style.Theme_PlugInPool_BoiteDialogue)
                        .setTitle("Qui a rentré la première bille ?")
                        .setSingleChoiceItems(listeJoueurs, listeJoueurs.length, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                if(champs[Protocole.CHAMP_COULEUR].equals(Protocole.JOUEUR_ROUGE))
                                {
                                    joueurs.get(i).setCouleur(Protocole.JOUEUR_ROUGE);
                                    joueurs.get(JOUEUR_2).setCouleur(Protocole.JOUEUR_JAUNE);
                                }
                                else if(champs[Protocole.CHAMP_COULEUR].equals(Protocole.JOUEUR_JAUNE))
                                {
                                    joueurs.get(JOUEUR_1).setCouleur(Protocole.JOUEUR_JAUNE);
                                    joueurs.get(i).setCouleur(Protocole.JOUEUR_ROUGE);
                                }
                                premierJoueur = i;
                                Log.d(TAG, joueurs.get(JOUEUR_1).getNom() + " " + joueurs.get(JOUEUR_1).getPrenom() + " Couleur : " + joueurs.get(JOUEUR_1).getCouleur());
                                Log.d(TAG, joueurs.get(JOUEUR_2).getNom() + " " + joueurs.get(JOUEUR_2).getPrenom() + " Couleur : " + joueurs.get(JOUEUR_2).getCouleur());
                            }
                        })
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                initialiserScores();
                            }
                        }).show();
                }
                rencontre.jouerCoup(champs[Protocole.CHAMP_COULEUR], champs[Protocole.CHAMP_BLOUSE]);
                break;
            case Protocole.FAUTE:
                // $PLUG;FAUTE;{COULEUR};{BLOUSE};\r\n
                Log.d(TAG, "Trame FAUTE : Couleur = " + champs[Protocole.CHAMP_COULEUR] + " -> Blouse = " + champs[Protocole.CHAMP_BLOUSE]);
                rencontre.faute(champs[Protocole.CHAMP_COULEUR], champs[Protocole.CHAMP_BLOUSE]);
                break;
            case Protocole.SUIVANT:
                // $PLUG;NEXT;
                changerJoueurIHM();
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