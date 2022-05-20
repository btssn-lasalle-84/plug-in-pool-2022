package com.lasalle.pluginpool;

/**
 * @file Coup.java
 * @brief Déclaration de la classe Coup
 * @author MERAS Pierre
 */

/**
 * @class Coup
 * @brief Classe pour un Coup
 */

public class Coup
{
    /**
     * Attributs
     */
    private String couleur;
    private String blouse;

    /**
     * @brief Constructeur
     */
    public Coup(String couleur, String blouse)
    {
        this.couleur = couleur;
        this.blouse = blouse;
    }

    public String getCouleur()
    {
        return this.couleur;
    }

    public String getBlouse()
    {
        return blouse;
    }
}
