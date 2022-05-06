package com.lasalle.pluginpool;

/**
 * @file Joueur.java
 * @brief Déclaration de la classe Joueur
 * @author MERAS Pierre
 */

import java.io.Serializable;

/**
 * @class Joueur
 * @brief Classe pour un Joueur
 */

public class Joueur implements Serializable
{
    /**
     * Variables
     */
    private String nom;
    private String prenom;
    private int nbBillesEmpochees = 0;
    private int nbBillesTouchees = 0;
    private int nbFautes = 0;

    /**
     * @brief Constructeur
     */
    public Joueur(String nom, String prenom)
    {
        this.nom = nom;
        this.prenom = prenom;
    }

    /**
     * @brief Accesseurs
     */
    public  String getNom()
    {
        return this.nom;
    }

    public  String getPrenom()
    {
        return this.prenom;
    }

    /**
     * @brief Mutateurs
     */
    public void setNom(String nom)
    {
        this.nom = nom;
    }

    public void setPrenom(String prenom)
    {
        this.prenom = prenom;
    }
}
