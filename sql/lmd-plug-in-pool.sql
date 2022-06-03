--- LMD (langage de manipulation de données)

--- Contenu des tables (tests)

--- Table Joueur

INSERT INTO Joueur(nom, prenom) VALUES ('FAURIE','Christophe');
INSERT INTO Joueur(nom, prenom) VALUES ('COLDRICK','Paul');
INSERT INTO Joueur(nom, prenom) VALUES ('LELONG','Kevin');
INSERT INTO Joueur(nom, prenom) VALUES ('LAMBERT','Christophe');
INSERT INTO Joueur(nom, prenom) VALUES ('JANNIN','Jean-Baptiste');
INSERT INTO Joueur(nom, prenom) VALUES ('RAMIER','Sébastien');
INSERT INTO Joueur(nom, prenom) VALUES ('MERAS','Pierre');
INSERT INTO Joueur(nom, prenom) VALUES ('PHILIPPE','Christopher');

--- Table Match

INSERT INTO Rencontre(idRencontre, idJoueur1, idJoueur2, nbManchesGagnantes, fini, horodatage) VALUES (NULL,7,8,2,0,'2022-01-29 08:00:00');

--- Table Manche

INSERT INTO Manche(idManche, idRencontre, pointsJoueur1, pointsJoueur2, precisionJoueur1, precisionJoueur2, debut) VALUES (NULL,1,8,4,57.32,45.25,'2022-01-29 08:15:00');
INSERT INTO Manche(idManche, idRencontre, pointsJoueur1, pointsJoueur2, precisionJoueur1, precisionJoueur2, debut) VALUES (NULL,1,8,2,82.54,78.15,'2022-01-29 08:25:00');
INSERT INTO Manche(idManche, idRencontre, pointsJoueur1, pointsJoueur2, precisionJoueur1, precisionJoueur2, debut) VALUES (NULL,1,4,8,52.47,12.25,'2022-01-29 08:55:00');
