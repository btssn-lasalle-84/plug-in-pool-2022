package com.lasalle.pluginpool;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @file SQLite.java
 * @brief Déclaration de la classe SQLite
 * @author Thierry VAIRA
 */

/**
 * @class SQLite
 * @brief Classe qui permet la création de la base de données
 */
public class SQLite extends SQLiteOpenHelper
{
    private static final String TAG = "_SQLite_";  //!< TAG pour les logs
    public static final String  DATABASE_NAME = "plug-in-pool.db";//!< Le nom de la base de données
    public static final int     DATABASE_VERSION = 3;//!< La version de la base de données

    /**
     * Requêtes de création des tables
     */
    private static final String CREATE_TABLE_JOUEUR = "CREATE TABLE IF NOT EXISTS Joueur(idJoueur INTEGER PRIMARY KEY AUTOINCREMENT, nom VARCHAR, prenom VARCHAR, UNIQUE(nom,prenom));";
    private static final String CREATE_TABLE_RENCONTRE = "CREATE TABLE IF NOT EXISTS Rencontre(idRencontre INTEGER PRIMARY KEY AUTOINCREMENT, idJoueur1 INTEGER NOT NULL, idJoueur2 INTEGER NOT NULL, nbManchesGagnantes INTEGER DEFAULT 0, fini INTEGER DEFAULT 0, horodatage DATETIME NOT NULL, CONSTRAINT fk_idJoueur_A FOREIGN KEY (idJoueur1) REFERENCES Joueur(idJoueur) ON DELETE CASCADE, CONSTRAINT fk_idJoueur_B FOREIGN KEY (idJoueur2) REFERENCES Joueur(idJoueur) ON DELETE CASCADE);";
    private static final String CREATE_TABLE_MANCHE = "CREATE TABLE IF NOT EXISTS Manche(idManche INTEGER PRIMARY KEY AUTOINCREMENT, idRencontre INTEGER NOT NULL, pointsJoueur1 INTEGER NOT NULL, pointsJoueur2 INTEGER NOT NULL, precisionJoueur1 REAL NOT NULL, precisionJoueur2 REAL NOT NULL, debut DATETIME NOT NULL, fin DATETIME, CONSTRAINT fk_idRencontre_1 FOREIGN KEY (idRencontre) REFERENCES Rencontre(idRencontre) ON DELETE CASCADE);";

    /**
     * Requêtes d'insertion de initiales dans la base de données
     */

    /**
     * Requêtes de test
     */
    private static final String INSERT_TABLE_JOUEUR_1 = "INSERT INTO Joueur(nom, prenom) VALUES ('FAURIE','Christophe');";
    private static final String INSERT_TABLE_JOUEUR_2 = "INSERT INTO Joueur(nom, prenom) VALUES ('COLDRICK','Paul');";
    private static final String INSERT_TABLE_JOUEUR_3 = "INSERT INTO Joueur(nom, prenom) VALUES ('LELONG','Kevin');";
    private static final String INSERT_TABLE_JOUEUR_4 = "INSERT INTO Joueur(nom, prenom) VALUES ('LAMBERT','Christophe');";
    private static final String INSERT_TABLE_JOUEUR_5 = "INSERT INTO Joueur(nom, prenom) VALUES ('JANNIN','Jean-Baptiste');";
    private static final String INSERT_TABLE_JOUEUR_6 = "INSERT INTO Joueur(nom, prenom) VALUES ('RAMIER','Sébastien');";
    private static final String INSERT_TABLE_JOUEUR_7 = "INSERT INTO Joueur(nom, prenom) VALUES ('MERAS','Pierre');";
    private static final String INSERT_TABLE_JOUEUR_8 = "INSERT INTO Joueur(nom, prenom) VALUES ('PHILIPPE','Christopher');";
    private static final String INSERT_TABLE_RENCONTRE_1 = "INSERT INTO Rencontre(idRencontre, idJoueur1, idJoueur2, nbManchesGagnantes, fini, horodatage) VALUES (NULL,7,8,2,0,'2022-01-29 08:00:00');";
    private static final String INSERT_TABLE_MANCHE_1 = "INSERT INTO Manche(idManche, idRencontre, pointsJoueur1, pointsJoueur2, precisionJoueur1, precisionJoueur2, debut) VALUES (NULL,1,8,4,57.32,45.25,'2022-01-29 08:15:00');";
    private static final String INSERT_TABLE_MANCHE_2 = "INSERT INTO Manche(idManche, idRencontre, pointsJoueur1, pointsJoueur2, precisionJoueur1, precisionJoueur2, debut) VALUES (NULL,1,8,2,82.54,78.15,'2022-01-29 08:25:00');";
    private static final String INSERT_TABLE_MANCHE_3 = "INSERT INTO Manche(idManche, idRencontre, pointsJoueur1, pointsJoueur2, precisionJoueur1, precisionJoueur2, debut) VALUES (NULL,1,4,8,52.47,12.25,'2022-01-29 08:55:00');";

    /**
     * @brief Constructeur de la classe SQLite
     */
    public SQLite(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * @brief Méthode appelée à la création de la base de données
     * @param db La base de données créée
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Log.d(TAG, "onCreate() path = " + db.getPath());
        creerTables(db);
        insererDonneesInitiales(db);
    }

    /**
     * @brief Méthode appelée lors de la mise à jour de la base de données
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.d(TAG, "onUpgrade()");
        onCreate(db);
    }

    /**
     * @brief Méthode permettant d'éxecuter les requêtes de création des tables
     * @param db La base de données sur laquelle éxecuter les requêtes
     */
    private void creerTables(SQLiteDatabase db)
    {
        Log.d(TAG, "creerTables()");
        db.execSQL(CREATE_TABLE_JOUEUR);
        db.execSQL(CREATE_TABLE_RENCONTRE);
        db.execSQL(CREATE_TABLE_MANCHE);
    }

    /**
     * @brief Méthode permettant d'éxecuter les requêtes d'insertion de données initiales
     * @param db La base de données sur laquelle éxecuter les requêtes
     */
    private void insererDonneesInitiales(SQLiteDatabase db)
    {
        Log.d(TAG, "insererDonneesInitiales()");
        // Tests
        db.execSQL(INSERT_TABLE_JOUEUR_1);
        db.execSQL(INSERT_TABLE_JOUEUR_2);
        db.execSQL(INSERT_TABLE_JOUEUR_3);
        db.execSQL(INSERT_TABLE_JOUEUR_4);
        db.execSQL(INSERT_TABLE_JOUEUR_5);
        db.execSQL(INSERT_TABLE_JOUEUR_6);
        db.execSQL(INSERT_TABLE_JOUEUR_7);
        db.execSQL(INSERT_TABLE_JOUEUR_8);
        db.execSQL(INSERT_TABLE_RENCONTRE_1);
        db.execSQL(INSERT_TABLE_MANCHE_1);
        db.execSQL(INSERT_TABLE_MANCHE_2);
        db.execSQL(INSERT_TABLE_MANCHE_3);
    }
}
