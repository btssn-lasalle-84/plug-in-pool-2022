package com.lasalle.pluginpool;

/**
 * @file Rencontre.java
 * @brief Déclaration de la classe Rencontre
 * @author MERAS Pierre
 */

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.android.material.tabs.TabLayout;

import java.io.Serializable;
import java.time.*;
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
    private static final int NB_BILLES_COULEUR = 8; // 7 billes rouges ou jaunes et 1 bille noire
    private static final int NB_POCHES = 6;
    private static final String TAG = "_Rencontre_";

    /**
     * Attributs
     */
    private int nbManchesGagnantes;
    private int nbManches;
    private int etatRencontre;
    //private LocalDateTime horodatage;
    private Vector<Manche> manches;
    private Vector<Joueur> joueurs;
    private int joueurEnCours;

    private PeripheriqueBluetooth peripheriqueBluetooth = null;
    private Handler handler = null;

    /**
     * @brief Constructeur
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Rencontre(Vector<Joueur> joueurs, int nbManchesGagnantes)
    {
        this.nbManchesGagnantes = nbManchesGagnantes;
        this.nbManches = 0;
        this.etatRencontre = RENCONTRE_ENCOURS;
        //this.horodatage = LocalDateTime.now(ZoneId.systemDefault());
        this.joueurs = joueurs;
    }

    /**
     * @brief Gère le déroulement de la rencontre
     * @param couleur
     * @param blouse
     */
    public void jouerCoup(String couleur, String blouse)
    {
        Log.d(TAG, "jouerRencontre()");
        if(etatRencontre == RENCONTRE_ENCOURS)
        {
            if(nbManches < nbManchesGagnantes)
            {
                if(couleur.equals(Protocole.JOUEUR_ROUGE))
                {
                    joueurs.get(0).empocherBille();
                    joueurs.get(0).toucherBille();
                    joueurs.get(0).tirerBille();
                    Log.d(TAG, "jouerRencontre() : Joueur Rouge - " + joueurs.get(0).getNbBillesEmpochees() + " billes empochees");
                }
                else if(couleur.equals(Protocole.JOUEUR_JAUNE))
                {
                    joueurs.get(1).empocherBille();
                    joueurs.get(1).toucherBille();
                    joueurs.get(1).tirerBille();
                    Log.d(TAG, "jouerRencontre() : Joueur Jaune - " + joueurs.get(1).getNbBillesEmpochees() + " billes empochees");
                }

                if(joueurs.get(0).getNbBillesEmpochees() == NB_BILLES_COULEUR || joueurs.get(1).getNbBillesEmpochees() == NB_BILLES_COULEUR)
                {
                    Log.d(TAG, "jouerRencontre() : manche finie");
                    rejouerRencontre();
                }
            }
            else
            {
                terminerRencontre();
            }
        }
    }

    /**
     * @brief Méthode appelée à la fin d'une rencontre
     */
    private void terminerRencontre()
    {
        Log.d(TAG, "terminerRencontre()");
        etatRencontre = RENCONTRE_FINIE;
    }

    /**
     * @brief Méthode appelée à la fin d'une manche
     */
    private void rejouerRencontre()
    {
        Log.d(TAG, "rejouerRencontre()");
        joueurs.get(0).resetNbBillesEmpochees();
        joueurs.get(1).resetNbBillesEmpochees();
        nbManches++;
    }

    /**
     * @brief Méthode appelée à la détection d'une faute
     */
    public void faute(String couleur, String blouse)
    {
        if(couleur.equals(Protocole.JOUEUR_ROUGE))
        {
            joueurs.get(0).augmenterFautes();
        }
        else if(couleur.equals(Protocole.JOUEUR_JAUNE))
        {
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

    /*public LocalDateTime getHorodatage()
    {
        return this.horodatage;
    }*/

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

    /*public void setHorodatage(LocalDateTime horodatage)
    {
        this.horodatage = horodatage;
    }*/
}
