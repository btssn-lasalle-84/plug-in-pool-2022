package com.lasalle.pluginpool;

/**
 * @file Rencontre.java
 * @brief Déclaration de la classe Rencontre
 * @author MERAS Pierre
 */

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.*;

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

    /**
     * @brief Constructeur
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Rencontre(int nbManchesGagnantes, int etatRencontre)
    {
        this.nbManchesGagnantes = nbManchesGagnantes;
        this.etatRencontre = etatRencontre;
        this.horodatage = LocalDateTime.now(ZoneId.systemDefault());
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
