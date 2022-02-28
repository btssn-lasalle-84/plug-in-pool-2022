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

INSERT INTO Match(idMatch, idJoueur1, idJoueur2, nbManchesGagnantes, fini, horodatage) VALUES (NULL,7,8,2,0,'2022-01-29 08:00:00');

--- Table Manche

INSERT INTO Manche(idManche, idMatch, pointsJoueur1, pointsJoueur2, debut) VALUES (NULL,1,1,0,'2022-01-29 08:15:00');
INSERT INTO Manche(idManche, idMatch, pointsJoueur1, pointsJoueur2, debut) VALUES (NULL,1,0,1,'2022-01-29 08:25:00');
INSERT INTO Manche(idManche, idMatch, pointsJoueur1, pointsJoueur2, debut) VALUES (NULL,1,1,0,'2022-01-29 08:55:00');
