package com.lasalle.pluginpool;

/**
 * @file Manche.java
 * @brief Déclaration de la classe Manche
 * @author MERAS Pierre
 */

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.time.*;
import java.util.Date;

/**
 * @class Manche
 * @brief Classe pour une Manche
 */

public class Manche implements Serializable
{
    private static final String TAG = "_Manche_";
    /**
     * Variables
     */
    private int pointsJoueur1;
    private int pointsJoueur2;
    private double precisionJoueur1;
    private double precisionJoueur2;
    private Date debut;
    private Date fin;

    /**
     * @brief Constructeur
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Manche(int pointsJoueur1, int pointsJoueur2, double precisionJoueur1, double precisionJoueur2, Date debut, Date fin)
    {
        this.pointsJoueur1 = pointsJoueur1;
        this.pointsJoueur2 = pointsJoueur2;
        this.precisionJoueur1 = precisionJoueur1;
        this.precisionJoueur2 = precisionJoueur2;
        this.debut = debut;
        this.fin = fin;
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

    public double getPrecisionJoueur1()
    {
        return precisionJoueur1;
    }

    public double getPrecisionJoueur2()
    {
        return precisionJoueur2;
    }

    public Date getDebut()
    {
        return this.debut;
    }

    public Date getFin()
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

    public void setPrecisionJoueur1(double precisionJoueur1)
    {
        this.precisionJoueur1 = precisionJoueur1;
    }

    public void setPrecisionJoueur2(double precisionJoueur2)
    {
        this.precisionJoueur2 = precisionJoueur2;
    }

    public void setHorodatageFin()
    {
        this.fin = new Date();
        Log.d(TAG, "setHorodatageFin() : " + fin + " ms");
    }

    public void setHorodatageDebut()
    {
        this.debut = new Date();
    }
}