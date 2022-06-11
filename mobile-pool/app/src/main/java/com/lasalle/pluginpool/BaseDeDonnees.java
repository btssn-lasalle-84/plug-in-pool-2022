package com.lasalle.pluginpool;

import android.annotation.SuppressLint;
import androidx.annotation.RequiresApi;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import java.text.DecimalFormat;
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
    public static final int ID_RENCONTRE_DEFAUT = -1;

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
    private static final int INDEX_PRECISION_JOUEUR_1 = 4;
    private static final int INDEX_PRECISION_JOUEUR_2 = 5;
    private static final int INDEX_DEBUT = 6;
    private static final int INDEX_FIN = 7;

    private static final String DEBUT_REQUETE_INSERTION_MANCHE =  "INSERT INTO Manche(idRencontre, pointsJoueur1, pointsJoueur2, precisionJoueur1, precisionJoueur2, debut, fin) VALUES (";
    private static final String DEBUT_REQUETE_TERMINER_MANCHE = "UPDATE Manche SET fin=DATETIME('now') WHERE idManche=";
    private static final String DEBUT_REQUETE_INSERTION_RENCONTRE = "INSERT INTO Rencontre(idJoueur1, idJoueur2, nbManchesGagnantes, fini, horodatage) VALUES (";
    private static final String DEBUT_REQUETE_SUPPRESSION_RENCONTRE = "DELETE FROM Rencontre WHERE Rencontre.idRencontre=";
    private static final String FIN_REQUETE_INSERTION_RENCONTRE = "0,DATETIME('now'))";
    private static final String REQUETE_ID_RENCONTRE = "SELECT MAX(idRencontre) FROM Rencontre";
    private static final String REQUETE_RENCONTRES = "SELECT * FROM Rencontre;";
    private static final String REQUETE_PURGE_RENCONTRES = "DELETE FROM Rencontre;";
    private static final String REQUETE_MANCHES = "SELECT * FROM Manche WHERE idRencontre=";
    private static final String DEBUT_REQUETE_INSERTION_JOUEUR = "INSERT INTO Joueur(nom, prenom) VALUES ('";
    private static final String DEBUT_REQUETE_SUPPRESSION_JOUEUR = "DELETE FROM Joueur WHERE idJoueur='";
    private static final String DEBUT_REQUETE_SELECTION_ID_JOUEUR = "SELECT idJoueur FROM Joueur WHERE nom='";
    private static final String DEBUT_REQUETE_SELECTION_JOUEUR = "SELECT * FROM Joueur WHERE Joueur.idJoueur=";

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
        bdd.execSQL("PRAGMA foreign_keys = ON;");
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
        String requete = DEBUT_REQUETE_INSERTION_JOUEUR + joueur.getNom() + "','" + joueur.getPrenom() + "')";
        Log.d(TAG,"insererJoueur() : Exécution de la requete : " + requete);
        bdd.execSQL(requete);
    }
    /**
     * @brief Permet d'effectuer une requete pour supprimer un joueur
     * @param joueur Le joueur à supprimer
     */
    public void supprimerJoueur(Joueur joueur)
    {
        ouvrir();
        String requete = DEBUT_REQUETE_SUPPRESSION_JOUEUR + chercherIDJoueur(joueur) + "';";
        Log.d(TAG,"supprimerJoueur() : Exécution de la requete : " + requete);
        bdd.execSQL(requete);
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
     * @param rencontre
     */
    public void enregistrerRencontre(Rencontre rencontre)
    {
        ouvrir();
        String requete = DEBUT_REQUETE_INSERTION_RENCONTRE + chercherIDJoueur(rencontre.getJoueurs().get(0)) + "," + chercherIDJoueur(rencontre.getJoueurs().get(1)) + "," + rencontre.getNbManchesGagnantes() + "," + FIN_REQUETE_INSERTION_RENCONTRE;
        Log.d(TAG,"enregistrerRencontre() : Exécution de la requete : " + requete);
        bdd.execSQL(requete);
    }

    /**
     * @brief Méthode appelée pour supprimer une seule rencontre
     * @param rencontre
     */
    public void supprimerRencontre(Rencontre rencontre)
    {
        ouvrir();
        String requete = DEBUT_REQUETE_SUPPRESSION_RENCONTRE + rencontre.getIdRencontre() + ";";
        Log.d(TAG,"supprimerRencontre() : Exécution de la requete : " + requete);
        bdd.execSQL(requete);
    }

    /**
     * @brief Méthode pour vider l'historique des rencontres
     */
    public void purgerRencontres()
    {
        ouvrir();
        String requete = REQUETE_PURGE_RENCONTRES;
        Log.d(TAG,"enregistrerRencontre() : Exécution de la requete : " + requete);
        bdd.execSQL(requete);
    }

    /**
     * @brief Méthode pour enregistrer une manche une fois terminée
     * @param manche
     */
    public void enregistrerManche(Manche manche)
    {
        ouvrir();
        DecimalFormat format = new DecimalFormat("00.0");
        String requete = DEBUT_REQUETE_INSERTION_MANCHE + chercherIDRencontre() + "," +
            manche.getPointsJoueur1() + "," +
            manche.getPointsJoueur2() + "," +
            format.format(manche.getPrecisionJoueur1()).replace(',','.') + "," +
            format.format(manche.getPrecisionJoueur2()).replace(',','.') + "," +
            new java.sql.Date(manche.getDebut().getTime()) + "," +
            new java.sql.Date(manche.getFin().getTime()) + ");";
        Log.d(TAG, "enregistrerManche() : requete = " + requete);
        Log.d(TAG, "enregistrerManche() : Précision joueur 1 = " + manche.getPrecisionJoueur1());
        Log.d(TAG, "enregistrerManche() : Précision joueur 2 = " + manche.getPrecisionJoueur2());
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
        String requete = DEBUT_REQUETE_SELECTION_ID_JOUEUR + joueur.getNom() + "' AND prenom='" + joueur.getPrenom() + "';";
        Cursor curseurResultat = bdd.rawQuery(requete,null);
        curseurResultat.moveToNext();
        return curseurResultat.getInt(INDEX_ID_JOUEUR);
    }

    /**
     * @brief Permet d'effectuer une requete de type SELECT pour récupérer toutes les rencontres
     * @return rencontres
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Vector<Rencontre> getRencontres()
    {
        Log.d(TAG, "getRencontres()");
        Vector<Rencontre> rencontres = new Vector<Rencontre>();
        String requeteRencontres = REQUETE_RENCONTRES;
        Cursor curseurRencontres = effectuerRequete(requeteRencontres);

        for (int i = 0; i < curseurRencontres.getCount(); i++)
        {
            curseurRencontres.moveToNext();
            Vector<Joueur> joueurs = new Vector<Joueur>();
            String requeteJoueurs = DEBUT_REQUETE_SELECTION_JOUEUR + curseurRencontres.getString(INDEX_ID_JOUEUR_1) + " OR Joueur.idJoueur=" + curseurRencontres.getString(INDEX_ID_JOUEUR_2) + ";";
            Cursor curseurJoueurs = effectuerRequete(requeteJoueurs);

            for(int j = 0; j < curseurJoueurs.getCount(); j++)
            {
                curseurJoueurs.moveToNext();
                joueurs.add(new Joueur(curseurJoueurs.getString(INDEX_NOM_JOUEUR), curseurJoueurs.getString(INDEX_PRENOM_JOUEUR)));
                Log.d(TAG, "nom = " + curseurJoueurs.getString(INDEX_NOM_JOUEUR) + " - " + "prenom = " + curseurJoueurs.getString(INDEX_PRENOM_JOUEUR));
            }
            Vector<Manche> manches = getManches(curseurRencontres.getInt(INDEX_ID_RENCONTRE));
            rencontres.add(new Rencontre(curseurRencontres.getInt(INDEX_ID_RENCONTRE), joueurs, manches, curseurRencontres.getInt(INDEX_NB_MANCHES_GAGNANTES)));
        }
        return rencontres;
    }

    /**
     * @brief Permet d'effectuer une requete de type SELECT pour récupérer toutes les manches associées à une rencontre
     * @param idRencontre
     * @return les manches associées à une rencontre
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Vector<Manche> getManches(int idRencontre)
    {
        Log.d(TAG, "getManches()");
        Vector<Manche> manches = new Vector<Manche>();
        String requeteManches = REQUETE_MANCHES + idRencontre + ";";
        Cursor curseurManches = effectuerRequete(requeteManches);

        for(int i = 0; i < curseurManches.getCount(); ++i)
        {
            curseurManches.moveToNext();
            manches.add(new Manche(
                curseurManches.getInt(INDEX_POINTS_JOUEUR_1),
                curseurManches.getInt(INDEX_POINTS_JOUEUR_2),
                curseurManches.getDouble(INDEX_PRECISION_JOUEUR_1),
                curseurManches.getDouble(INDEX_PRECISION_JOUEUR_2),
                new Date(curseurManches.getLong(INDEX_FINI)*1000),
                new Date(curseurManches.getLong(INDEX_HORODATAGE)*1000))
            );
            Log.d(TAG, "Joueur 1 : " + manches.get(i).getPointsJoueur1() + " Joueur 2 : " + manches.get(i).getPointsJoueur2() + " Début : " + manches.get(i).getDebut() + " Fin : " + manches.get(i).getFin());
        }
        return manches;
    }
}
