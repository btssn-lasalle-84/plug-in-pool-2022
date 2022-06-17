--- LDD (langage de définition de données)

--- Supprime les tables

DROP TABLE IF EXISTS Manche;
DROP TABLE IF EXISTS Rencontre;
DROP TABLE IF EXISTS Joueur;

--- Table Joueur

CREATE TABLE IF NOT EXISTS Joueur(idJoueur INT(10) NOT NULL AUTO_INCREMENT, nom VARCHAR, prenom VARCHAR, UNIQUE(nom,prenom), PRIMARY KEY (idJoueur));

--- Table Rencontre

CREATE TABLE IF NOT EXISTS Rencontre(idRencontre INTEGER NOT NULL AUTO_INCREMENT, idJoueur1 INTEGER NOT NULL, idJoueur2 INTEGER NOT NULL, nbManchesGagnantes INTEGER DEFAULT 0, fini INTEGER DEFAULT 0, horodatage DATETIME NOT NULL, CONSTRAINT fk_idJoueur_A FOREIGN KEY (idJoueur1) REFERENCES Joueur(idJoueur), CONSTRAINT fk_idJoueur_B FOREIGN KEY (idJoueur2) REFERENCES Joueur(idJoueur), PRIMARY KEY (idRencontre));

--- Table Manche

CREATE TABLE IF NOT EXISTS Manche(idManche INTEGER NOT NULL AUTO_INCREMENT, idRencontre INTEGER NOT NULL, pointsJoueur1 INTEGER NOT NULL, pointsJoueur2 INTEGER NOT NULL, precisionJoueur1 REAL NOT NULL, precisionJoueur2 REAL NOT NULL, debut DATETIME NOT NULL, fin DATETIME, CONSTRAINT fk_idRencontre_1 FOREIGN KEY (idRencontre) REFERENCES Rencontre(idRencontre), PRIMARY KEY (idManche));

--- Voir aussi :
--- ON DELETE CASCADE
