package com.lasalle.pluginpool;

/**
 * @file Rencontre.java
 * @brief Déclaration de la classe Rencontre
 * @author MERAS Pierre
 */

import android.os.Handler;
import android.util.Log;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;

/**
 * @class Rencontre
 * @brief Classe pour une rencontre
 */

public class Rencontre implements Serializable
{
    /**
     * Constantes
     */
    private static final int RENCONTRE_ENCOURS = 0;
    private static final int RENCONTRE_FINIE = 1;
    private static boolean NOUVELLE_MANCHE = false;
    private static final int NB_BILLES_COULEUR = 8; // 7 billes rouges ou jaunes et 1 bille noire
    private static final int NB_POCHES = 6;
    private static final String TAG = "_Rencontre_";

    /**
     * Attributs
     */
    private int nbManchesGagnantes;
    private int nbManches;
    private int etatRencontre;
    private Date horodatageDebut;
    private Date horodatage;
    private int dureeRencontreSecondes;
    private Vector<Manche> manches;
    private Vector<Joueur> joueurs;
    private Vector<Coup> coups;
    private int premierJoueur;
    private int deuxiemeJoueur;
    private PeripheriqueBluetooth peripheriqueBluetooth = null;
    private Handler handler = null;

    /**
     * @brief Constructeur
     */
    public Rencontre(Vector<Joueur> joueurs, int nbManchesGagnantes)
    {
        this.nbManchesGagnantes = nbManchesGagnantes;
        this.nbManches = 0;
        this.etatRencontre = RENCONTRE_ENCOURS;
        this.joueurs = joueurs;
        this.coups = new Vector<>();
    }

    public void initialiserJoueurs(int premierJoueur)
    {
        this.premierJoueur = premierJoueur;
        this.deuxiemeJoueur = (premierJoueur == 0) ? 1 : 0;
    }

    public void stockerCoup(String couleur, String blouse)
    {
        Coup coup = new Coup(couleur, blouse);
        coups.add(coup);
        Log.d(TAG, "stockerCoup() : couleur " + couleur + " blouse : " + blouse);
    }

    /**
     * @brief Gère le déroulement de la rencontre
     */
    public void jouerCoup()
    {
        Log.d(TAG, "jouerCoup()");
        String couleur = "";
        String blouse = "";
        if(joueurs.get(premierJoueur).getCouleur().equals("R") || joueurs.get(premierJoueur).getCouleur().equals("J"))
        {
            while(coups.size() > 0)
            {
                couleur = coups.lastElement().getCouleur();
                blouse = coups.lastElement().getBlouse();
                coups.remove(coups.lastElement());
                Log.d(TAG, "jouerCoup() : couleur " + couleur + " blouse : " + blouse);
                if (etatRencontre == RENCONTRE_ENCOURS)
                {
                    if (nbManches < nbManchesGagnantes)
                    {
                        if (couleur.equals(joueurs.get(premierJoueur).getCouleur()))
                        {
                            joueurs.get(0).empocherBille();
                            joueurs.get(0).toucherBille();
                            joueurs.get(0).tirerBille();
                            Log.d(TAG, "jouerCoup() : Joueur Rouge - " + joueurs.get(0).getNbBillesEmpochees() + " billes empochees");
                        }
                        else if (couleur.equals(joueurs.get(deuxiemeJoueur).getCouleur()))
                        {
                            joueurs.get(1).empocherBille();
                            joueurs.get(1).toucherBille();
                            joueurs.get(1).tirerBille();
                            Log.d(TAG, "jouerCoup() : Joueur Jaune - " + joueurs.get(1).getNbBillesEmpochees() + " billes empochees");
                        }

                        if (joueurs.get(premierJoueur).getNbBillesEmpochees() == NB_BILLES_COULEUR || joueurs.get(deuxiemeJoueur).getNbBillesEmpochees() == NB_BILLES_COULEUR)
                        {
                            Log.d(TAG, "jouerCoup() : manche finie");
                            if(joueurs.get(premierJoueur).getNbBillesEmpochees() == NB_BILLES_COULEUR)
                            {
                                joueurs.get(premierJoueur).augmenterNbManchesGagnees();
                            }
                            else if(joueurs.get(deuxiemeJoueur).getNbBillesEmpochees() == NB_BILLES_COULEUR)
                            {
                                joueurs.get(deuxiemeJoueur).augmenterNbManchesGagnees();
                            }
                            terminerManche();
                        }
                    }
                }
            }
        }
    }

    /**
     * @brief Méthode appelée à la fin d'une rencontre
     */
    protected void terminerRencontre()
    {
        Log.d(TAG, "terminerRencontre()");
        etatRencontre = RENCONTRE_FINIE;
        calculerDureeRencontre();
    }

    /**
     * @brief Méthode appelée à la fin d'une manche
     */
    private void terminerManche()
    {
        Log.d(TAG, "terminerManche()");
        nbManches++;
        Log.d(TAG, "terminerManche() : nombre manches : " + nbManches);
        Log.d(TAG, "terminerManche() : nombre manches gagnantes: " + nbManchesGagnantes);
        if(nbManches < nbManchesGagnantes)
        {
            Log.d(TAG, "terminerManche() : resetNbBillesEmpochees()");
            NOUVELLE_MANCHE = true;
            joueurs.get(0).resetNbBillesEmpochees();
            joueurs.get(1).resetNbBillesEmpochees();
        }
        else
        {
            Log.d(TAG, "terminerManche() : terminerRencontre()");
            terminerRencontre();
        }
    }

    /**
     * @brief Méthode appelée à la détection d'une faute
     */
    public void faute(String couleur, String blouse)
    {
        if(couleur.equals(Protocole.JOUEUR_ROUGE))
        {
            joueurs.get(0).tirerBille();
            joueurs.get(0).augmenterFautes();
        }
        else if(couleur.equals(Protocole.JOUEUR_JAUNE))
        {
            joueurs.get(1).tirerBille();
            joueurs.get(1).augmenterFautes();
        }
    }

    /**
     * @brief Accesseurs
     */
    public int getEtatRencontre()
    {
        return this.etatRencontre;
    }

    public int getNbManchesGagnantes()
    {
        return this.nbManchesGagnantes;
    }

    public int getNbManches()
    {
        return this.nbManches;
    }

    public String getHorodatage()
    {
        return String.format("%sm%ss", dureeRencontreSecondes/60, dureeRencontreSecondes%60);
    }

    public Vector<Joueur> getJoueurs()
    {
        return joueurs;
    }

    public void setJoueurs(Vector<Joueur> joueurs)
    {
        this.joueurs = joueurs;
    }

    public int getNbBillesCouleur()
    {
        return this.NB_BILLES_COULEUR - 1;
    }

    public boolean estNouvelleManche()
    {
        return this.NOUVELLE_MANCHE;
    }

    /**
     * @brief Mutateurs
     */
    public void setEtatRencontre(int etatRencontre)
    {
        this.etatRencontre = etatRencontre;
    }

    public void setNbManchesGagnantes(int nbManchesGagnantes)
    {
        this.nbManchesGagnantes = nbManchesGagnantes;
    }

    public void setHorodatageDebut()
    {
        Log.d(TAG, "setHorodatageDebut()");
        this.horodatageDebut = new Date();
    }

    /**
     * @brief Méthode pour calculer la durée d'une rencontre
     */
    public void calculerDureeRencontre()
    {
        Date now = new Date();
        long h = now.getTime() - this.horodatageDebut.getTime();
        Log.d(TAG, "calculerDureeRencontre() : " + h + " ms");
        dureeRencontreSecondes = (int)(h / 1000);
    }

    /**
     * @brief Méthode appelée à la fin d'une manche
     */
    public void changerNouvelleManche()
    {
        NOUVELLE_MANCHE = !NOUVELLE_MANCHE;
    }
}
