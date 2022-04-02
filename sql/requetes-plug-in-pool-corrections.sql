--- Exemples de requêtes SQL

--- Liste des joueurs

SELECT * FROM Joueur;

SELECT COUNT(*) AS NbJoueurs FROM Joueur;

SELECT nom || ' ' || prenom AS NomJoueur FROM Joueur ORDER BY NomJoueur;

--- Liste des Matchs

SELECT * FROM Match;

SELECT * FROM Match 
INNER JOIN Joueur a ON (a.idJoueur = Match.idJoueur1) 
INNER JOIN Joueur b ON (b.idJoueur = Match.idJoueur2);


--- Terminer une Match

UPDATE Match SET fini=1 WHERE idMatch='1';

--- Liste des Manches

SELECT * FROM Manche;

--- Liste de toutes les manches pour un match

SELECT * FROM Manche 
INNER JOIN Match ON Manche.idMatch = Match.idMatch 
INNER JOIN Joueur a ON (a.idJoueur = Match.idJoueur1) 
INNER JOIN Joueur b ON (b.idJoueur = Match.idJoueur2);

---- Commencer une manche

INSERT INTO Manche(idMatch, pointsJoueur1, pointsJoueur2, debut) VALUES (1,1,0,DATETIME('now'));

--- Terminer une manche

UPDATE Manche SET fin=DATETIME('now'), pointsJoueur1='1', pointsJoueur2='0' WHERE idManche='1';
