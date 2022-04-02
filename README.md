# Le projet plug-in-pool-2022

Système numérique permettant de jouer au billard BlackBall

- Automatisation du déroulement d’une partie
- Affichage du nombre de billes empochées et/ou restantes
- Enregistrement des données d’une partie finie

Le système Plug-in-Pool est décomposé en trois modules :

- Module de gestion de partie (**Mobile-POOL** IR)​ : les joueurs paramètrent et lancent la partie à partir d’une application sur un terminal mobile (sous Android) ;
- Module de détection des billes (Détection-POOL EC)​: le billard est équipé de capteurs permettant de détecter l’empochage et la couleur d’une bille.
- Module de visualisation de partie (**Écran-POOL** IR) ​ : le déroulement de la partie est affiché sur un écran de télévision.

## Fonctionnalités Mobile-POOL

Application mobile qui gère le déroulement des rencontres

- Itération 2 :
    - [ ] Enregistrer un nouveau joueur
  - [ ] Sélectionner un joueur existant
  - [ ] Paramétrer la partie : type et nombre de manches gagnantes
- Itération 3 :
  - [ ] Initialiser une communication
  - [ ] Recevoir une trame
  - [ ] Envoyer une trame
  - [ ] Traiter une trame
  - [ ] Connecter une table de billard
  - [ ] Connecter l’écran

## Fonctionnalités Écran-POOL

Ce module correspond à la partie “affichage” du système. Il a pour objectifs de réaliser la ​récupération d’informations​ envoyées par le terminal mobile et l’affichage de la rencontre actuelle. Il communique en Bluetooth uniquement avec le terminal mobile Android.

Sur l'écran, les joueurs pourront visualiser en continu :

- le nom des joueurs (si existant), la durée écoulée de la partie ;
- les billes empochées et restantes,
- le nombre de manches gagnées par chaque joueur
- des statistiques

L’application Qt s’exécutera en mode “Kiosque” sur le Raspberry Pi.

## Historique des versions

- Version 0.1 : 02/04/2022

## Auteurs

- Version Mobile Android : Pierre Meras <<pierremeras@hotmail.fr>>
- Version Desktop Qt : Christopher PHILIPPE <<christopher.philippe84@gmail.com>>

## Kanban

[plug-in-pool-2022](https://github.com/btssn-lasalle-84/plug-in-pool-2022/projects/1)
