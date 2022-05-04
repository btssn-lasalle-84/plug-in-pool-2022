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

    public static final int CODE_ERREUR_TRAME_INCONNUE = 0;
    public static final int CODE_ERREUR_TRAME_NONSUPPORTEE = 1;
}
