# Simulateur PLUG-IN-POOL 2022

## Présentation du protocole implanté dans le simulateur ESP'ACE

Ce document présente rapidement le fonctionnement du simulateur ainsi que le protocole implémenté. Le protocole complet est disponible dans Google Drive. Actuellement, la version du protocole est la **0.1** (10 avril 2022).

## Configuration du simulateur

```cpp
#define NB_POCHES       6
#define NB_BILLES_ROUGE 7
#define NB_BILLES_JAUNE 7
```

Un tir prend entre 1 et 4 secondes. Le joueur a 1 chance sur 2 d'empocher une bille sinon il loupe ou il fait une faute (empochage d'une mauvaise bille).

## Fonctionnement

Pour l'instant, le simulateur démarre à la réception d'une trame : `$START\r\n`

Quand le simulateur affiche "loupé" ou "faute !", il faut appuyer sur le bouton SUIVANT (SW2).

La partie peut être arrêtée avec une trame `$STOP\r\n` ou annulée avec une trame `$RESET\r\n`.

## Écran OLED

Le nom du périphérique bluetooth et son adresse MAC sont affichés sur les deux premières lignes.

Les lignes suivantes affichent successivement :

- le score du joueur en train de tirer
- le tir du joueur qui a les billes rouges
- le tir du joueur qui a les billes jaunes
- la trame reçue et l'état de la partie

_Remarque :_ `>` indique le joueur en train de tirer.

## platform.ini

```ini
[env:lolin32]
platform = espressif32
board = lolin32
framework = arduino
lib_deps =
  thingpulse/ESP8266 and ESP32 OLED driver for SSD1306 displays @ ^4.2.0
upload_port = /dev/ttyUSB0
upload_speed = 115200
monitor_port = /dev/ttyUSB0
monitor_speed = 115200
```

## Auteur

- Thierry Vaira <<tvaira@free.fr>>
