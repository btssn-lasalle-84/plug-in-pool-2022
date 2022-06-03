--- Exemples de requêtes SQL

--- Liste des joueurs

SELECT * FROM Joueur;

SELECT COUNT(*) AS NbJoueurs FROM Joueur;

SELECT nom || ' ' || prenom AS NomJoueur FROM Joueur ORDER BY NomJoueur;

--- Liste des Rencontres

SELECT * FROM Rencontre;

SELECT * FROM Rencontre 
INNER JOIN Joueur a ON (a.idJoueur = Rencontre.idJoueur1) 
INNER JOIN Joueur b ON (b.idJoueur = Rencontre.idJoueur2);


--- Terminer une Rencontre

UPDATE Rencontre SET fini=1 WHERE idRencontre='1';

--- Liste des Manches

SELECT * FROM Manche;

--- Liste de toutes les manches pour un Rencontre

SELECT * FROM Manche 
INNER JOIN Rencontre ON Manche.idRencontre = Rencontre.idRencontre 
INNER JOIN Joueur a ON (a.idJoueur = Rencontre.idJoueur1) 
INNER JOIN Joueur b ON (b.idJoueur = Rencontre.idJoueur2);

---- Commencer une manche

INSERT INTO Manche(idRencontre, pointsJoueur1, pointsJoueur2, debut) VALUES (1,1,0,DATETIME('now'));

--- Terminer une manche

UPDATE Manche SET fin=DATETIME('now'), pointsJoueur1='1', pointsJoueur2='0' WHERE idManche='1';
