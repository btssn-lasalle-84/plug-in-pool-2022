--- Exemples de requêtes SQL


--- Liste des joueurs

SELECT * FROM Joueur;

--- Nombre de joueurs

SELECT COUNT(*) FROM Joueur;

--- Liste des noms de joueur

SELECT nom || ' ' || prenom AS NomJoueur FROM Joueur ORDER BY NomJoueur;

--- Liste des rencontres

SELECT * FROM Rencontre;

--- Terminer une manche

UPDATE Manche SET fin=DATETIME('now') WHERE idManche='1';

--- Liste des manches

SELECT * FROM Manche;

--- Liste de toutes manches pour une rencontre

SELECT * FROM Manche WHERE idRencontre='2';

--- Liste de toutes manches pour une rencontre avec les noms des joueurs

SELECT * FROM Manche 
INNER JOIN Rencontre ON Manche.idRencontre=Rencontre.idRencontre
INNER JOIN Joueur a ON (a.idJoueur = Rencontre.idJoueur1) 
INNER JOIN Joueur b ON (b.idJoueur = Rencontre.idJoueur2);

--- Score d'une manche d'une rencontre

SELECT pointsJoueur1, pointJoueur2 FROM Manche
INNER JOIN Rencontre ON Manche.idRencontre=Rencontre.idRencontre;

--- Enregistrer une rencontre

INSERT INTO Rencontre(idJoueur1, idJoueur2, nbManchesGagnantes, fini, horodatage) VALUES (7,8,2,0,DATETIME('now'));

--- Terminer une rencontre

UPDATE Rencontre SET fini=1 WHERE idRencontre='1';
