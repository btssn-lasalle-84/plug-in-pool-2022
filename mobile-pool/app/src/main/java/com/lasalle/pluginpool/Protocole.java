package com.lasalle.pluginpool;

/**
 * @file Protocole.java
 * @brief Déclaration de la classe Protocole
 * @author MERAS Pierre
 */

/**
 * @class Protocole
 * @brief Définition du protocole Plug In Pool
 */
public class Protocole
{
    public static final String delimiteurDebut = "$PLUG";
    public static final String delimiteurChamp = ";";
    public static final String delimiteurFin = "\r\n";

    public static final String trameArreter = "$PLUG;STOP;\r\n";
    public static final String trameCommencer = "$PLUG;START;\r\n";
    public static final String trameAnnuler = "$PLUG;RESET;\r\n";
    public static final String trameAcquitemment = "$PLUG;ACK;\r\n";

    public static final String EMPOCHE = "EMPOCHE";
    public static final String FAUTE = "FAUTE";
    public static final String SUIVANT = "NEXT";
    public static final String ACK = "ACK";
    public static final String ERREUR = "ERREUR";

    public static final int CHAMP_ENTETE_TRAME = 0;
    public static final int CHAMP_TYPE_TRAME = 1;
    public static final int CHAMP_COULEUR = 2;
    public static final int CHAMP_BLOUSE = 3;

    public static final int CODE_ERREUR_TRAME_INCONNUE = 0;
    public static final int CODE_ERREUR_TRAME_NONSUPPORTEE = 1;
}
