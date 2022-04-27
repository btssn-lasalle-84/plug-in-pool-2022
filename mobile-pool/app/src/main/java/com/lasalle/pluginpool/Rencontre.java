package com.lasalle.pluginpool;

/**
 * @file Rencontre.java
 * @brief Déclaration de la classe Rencontre
 * @author MERAS Pierre
 */

import android.os.Build;

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
     * Variables
     */
    private int nbManchesGagnantes;
    private int etatRencontre;
    private LocalDateTime horodatage;
    private Joueur joueur1;
    private Joueur joueur2;

    /**
     * @brief Constructeur
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Rencontre(int nbManchesGagnantes, int etatRencontre, Joueur joueur1, Joueur joueur2)
    {
        this.nbManchesGagnantes = nbManchesGagnantes;
        this.etatRencontre = etatRencontre;
        this.horodatage = LocalDateTime.now(ZoneId.systemDefault());
        this.joueur1 = joueur1;
        this.joueur2 = joueur2;
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

    public Joueur getJoueur1()
    {
        return this.joueur1;
    }

    public Joueur getJoueur2()
    {
        return this.joueur2;
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

    public void setJoueur1(Joueur joueur1)
    {
        this.joueur1 = joueur1;
    }

    public void setJoueur2(Joueur joueur2)
    {
        this.joueur2 = joueur2;
    }
}
