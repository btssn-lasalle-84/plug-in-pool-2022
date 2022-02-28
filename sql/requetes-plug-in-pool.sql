--- Exemples de requêtes SQL

--- Liste des clubs


--- Nombre de clubs


--- Liste des joueurs


--- Nombre de joueurs


--- Liste des noms de joueur

SELECT nom || ' ' || prenom AS NomJoueur FROM Joueur ORDER BY NomJoueur;

--- Liste des joueurs d'un club

SELECT * FROM Joueur 
INNER JOIN Club ON Joueur.idClub=Club.idClub;

SELECT Joueur.numeroLicence, Joueur.nom, Joueur.prenom, Club.nomClub FROM Joueur 
INNER JOIN Club ON Joueur.idClub=Club.idClub 
ORDER BY Club.nomClub ASC;

--- Liste des joueurs pour un club



--- Liste des joueurs triés par nom pour un club



--- Liste des rencontres


--- Liste des rencontres avec les informations de club



--- Terminer une rencontre



--- Liste des parties



--- Liste de toutes parties pour une rencontre



--- Liste de toutes parties SIMPLE pour une rencontre



--- Liste de toutes parties DOUBLE pour une rencontre



--- Liste de toutes parties SIMPLE pour une rencontre avec les noms des joueurs



--- Score d'une partie d'une rencontre



---- Commencer un set



---- Finir un set et enregistrer le score



--- Terminer une partie


