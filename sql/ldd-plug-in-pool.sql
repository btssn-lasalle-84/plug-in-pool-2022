--- LDD (langage de définition de données)

--- Supprime les tables

DROP TABLE IF EXISTS Manche;
DROP TABLE IF EXISTS Match;
DROP TABLE IF EXISTS Joueur;

--- Table Joueur

CREATE TABLE IF NOT EXISTS Joueur(idJoueur INTEGER PRIMARY KEY AUTOINCREMENT, nom VARCHAR, prenom VARCHAR, UNIQUE(nom,prenom));

--- Table Match

CREATE TABLE IF NOT EXISTS Match(idMatch INTEGER PRIMARY KEY AUTOINCREMENT, idJoueur1 INTEGER NOT NULL, idJoueur2 INTEGER NOT NULL, nbManchesGagnantes INTEGER DEFAULT 0, fini INTEGER DEFAULT 0, horodatage DATETIME NOT NULL, CONSTRAINT fk_idJoueur_A FOREIGN KEY (idJoueur1) REFERENCES Joueur(idJoueur), CONSTRAINT fk_idJoueur_B FOREIGN KEY (idJoueur2) REFERENCES Joueur(idJoueur));

--- Table Manche

CREATE TABLE IF NOT EXISTS Manche(idManche INTEGER PRIMARY KEY AUTOINCREMENT, idMatch INTEGER NOT NULL, pointsJoueur1 INTEGER NOT NULL, pointsJoueur2 INTEGER NOT NULL, debut DATETIME NOT NULL, fin DATETIME, CONSTRAINT fk_idMatch_1 FOREIGN KEY (idMatch) REFERENCES Match(idMatch));

--- Voir aussi :
--- ON DELETE CASCADE
