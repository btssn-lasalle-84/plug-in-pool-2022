package com.lasalle.pluginpool;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Date;
import java.util.Vector;

/**
 * @file BaseDeDonnees.java
 * @brief Déclaration de la classe BaseDeDonnees
 * @author Thierry VAIRA
 */

/**
 * @class BaseDeDonnees
 * @brief Classe permettant de manipuler la base de données
 */

public class BaseDeDonnees
{
    private static final String TAG = "_BaseDeDonnees_";//<! TAG pour les logs
    private SQLiteDatabase bdd = null;//<! L'accès à la base de données
    private SQLite sqlite = null;//<! Permet l'initialisation de la base de données

    /**
     * Constantes
     */
    private static final int INDEX_ID_JOUEUR = 0;
    private static final int INDEX_NOM_JOUEUR = 1;
    private static final int INDEX_PRENOM_JOUEUR = 2;

    private static final int INDEX_ID_RENCONTRE = 0;
    private static final int INDEX_ID_JOUEUR_1 = 1;
    private static final int INDEX_ID_JOUEUR_2 = 2;
    private static final int INDEX_NB_MANCHES_GAGNANTES = 3;
    private static final int INDEX_FINI = 4;
    private static final int INDEX_HORODATAGE = 5;
    
    private static final int INDEX_ID_MANCHE = 0;
    private static final int INDEX_FK_RENCONTRE = 1;
    private static final int INDEX_POINTS_JOUEUR_1 = 2;
    private static final int INDEX_POINTS_JOUEUR_2 = 3;
    private static final int INDEX_DEBUT = 4;
    private static final int INDEX_FIN = 5;

    private static final String DEBUT_REQUETE_INSERTION_MANCHE =  "INSERT INTO Manche(idRencontre, pointsJoueur1, pointsJoueur2, debut, fin) VALUES (";
    private static final String DEBUT_REQUETE_TERMINER_MANCHE = "UPDATE Manche SET fin=DATETIME('now') WHERE idManche=";
    private static final String DEBUT_REQUETE_INSERTION_RENCONTRE = "INSERT INTO Rencontre(idJoueur1, idJoueur2, nbManchesGagnantes, fini, horodatage) VALUES (";
    private static final String FIN_REQUETE_INSERTION_RENCONTRE = "0,DATETIME('now'))";
    private static final String REQUETE_ID_RENCONTRE = "SELECT MAX(idRencontre) FROM Rencontre";
    private static final String DEBUT_REQUETE_INSERTION_JOUEUR = "INSERT INTO Joueur(nom, prenom) VALUES ('";
    private static final String DEBUT_REQUETE_SUPPRESSION_JOUEUR = "DELETE FROM Joueur WHERE nom='";
    private static final String DEBUT_REQUETE_SELECTION_JOUEUR = "SELECT idJoueur FROM Joueur WHERE nom='";

    /**
     * @brief Constructeur de la classe BaseDeDonnees
     * @param context le contexte dans lequel l'objet est créé
     */
    public BaseDeDonnees(Context context)
    {
        this.sqlite = new SQLite(context);
    }

    /**
     * @brief Ouvre un accés à la base de données
     */
    protected void ouvrir()
    {
        Log.d(TAG, "ouvrir()");
        if (bdd == null)
            bdd = sqlite.getWritableDatabase();
    }

    /**
     * @brief Ferme l'accés à la base de données
     */
    private void fermer()
    {
        Log.d(TAG, "fermer()");
        if (bdd != null)
            if (bdd.isOpen())
                bdd.close();
    }

    /**
     * @brief Permet d'effectuer une requete sur la base de données
     * @param requete la requete a éffectuer
     */
    private Cursor effectuerRequete(String requete)
    {
        ouvrir();

        Cursor curseurResultat = bdd.rawQuery(requete,null);
        Log.d(TAG,"effectuerRequete() : Exécution de la requete : " + requete);
        Log.d(TAG,"effectuerRequete() : Nombre de résultats : " + Integer.toString(curseurResultat.getCount()));

        return curseurResultat;
    }

    /**
     * @brief Permet d'effectuer une requete de type SELECT pour récupérer tous les joueurs
     * @return Les objets joueurs récupérés
     */
    public Vector<Joueur> getJoueurs()
    {
        Vector<Joueur> joueurs = new Vector<Joueur>();
        String requete = "SELECT * FROM Joueur";

        Cursor curseurResultat = effectuerRequete(requete);

        for (int i = 0; i < curseurResultat.getCount(); i++)
        {
            curseurResultat.moveToNext();
            Log.d(TAG, "nom = " + curseurResultat.getString(INDEX_NOM_JOUEUR) + " - " + "prenom = " + curseurResultat.getString(INDEX_PRENOM_JOUEUR));
            joueurs.add(new Joueur(curseurResultat.getString(INDEX_NOM_JOUEUR),curseurResultat.getString(INDEX_PRENOM_JOUEUR)));
        }

        return joueurs;
    }


    /**
     * @brief Permet d'effectuer une requete pour insérer un joueur
     * @param joueur Le joueur à insérer
     */
    public void insererJoueur(Joueur joueur)
    {
        ouvrir();
        bdd.execSQL(DEBUT_REQUETE_INSERTION_JOUEUR + joueur.getNom() + "','" + joueur.getPrenom() + "')");
    }
    /**
     * @brief Permet d'effectuer une requete pour supprimer un joueur
     * @param joueur Le joueur à supprimer
     */
    public void supprimerJoueur(Joueur joueur)
    {
        ouvrir();
        bdd.execSQL(DEBUT_REQUETE_SUPPRESSION_JOUEUR + joueur.getNom() + "' AND prenom='" + joueur.getPrenom() + "'");
    }

    /**
     * @brief Permet d'effectuer une requete terminer une manche
     * @param idManche La manche à terminer
     */
    public void terminerManche(int idManche)
    {
        ouvrir();
        String requete = DEBUT_REQUETE_TERMINER_MANCHE + Integer.toString(idManche);
        bdd.execSQL(requete);
    }

    /**
     * @brief Permet d'effectuer une requete SQL de type INSERT, UPDATE ou DELETE
     * @param requete La requete SQL
     */
    public void executerRequete(String requete)
    {
        ouvrir();
        bdd.execSQL(requete);
    }

    /**
     * @brief Permet d'enregistrer la rencontre une fois commencée
     * @param joueur1
     * @param joueur2
     * @param nbManchesGagnantes
     */
    public void enregistrerRencontre(Joueur joueur1, Joueur joueur2, int nbManchesGagnantes)
    {
        ouvrir();
        String requete = DEBUT_REQUETE_INSERTION_RENCONTRE + chercherIDJoueur(joueur1) + "," + chercherIDJoueur(joueur2) + "," + nbManchesGagnantes + "," + FIN_REQUETE_INSERTION_RENCONTRE;
        bdd.execSQL(requete);
    }

    public void enregistrerManche(Manche manche)
    {
        ouvrir();
        String requete = DEBUT_REQUETE_INSERTION_MANCHE + chercherIDRencontre() + "," + manche.getPointsJoueur1() + "," + manche.getPointsJoueur2() + "," + new java.sql.Date(manche.getDebut().getTime()) + "," + new java.sql.Date(manche.getFin().getTime()) + ");";
        bdd.execSQL(requete);
    }

    /**
     * @brief Méthode pour récupérer l'id de la dernière rencontre
     * @return l'id de la dernière rencontre
     */
    @SuppressLint("Recycle")
    public int chercherIDRencontre()
    {
        ouvrir();
        Cursor curseurResultat = bdd.rawQuery(REQUETE_ID_RENCONTRE,null);
        curseurResultat.moveToNext();
        return curseurResultat.getInt(INDEX_ID_RENCONTRE);
    }

    /**
     * @brief Méthode pour récupérer l'id d'un joueur
     * @param joueur
     * @return l'id du joueur
     */
    @SuppressLint("Recycle")
    public int chercherIDJoueur(Joueur joueur)
    {
        ouvrir();
        String requete = DEBUT_REQUETE_SELECTION_JOUEUR + joueur.getNom() + "' AND prenom='" + joueur.getPrenom() + "';";
        Cursor curseurResultat = bdd.rawQuery(requete,null);
        curseurResultat.moveToNext();
        return curseurResultat.getInt(INDEX_ID_JOUEUR);
    }
}
