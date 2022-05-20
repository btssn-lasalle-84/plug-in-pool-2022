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
    private String couleur;
    private int nbManchesGagnees = 0;
    private int nbBillesEmpochees = 0;
    private int nbBillesTouchees = 0;
    private int nbCoupsTires = 0;
    private int nbFautes = 0;

    /**
     * @brief Constructeur
     */
    public Joueur(String nom, String prenom)
    {
        this.nom = nom;
        this.prenom = prenom;
        this.couleur = "";
    }

    /**
     * @brief Accesseurs
     */
    public String getNom()
    {
        return this.nom;
    }

    public String getPrenom()
    {
        return this.prenom;
    }

    public String getCouleur()
    {
        return couleur;
    }

    public int getNbBillesEmpochees()
    {
        return nbBillesEmpochees;
    }

    public int getNbBillesTouchees()
    {
        return nbBillesTouchees;
    }

    public int getNbFautes()
    {
        return nbFautes;
    }

    public int getNbCoupsTires()
    {
        return nbCoupsTires;
    }

    public int getNbManchesGagnees()
    {
        return nbManchesGagnees;
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

    public void setCouleur(String couleur)
    {
        this.couleur = couleur;
    }

    public void empocherBille()
    {
        ++this.nbBillesEmpochees;
    }

    public void augmenterFautes()
    {
        ++this.nbFautes;
    }

    public void toucherBille()
    {
        ++this.nbBillesTouchees;
    }

    public void tirerBille()
    {
        ++this.nbCoupsTires;
    }

    public void resetNbBillesEmpochees()
    {
        this.nbBillesEmpochees = 0;
    }

    public void augmenterNbManchesGagnees()
    {
        this.nbManchesGagnees++;
    }
}
