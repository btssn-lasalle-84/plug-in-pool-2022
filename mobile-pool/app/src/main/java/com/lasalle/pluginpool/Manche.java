package com.lasalle.pluginpool;

/**
 * @file Manche.java
 * @brief Déclaration de la classe Manche
 * @author MERAS Pierre
 */

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.*;

/**
 * @class Manche
 * @brief Classe pour une Manche
 */

public class Manche
{
    /**
     * Variables
     */
    private int pointsJoueur1;
    private int pointsJoueur2;
    private LocalDateTime debut;
    private LocalDateTime fin;

    /**
     * @brief Constructeur
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Manche(int pointsJoueur1, int pointsJoueur2)
    {
        this.pointsJoueur1 = pointsJoueur1;
        this.pointsJoueur2 = pointsJoueur2;
        this.debut = LocalDateTime.now(ZoneId.systemDefault());
        this.fin = LocalDateTime.now(ZoneId.systemDefault()); /*a changer*/
    }

    /**
     * @brief Accesseurs
     */

    public int getPointsJoueur1()
    {
        return this.pointsJoueur1;
    }

    public int getPointsJoueur2()
    {
        return this.pointsJoueur2;
    }

    public LocalDateTime getDebut()
    {
        return this.debut;
    }

    public LocalDateTime getFin()
    {
        return this.fin;
    }

    /**
     * @brief Mutateurs
     */

    public void setPointsJoueur1(int pointsJoueur1)
    {
        this.pointsJoueur1 = pointsJoueur1;
    }

    public void setPointsJoueur2(int pointsJoueur2)
    {
        this.pointsJoueur2 = pointsJoueur2;
    }

    public void setDebut(LocalDateTime debut)
    {
        this.debut = debut;
    }

    public void setFin(LocalDateTime fin)
    {
        this.fin = fin;
    }
}