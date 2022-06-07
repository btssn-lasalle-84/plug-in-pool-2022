package com.lasalle.pluginpool;

/**
 * @file IHMRencontreEnCours.java
 * @brief Déclaration de la classe IHMRencontreEnCours
 * @author MERAS Pierre
 */

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

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
    private static final int RENCONTRE_ENCOURS = 0;
    private static final int RENCONTRE_FINIE = 1;
    private static final String RENCONTRE = "RENCONTRE";
    private static final String REJOUER = "REJOUER";
    private static final String JOUEURS = "JOUEURS";

    /**
     * Attributs
     */
    private PeripheriqueBluetooth peripheriqueBluetooth = null;
    private BaseDeDonnees baseDeDonnees = null;
    private Handler handler = null;
    private Rencontre rencontre = null;
    Vector<Joueur> joueurs;
    private int premierJoueur;
    private int deuxiemeJoueur;
    private boolean estPremierJoueurChoisi = false;

    /**
     * Ressources IHM
     */
    private Button boutonQuitterRencontre;//!< Le bouton permettant d'arreter la rencontre
    private Button boutonFaute;//!< Le bouton permettant de signaler une faute lors du tour du joueur
    private Button boutonJoueurSuivant;//!< Le bouton permettant de passer la main au joueur suivant
    private Vector<Button> scoreJoueur1 = new Vector<>();
    private Vector<Button> scoreJoueur2 = new Vector<>();
    private TextView texteJoueur1;
    private TextView texteJoueur2;
    private TextView curseur1;
    private TextView curseur2;
    private TextView texteDernierCoup1;
    private TextView texteDernierCoup2;
    private TextView nbManchesGagneesJ1;
    private TextView nbManchesGagneesJ2;

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
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initialiserRessourcesBluetooth()
    {
        Log.d(TAG,"initialiserRessourcesBluetooth()");
        peripheriqueBluetooth = PeripheriqueBluetooth.getInstance(handler);
        Log.d(TAG,"[initialiserRessourcesBluetooth] connecte = " + peripheriqueBluetooth.estConnecte());
        if(!peripheriqueBluetooth.estConnecte())
            peripheriqueBluetooth.connecter();
        else
            demarrerNouvellePartie();
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
     * @brief Démarre une nouvelle partie
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void demarrerNouvellePartie()
    {
        peripheriqueBluetooth.envoyer(Protocole.trameAnnuler);
        peripheriqueBluetooth.envoyer(Protocole.trameCommencer);
        estPremierJoueurChoisi = false;
        joueurs.get(0).setCouleur("");
        joueurs.get(1).setCouleur("");
        curseur1.setVisibility(View.INVISIBLE);
        curseur2.setVisibility(View.INVISIBLE);
        reinitialiserScoreJoueurs();
        afficherManchesGagnees();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void reinitialiserScoreJoueurs()
    {
        for(int i = 0; i < Rencontre.NB_BILLES_COULEUR; ++i)
        {
            scoreJoueur1.get(i).setForeground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bille_blanche));
            scoreJoueur2.get(i).setForeground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bille_blanche));
        }
    }

    private void initialiserScoreJoueurs()
    {
        scoreJoueur1.add((Button)findViewById(R.id.bille1Joueur1));
        scoreJoueur1.add((Button)findViewById(R.id.bille2Joueur1));
        scoreJoueur1.add((Button)findViewById(R.id.bille3Joueur1));
        scoreJoueur1.add((Button)findViewById(R.id.bille4Joueur1));
        scoreJoueur1.add((Button)findViewById(R.id.bille5Joueur1));
        scoreJoueur1.add((Button)findViewById(R.id.bille6Joueur1));
        scoreJoueur1.add((Button)findViewById(R.id.bille7Joueur1));
        scoreJoueur1.add((Button)findViewById(R.id.bille8Joueur1));

        scoreJoueur2.add((Button)findViewById(R.id.bille1Joueur2));
        scoreJoueur2.add((Button)findViewById(R.id.bille2Joueur2));
        scoreJoueur2.add((Button)findViewById(R.id.bille3Joueur2));
        scoreJoueur2.add((Button)findViewById(R.id.bille4Joueur2));
        scoreJoueur2.add((Button)findViewById(R.id.bille5Joueur2));
        scoreJoueur2.add((Button)findViewById(R.id.bille6Joueur2));
        scoreJoueur2.add((Button)findViewById(R.id.bille7Joueur2));
        scoreJoueur2.add((Button)findViewById(R.id.bille8Joueur2));
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
        texteDernierCoup1 = (TextView)findViewById(R.id.texteDernierCoup1);
        texteDernierCoup2 = (TextView)findViewById(R.id.texteDernierCoup2);
        nbManchesGagneesJ1 = (TextView)findViewById(R.id.nbManchesGagneesJ1);
        nbManchesGagneesJ2 = (TextView)findViewById(R.id.nbManchesGagneesJ2);

        initialiserScoreJoueurs();
        listerRessourcesRencontre();

        boutonQuitterRencontre.setOnClickListener(
            new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    Log.d(TAG, "initialiserRessourcesIHMRencontreEnCours() : " + Protocole.trameAnnuler);
                    peripheriqueBluetooth.envoyer(Protocole.trameAnnuler);
                    Intent intent = new Intent(IHMRencontreEnCours.this, IHMNouvelleRencontre.class);
                    startActivity(intent);
                }
            });

        boutonFaute.setOnClickListener(
            new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    Log.d(TAG, "initialiserRessourcesIHMRencontreEnCours() : faute");
                }
            });

        boutonJoueurSuivant.setOnClickListener(
            new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    Log.d(TAG, "initialiserRessourcesIHMRencontreEnCours() : suivant");
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
        curseur1.setVisibility(View.INVISIBLE);
        curseur2 .setVisibility(View.INVISIBLE);
        nbManchesGagneesJ1.setVisibility(View.INVISIBLE);
        nbManchesGagneesJ2.setVisibility(View.INVISIBLE);
        texteDernierCoup1.setText("");
        texteDernierCoup2.setText("");
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
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    private void changerJoueurIHM()
    {
        if(estPremierJoueurChoisi)
        {
            if(curseur1.getVisibility() == View.INVISIBLE)
            {
                curseur1.setVisibility(View.VISIBLE);
                curseur2.setVisibility(View.INVISIBLE);
                joueurs.get(0).tirerBille(); //Si on change de joueur, alors il a forcément loupé son tir sinon il aurait rejoué
                scoreJoueur2.lastElement().setForeground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bille_blanche));
                if(joueurs.get(premierJoueur).getNbBillesEmpochees() >= 7)
                    scoreJoueur1.lastElement().setForeground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bille_noire));
            }
            else if(curseur1.getVisibility() == View.VISIBLE)
            {
                curseur1.setVisibility(View.INVISIBLE);
                curseur2.setVisibility(View.VISIBLE);
                joueurs.get(1).tirerBille();
                scoreJoueur1.lastElement().setForeground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bille_blanche));
                if(joueurs.get(deuxiemeJoueur).getNbBillesEmpochees() >= 7)
                    scoreJoueur2.lastElement().setForeground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bille_noire));
            }
        }
    }

    /**
     * @brief Gère le handler pour la communication
     */
    private void gererHandler()
    {
        this.handler = new Handler(this.getMainLooper())
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
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
                        demarrerNouvellePartie();
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
    @RequiresApi(api = Build.VERSION_CODES.O)
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
                rencontre.stockerCoup(champs[Protocole.CHAMP_COULEUR], champs[Protocole.CHAMP_BLOUSE]);
                if(!estPremierJoueurChoisi)
                {
                    estPremierJoueurChoisi = true;
                    afficherListePremierJoueur(champs);
                }
                rencontre.jouerCoup();
                actualiserScoresBilles();
                actualiserScores(champs);
                terminerRencontre();
                break;
            case Protocole.FAUTE:
                // $PLUG;FAUTE;{COULEUR};{BLOUSE};\r\n
                Log.d(TAG, "Trame FAUTE : Couleur = " + champs[Protocole.CHAMP_COULEUR] + " -> Blouse = " + champs[Protocole.CHAMP_BLOUSE]);
                rencontre.faute(champs[Protocole.CHAMP_COULEUR], champs[Protocole.CHAMP_BLOUSE]);
                actualiserScores(champs);
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

    /**
     * @brief Méthode appelée à chaque empochage d'une bille
     * @param champs
     */
    @SuppressLint({"SetTextI18n", "NewApi"})
    private void actualiserScores(String[] champs)
    {
        Log.d(TAG, "actualiserScores()");
        switch(champs[Protocole.CHAMP_TYPE_TRAME])
        {
            case Protocole.EMPOCHE:
                Log.d(TAG, "actualiserScores() - cas empoche");
                if(curseur1.getVisibility() == View.VISIBLE)
                {
                    Log.d(TAG, "actualiserScores() joueur 1 : " + Protocole.EMPOCHE + " - " + joueurs.get(premierJoueur).getCouleur());
                    texteDernierCoup1.setText("Empochée ! - Blouse : " + champs[Protocole.CHAMP_BLOUSE]);
                    if(joueurs.get(premierJoueur).getNbBillesEmpochees() >= 7)
                    {
                        scoreJoueur1.lastElement().setForeground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bille_noire));
                        scoreJoueur2.lastElement().setForeground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bille_blanche));
                    }
                }
                else if(curseur2.getVisibility() == View.VISIBLE)
                {
                    Log.d(TAG, "actualiserScores() joueur 1 : " + Protocole.EMPOCHE + " - " + joueurs.get(deuxiemeJoueur).getCouleur());
                    texteDernierCoup2.setText("Empochée ! - Blouse : " + champs[Protocole.CHAMP_BLOUSE]);
                    if(joueurs.get(deuxiemeJoueur).getNbBillesEmpochees() >= 7)
                    {
                        scoreJoueur1.lastElement().setForeground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bille_blanche));
                        scoreJoueur2.lastElement().setForeground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bille_noire));
                    }
                }
                break;
            case Protocole.FAUTE:
                Log.d(TAG, "actualiserScores() - cas faute");
                if(curseur1.getVisibility() == View.VISIBLE)
                {
                    Log.d(TAG, "actualiserScores() joueur 1 : " + Protocole.FAUTE + " - " + joueurs.get(premierJoueur).getCouleur());
                    texteDernierCoup1.setText("Faute ! - Blouse : " + champs[Protocole.CHAMP_BLOUSE]);
                    if(joueurs.get(premierJoueur).getNbBillesEmpochees() >= 7)
                    {
                        scoreJoueur1.lastElement().setForeground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bille_noire));
                        scoreJoueur2.lastElement().setForeground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bille_blanche));
                    }
                }
                else if(curseur2.getVisibility() == View.VISIBLE)
                {
                    Log.d(TAG, "actualiserScores() joueur 2 : " + Protocole.FAUTE + " - " + joueurs.get(deuxiemeJoueur).getCouleur());
                    texteDernierCoup2.setText("Faute ! - Blouse : " + champs[Protocole.CHAMP_BLOUSE]);
                    if(joueurs.get(deuxiemeJoueur).getNbBillesEmpochees() >= 7)
                    {
                        scoreJoueur1.lastElement().setForeground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bille_blanche));
                        scoreJoueur2.lastElement().setForeground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bille_noire));
                    }
                }
                break;
        }
        actualiserScoresBilles();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void actualiserScoresBilles()
    {
        if(estPremierJoueurChoisi)
        {
            Log.d(TAG, "actualiserScoresBilles()");
            for(int i = 0; i < joueurs.get(premierJoueur).getNbBillesEmpochees(); ++i)
            {
                Log.d(TAG, "premierJoueur : " + premierJoueur);
                if (joueurs.get(premierJoueur).getNbBillesEmpochees() < 8)
                {
                    if(joueurs.get(0).getCouleur().equals("R"))
                    {
                        Log.d(TAG, "actualiserScoresBilles() : " + joueurs.get(0).getCouleur() + " J1 Rouge");
                        scoreJoueur1.get(i).setForeground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bille_rouge));
                    }
                    else if(joueurs.get(0).getCouleur().equals("J"))
                    {
                        Log.d(TAG, "actualiserScoresBilles() : " + joueurs.get(0).getCouleur() + " J1 Jaune");
                        scoreJoueur1.get(i).setForeground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bille_jaune));
                    }
                }
            }

            for(int i = 0; i < joueurs.get(deuxiemeJoueur).getNbBillesEmpochees(); ++i)
            {
                Log.d(TAG, "deuxiemeJoueur : " + deuxiemeJoueur);
                if (joueurs.get(deuxiemeJoueur).getNbBillesEmpochees() < 8)
                {
                    if(joueurs.get(0).getCouleur().equals("J"))
                    {
                        Log.d(TAG, "actualiserScoresBilles() : " + joueurs.get(1).getCouleur() + " J2 Rouge");
                        scoreJoueur2.get(i).setForeground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bille_rouge));
                    }
                    else if(joueurs.get(0).getCouleur().equals("R"))
                    {
                        Log.d(TAG, "actualiserScoresBilles() : " + joueurs.get(1).getCouleur() + " J2 Jaune");
                        scoreJoueur2.get(i).setForeground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bille_jaune));
                    }
                }
            }
        }
    }

    /**
     * @brief Affiche la liste des joueurs pour sélectionner le premier joueur
     * @param champs
     */
    private void afficherListePremierJoueur(String[] champs)
    {
        Log.d(TAG, "afficherListePremierJoueur()");
        if(joueurs.get(JOUEUR_1).getNbBillesEmpochees() == 0 && joueurs.get(JOUEUR_2).getNbBillesEmpochees() == 0)
        {
            final String[] listeJoueurs = {joueurs.get(JOUEUR_1).getNom() + " " + joueurs.get(JOUEUR_1).getPrenom(),
                                           joueurs.get(JOUEUR_2).getNom() + " " + joueurs.get(JOUEUR_2).getPrenom()};
            new AlertDialog.Builder(IHMRencontreEnCours.this, R.style.Theme_PlugInPool_BoiteDialogue)
                .setTitle("Qui a empoché la première bille ?")
                .setSingleChoiceItems(listeJoueurs, listeJoueurs.length, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        Log.d(TAG, "Première couleur : " + champs[Protocole.CHAMP_COULEUR]);
                        if(champs[Protocole.CHAMP_COULEUR].equals(Protocole.JOUEUR_ROUGE))
                        {
                            joueurs.get(i).setCouleur(Protocole.JOUEUR_ROUGE);
                            joueurs.get((i == 0) ? 1 : 0).setCouleur(Protocole.JOUEUR_JAUNE);
                        }
                        else if(champs[Protocole.CHAMP_COULEUR].equals(Protocole.JOUEUR_JAUNE))
                        {
                            joueurs.get(i).setCouleur(Protocole.JOUEUR_JAUNE);
                            joueurs.get((i == 0) ? 1 : 0).setCouleur(Protocole.JOUEUR_ROUGE);
                        }
                        premierJoueur = i;
                        deuxiemeJoueur = (premierJoueur == 0) ? 1 : 0;
                        Log.d(TAG, "Premier joueur : " + joueurs.get(premierJoueur).getNom() + " " + joueurs.get(premierJoueur).getPrenom() + " Couleur : " + joueurs.get(premierJoueur).getCouleur() + " " + premierJoueur);
                        Log.d(TAG, "Deuxième joueur : " + joueurs.get(deuxiemeJoueur).getNom() + " " + joueurs.get(deuxiemeJoueur).getPrenom() + " Couleur : " + joueurs.get(deuxiemeJoueur).getCouleur() + " " + deuxiemeJoueur);
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        rencontre.initialiserJoueurs(premierJoueur);
                        initialiserScores();
                    }
                }).show();
        }
    }

    /**
     * @brief Méthode appelée à la fin d'une manche afin de commencer une nouvelle manche ou bien de finir la rencontre
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void terminerRencontre()
    {
        Log.d(TAG, "terminerRencontre()");
        if(rencontre.estNouvelleManche())
        {
            demarrerNouvellePartie();
            rencontre.changerNouvelleManche();
        }
        else if(rencontre.getEtatRencontre() == RENCONTRE_FINIE)
        {
            Intent intent = new Intent(IHMRencontreEnCours.this, IHMFinDeRencontre.class);
            intent.putExtra(RENCONTRE, rencontre);
            startActivity(intent);
            finish();
        }
    }

    /**
     * @brief Permet d'afficher le nombre de manches gagnées par les joueurs
     */
    private void afficherManchesGagnees()
    {
        Log.d(TAG, "afficherManchesGagnees()");
        if(joueurs.get(0).getNbManchesGagnees() > 0)
        {
            nbManchesGagneesJ1.setText(joueurs.get(0).getNbManchesGagnees() + " manche(s) gagnée(s) sur " + rencontre.getNbManchesGagnantes() + " !");
            nbManchesGagneesJ1.setVisibility(View.VISIBLE);
        }

        if(joueurs.get(1).getNbManchesGagnees() > 0)
        {
            nbManchesGagneesJ2.setText(joueurs.get(1).getNbManchesGagnees() + " manche(s) gagnée(s) sur " + rencontre.getNbManchesGagnantes() + " !");
            nbManchesGagneesJ2.setVisibility(View.VISIBLE);
        }
    }
}