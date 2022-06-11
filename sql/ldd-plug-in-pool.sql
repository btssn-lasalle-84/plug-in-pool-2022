--- LDD (langage de définition de données)

--- Supprime les tables

DROP TABLE IF EXISTS Manche;
DROP TABLE IF EXISTS Rencontre;
DROP TABLE IF EXISTS Joueur;

--- Table Joueur

CREATE TABLE IF NOT EXISTS Joueur(idJoueur INTEGER PRIMARY KEY AUTOINCREMENT, nom VARCHAR, prenom VARCHAR, UNIQUE(nom,prenom));

--- Table Rencontre

CREATE TABLE IF NOT EXISTS Rencontre(idRencontre INTEGER PRIMARY KEY AUTOINCREMENT, idJoueur1 INTEGER NOT NULL, idJoueur2 INTEGER NOT NULL, nbManchesGagnantes INTEGER DEFAULT 0, fini INTEGER DEFAULT 0, horodatage DATETIME NOT NULL, CONSTRAINT fk_idJoueur_A FOREIGN KEY (idJoueur1) REFERENCES Joueur(idJoueur) ON DELETE CASCADE, CONSTRAINT fk_idJoueur_B FOREIGN KEY (idJoueur2) REFERENCES Joueur(idJoueur) ON DELETE CASCADE);

--- Table Manche

CREATE TABLE IF NOT EXISTS Manche(idManche INTEGER PRIMARY KEY AUTOINCREMENT, idRencontre INTEGER NOT NULL, pointsJoueur1 INTEGER NOT NULL, pointsJoueur2 INTEGER NOT NULL, precisionJoueur1 REAL NOT NULL, precisionJoueur2 REAL NOT NULL, debut DATETIME NOT NULL, fin DATETIME, CONSTRAINT fk_idRencontre_1 FOREIGN KEY (idRencontre) REFERENCES Rencontre(idRencontre) ON DELETE CASCADE);

--- Voir aussi :
--- ON DELETE CASCADE
