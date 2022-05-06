package com.lasalle.pluginpool;

/**
 * @file Rencontre.java
 * @brief Déclaration de la classe Rencontre
 * @author MERAS Pierre
 */

import android.os.Build;
import android.os.Handler;

import androidx.annotation.RequiresApi;

import java.time.*;
import java.util.Vector;

/**
 * @class Rencontre
 * @brief Classe pour une rencontre
 */

public class Rencontre
{
    /**
     * Constantes
     */
    private static final int RENCONTRE_ENCOURS = 0;
    private static final int RENCONTRE_FINIE = 1;
    /**
     * Attributs
     */
    private int nbManchesGagnantes;
    private int nbManches;
    private int etatRencontre;
    private LocalDateTime horodatage;
    private Vector<Manche> manches;
    private Vector<Joueur> joueurs;
    private BaseDeDonnees baseDeDonnees = null;
    private Handler handler;

    /**
     * @brief Constructeur
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Rencontre(Vector<Joueur> joueurs, int nbManchesGagnantes)
    {
        this.nbManchesGagnantes = nbManchesGagnantes;
        this.nbManches = 0;
        this.etatRencontre = RENCONTRE_ENCOURS;
        this.horodatage = LocalDateTime.now(ZoneId.systemDefault());
        this.joueurs = new Vector<>(joueurs);
    }

    /**
     * @brief Initialise les ressources de la rencontre
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void initialiserRencontre()
    {
        /**
         * @todo Changer les getJoueurs() et getNbManchesGagnates() avec les valeurs de l'ihm
         */
        //etatRencontre = RENCONTRE_ENCOURS;
    }

    /**
     * @brief Gère le déroulement de la rencontre
     * @param couleur
     * @param blouse
     */
    public void jouerRencontre(String couleur, String blouse)
    {
        if(etatRencontre == RENCONTRE_ENCOURS)
        {
            if(nbManches < nbManchesGagnantes)
            {

            }
            else
            {

            }
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

    public LocalDateTime getHorodatage()
    {
        return this.horodatage;
    }

    public Vector<Joueur> getJoueurs()
    {
        return joueurs;
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

    public void setHorodatage(LocalDateTime horodatage)
    {
        this.horodatage = horodatage;
    }
}
