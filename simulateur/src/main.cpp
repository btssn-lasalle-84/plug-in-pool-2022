/**
* @file src/main.cpp
* @brief Programme principal PLUG-IN-POOL 2022
* @author Thierry Vaira
* @version 0.1
*/
#include <Arduino.h>
#include <BluetoothSerial.h>
#include <afficheur.h>
#include "esp_bt_main.h"
#include "esp_bt_device.h"

#define DEBUG
//#define DEBUG_VERIFICATION

// Brochages
#define GPIO_LED_ROUGE      5       //!< En attente
#define GPIO_LED_ORANGE     17      //!< Trame OK
#define GPIO_LED_VERTE      16      //!< Trame START
#define GPIO_SW1            12      //!< Pour simuler un tir (non utilisé)
#define GPIO_SW2            14      //!< Pour simuler joueur suivant
#define ADRESSE_I2C_OLED    0x3c    //!< Adresse I2C de l'OLED
#define BROCHE_I2C_SDA      21      //!< Broche SDA
#define BROCHE_I2C_SCL      22      //!< Broche SCL

// Protocole (cf. Google Drive)
#define EN_TETE_TRAME           "$PLUG"
#define DELIMITEUR_CHAMP        ";"
#define DELIMITEURS_FIN         "\r\n"
#define DELIMITEUR_DATAS        ';'
#define DELIMITEUR_FIN          '\n'

#define ERREUR_TRAME_INCONNUE       0
#define ERREUR_TRAME_NON_SUPPORTEE  1
#define ERREUR_TRAME_ETAT           2

#define NB_POCHES       6

#define NB_BILLES       7
#define NB_BILLES_ROUGE 7
#define NB_BILLES_JAUNE 7

#define TIR_LOUPE       9 // simule un tir loupé

#define BLUETOOTH
#ifdef BLUETOOTH
#define BLUETOOTH_SLAVE //!< esclave
//#define BLUETOOTH_MASTER //!< maître
BluetoothSerial ESPBluetooth;
#endif

/**
 * @enum TypeTrame
 * @brief Les differents types de trame
 */
enum TypeTrame
{
  Inconnu = -1,
  //START, PAUSE, PLAY, STOP, RESET, CONFIG, EMPOCHE, FAUTE, NEXT, ACK, ERREUR,
  START, STOP, RESET, CONFIG, EMPOCHE, FAUTE, NEXT, ACK, ERREUR,
  NB_TRAMES
};

/**
 * @enum EtatPartie
 * @brief Les differents états d'une partie
 */
enum EtatPartie
{
  Finie = 0,
  EnCours,
  EnPause,
  Terminee
};

/**
 * @enum CouleurBille
 * @brief Les deux couleurs des billes
 */
enum CouleurBille
{
  BLANCHE = -2,
  NOIRE = -1,
  ROUGE = 0,
  JAUNE = 1,
  NbCouleurs
};

//const String nomsTrame[TypeTrame::NB_TRAMES] = { "START", "PAUSE", "PLAY", "STOP", "RESET", "CONFIG", "EMPOCHE", "FAUTE", "NEXT", "ACK", "ERREUR" }; //!< nom des trames dans le protocole
const String nomsTrame[TypeTrame::NB_TRAMES] = { "START", "STOP", "RESET", "CONFIG", "EMPOCHE", "FAUTE", "NEXT", "ACK", "ERREUR" }; //!< nom des trames dans le protocole
EtatPartie etatPartie = Finie; //!< l'état de la partie
int nbBilles[CouleurBille::NbCouleurs] = { NB_BILLES_ROUGE, NB_BILLES_JAUNE }; //!< le nombre de billes à empocher pour chaque couleur
CouleurBille joueurCourant = CouleurBille::NOIRE; //!< la couleur du joueur qui tire
bool etatJoueur[CouleurBille::NbCouleurs] = { false, false }; //!< le joueur a joué ou pas
bool joueurGagnant[CouleurBille::NbCouleurs] = { false, false }; //!< le joueur a gagné ou pas
const String codeCouleur[CouleurBille::NbCouleurs] = { "R", "J" }; //!< code de couleur de la bille empochée
const String codePoche[NB_POCHES] = { "A", "B", "C", "D", "E", "F" }; //!< code des poches
//String typePartie;
bool refresh = false; //!< demande rafraichissement de l'écran OLED
bool antiRebond = false; //!< anti-rebond
bool tirEncours = false; //!< une sequence de tirs est en cours
bool joueurSuivant = false; //!< au joueur suivant ou pas
Afficheur afficheur(ADRESSE_I2C_OLED, BROCHE_I2C_SDA, BROCHE_I2C_SCL); //!< afficheur OLED SSD1306
String entete = String(EN_TETE_TRAME); // caractère séparateur
String separateur = String(DELIMITEUR_CHAMP); // caractère séparateur
String delimiteurFin = String(DELIMITEURS_FIN); // fin de trame

String extraireChamp(String &trame, unsigned int numeroChamp)
{
  String champ;
  unsigned int compteurCaractere = 0;
  unsigned int compteurDelimiteur = 0;
  char fin = '\n';

  if(delimiteurFin.length() > 0)
    fin = delimiteurFin[0];

  for(unsigned int i = 0; i < trame.length(); i++)
  {
    if(trame[i] == separateur[0] || trame[i] == fin)
    {
      compteurDelimiteur++;
    }
    else if(compteurDelimiteur == numeroChamp)
    {
        champ += trame[i];
        compteurCaractere++;
    }
  }

  return champ;
}

/**
 * @brief Envoie une trame d'acquittement via le Bluetooth
 *
 */
void envoyerTrameAcquittement()
{
  char trameEnvoi[64];

  //Format : $PLUG;ACK;\r\n
  sprintf((char *)trameEnvoi, "%s;%s;\r\n", entete.c_str(), nomsTrame[TypeTrame::ACK].c_str());

  ESPBluetooth.write((uint8_t*)trameEnvoi, strlen((char *)trameEnvoi));
  #ifdef DEBUG
  String trame = String(trameEnvoi);
  trame.remove(trame.indexOf("\r"), 2);
  Serial.print("> ");
  Serial.println(trame);
  #endif
}

/**
 * @brief Envoie une trame d'erreur via le Bluetooth
 *
 */
void envoyerTrameErreur(int code)
{
  char trameEnvoi[64];

  //Format : $PLUG;ERREUR;{CODE};\r\n
  sprintf((char *)trameEnvoi, "%s;%s;%d;\r\n", entete.c_str(), nomsTrame[TypeTrame::ERREUR].c_str(), code);

  ESPBluetooth.write((uint8_t*)trameEnvoi, strlen((char *)trameEnvoi));
  #ifdef DEBUG
  String trame = String(trameEnvoi);
  trame.remove(trame.indexOf("\r"), 2);
  Serial.print("> ");
  Serial.println(trame);
  #endif
}

/**
 * @brief Envoie une trame via le Bluetooth
 *
 */
void envoyerTrameEmpoche(int numeroPoche, CouleurBille couleurBille)
{
  char trameEnvoi[64];

  //Format : $PLUG;EMPOCHE;{COULEUR};{NUMERO};\r\n
  sprintf((char *)trameEnvoi, "%s;%s;%s;%s;\r\n", entete.c_str(), nomsTrame[TypeTrame::EMPOCHE].c_str(), codeCouleur[(int)couleurBille].c_str(), codePoche[numeroPoche].c_str());

  ESPBluetooth.write((uint8_t*)trameEnvoi, strlen((char *)trameEnvoi));
  #ifdef DEBUG
  String trame = String(trameEnvoi);
  trame.remove(trame.indexOf("\r"), 2);
  Serial.print("> ");
  Serial.println(trame);
  #endif
}

/**
 * @brief Envoie une trame NEXT via le Bluetooth
 *
 */
void envoyerTrameNext()
{
  char trameEnvoi[64];

  //Format : $PLUG;NEXT;\r\n
  sprintf((char *)trameEnvoi, "%s;%s;\r\n", entete.c_str(), nomsTrame[TypeTrame::NEXT].c_str());

  ESPBluetooth.write((uint8_t*)trameEnvoi, strlen((char *)trameEnvoi));
  #ifdef DEBUG
  String trame = String(trameEnvoi);
  trame.remove(trame.indexOf("\r"), 2);
  Serial.print("> ");
  Serial.println(trame);
  #endif
}

/**
 * @brief Envoie une trame FAUTE via le Bluetooth
 *
 */
void envoyerTrameFaute(int numeroPoche, CouleurBille couleurBille)
{
  char trameEnvoi[64];

  //Format : $PLUG;FAUTE;{COULEUR};{BLOUSE};\r\n
  switch(couleurBille)
  {
    case CouleurBille::BLANCHE:
      sprintf((char *)trameEnvoi, "%s;%s;B;%s;\r\n", entete.c_str(), nomsTrame[TypeTrame::FAUTE].c_str(), codePoche[numeroPoche].c_str());
      break;
    case CouleurBille::NOIRE:
      sprintf((char *)trameEnvoi, "%s;%s;N;%s;\r\n", entete.c_str(), nomsTrame[TypeTrame::FAUTE].c_str(), codePoche[numeroPoche].c_str());
      break;
    default:
      sprintf((char *)trameEnvoi, "%s;%s;%s;%s;\r\n", entete.c_str(), nomsTrame[TypeTrame::FAUTE].c_str(), codeCouleur[(int)couleurBille].c_str(), codePoche[numeroPoche].c_str());
      break;
  }

  ESPBluetooth.write((uint8_t*)trameEnvoi, strlen((char *)trameEnvoi));
  #ifdef DEBUG
  String trame = String(trameEnvoi);
  trame.remove(trame.indexOf("\r"), 2);
  Serial.print("> ");
  Serial.println(trame);
  #endif
}

/**
 * @brief Détermine si un des joueurs a gagné
 *
 */
bool estPartieGagnee()
{
  if(joueurGagnant[CouleurBille::ROUGE] || joueurGagnant[CouleurBille::JAUNE])
  {
    return true;
  }
  return false;
}

/**
 * @brief Détermine si un joueur a gagné
 *
 */
bool estPartieGagnee(CouleurBille couleurJoueur)
{
  return joueurGagnant[couleurJoueur];
}

/**
 * @brief Retourne le nombre de billes restantes et empochées
 *
 */
String getScore(CouleurBille couleurJoueur)
{
  String scoreJoueur;
  for(int i=0;i<nbBilles[couleurJoueur];++i)
  {
      scoreJoueur += String("O");
  }
  for(int i=nbBilles[couleurJoueur];i<NB_BILLES;++i)
  {
      scoreJoueur += String("X");
  }
  return scoreJoueur;
}

/**
 * @brief Affiche le score
 *
 */
void afficherScore()
{
  char strMessageDisplay[24];
  if (joueurCourant == CouleurBille::ROUGE)
  {
    afficheur.setMessageLigne(Afficheur::Ligne1, String(""));
    String score = getScore(CouleurBille::ROUGE);
    sprintf(strMessageDisplay, "> Rouge : %s", score.c_str());
    afficheur.setMessageLigne(Afficheur::Ligne2, String(strMessageDisplay));
    score = getScore(CouleurBille::JAUNE);
    sprintf(strMessageDisplay, "   Jaune : %s", score.c_str());
    afficheur.setMessageLigne(Afficheur::Ligne3, String(strMessageDisplay));
  }
  else if (joueurCourant == CouleurBille::JAUNE)
  {
    afficheur.setMessageLigne(Afficheur::Ligne1, String(""));
    String score = getScore(CouleurBille::ROUGE);
    sprintf(strMessageDisplay, "   Rouge : %s", score.c_str());
    afficheur.setMessageLigne(Afficheur::Ligne2, String(strMessageDisplay));
    score = getScore(CouleurBille::JAUNE);
    sprintf(strMessageDisplay, "> Jaune : %s", score.c_str());
    afficheur.setMessageLigne(Afficheur::Ligne3, String(strMessageDisplay));
  }
  //afficheur.afficher();
}

/**
 * @brief Met à jour l'affichage OLED lorsqu'il y a un changement de joueur
 *
 */
void afficherSuivant()
{
  char strMessageDisplay[24];
  if (joueurCourant == CouleurBille::ROUGE)
  {
    String score = getScore(CouleurBille::ROUGE);
    sprintf(strMessageDisplay, "   Rouge : %s", score.c_str());
    afficheur.setMessageLigne(Afficheur::Ligne1, String(strMessageDisplay));
    sprintf(strMessageDisplay, "> Rouge");
    afficheur.setMessageLigne(Afficheur::Ligne2, String(strMessageDisplay));
    score = getScore(CouleurBille::JAUNE);
    sprintf(strMessageDisplay, "   Jaune : %s", score.c_str());
    afficheur.setMessageLigne(Afficheur::Ligne3, String(strMessageDisplay));
  }
  else if (joueurCourant == CouleurBille::JAUNE)
  {
    String score = getScore(CouleurBille::JAUNE);
    sprintf(strMessageDisplay, "   Jaune : %s", score.c_str());
    afficheur.setMessageLigne(Afficheur::Ligne1, String(strMessageDisplay));
    score = getScore(CouleurBille::ROUGE);
    sprintf(strMessageDisplay, "   Rouge : %s", score.c_str());
    afficheur.setMessageLigne(Afficheur::Ligne2, String(strMessageDisplay));
    sprintf(strMessageDisplay, "> Jaune");
    afficheur.setMessageLigne(Afficheur::Ligne3, String(strMessageDisplay));
  }
  //afficheur.afficher();
}

/**
 * @brief Affiche le résultat d'un tir
 *
 */
void afficherTir(int numeroPoche, bool billeNoire = false)
{
  char strMessageDisplay[24];
  if (joueurCourant == CouleurBille::ROUGE)
  {
    String score = getScore(CouleurBille::ROUGE);
    sprintf(strMessageDisplay, "   Rouge : %s", score.c_str());
    if(!billeNoire)
      afficheur.setMessageLigne(Afficheur::Ligne1, String(strMessageDisplay));
    else
      afficheur.setMessageLigne(Afficheur::Ligne1, String("   Rouge : victoire"));
    if(numeroPoche >= 1 && numeroPoche <= NB_POCHES)
      sprintf(strMessageDisplay, "> Rouge : poche %d", numeroPoche);
    else if(numeroPoche >= TIR_LOUPE)
      sprintf(strMessageDisplay, "> Rouge : loupé");
    else
      sprintf(strMessageDisplay, "> Rouge : faute !");
    afficheur.setMessageLigne(Afficheur::Ligne2, String(strMessageDisplay));
    score = getScore(CouleurBille::JAUNE);
    sprintf(strMessageDisplay, "   Jaune : %s", score.c_str());
    afficheur.setMessageLigne(Afficheur::Ligne3, String(strMessageDisplay));
  }
  else if (joueurCourant == CouleurBille::JAUNE)
  {
    String score = getScore(CouleurBille::JAUNE);
    sprintf(strMessageDisplay, "   Jaune : %s", score.c_str());
    if(!billeNoire)
      afficheur.setMessageLigne(Afficheur::Ligne1, String(strMessageDisplay));
      else
      afficheur.setMessageLigne(Afficheur::Ligne1, String("   Jaune : victoire"));
    score = getScore(CouleurBille::ROUGE);
    sprintf(strMessageDisplay, "   Rouge : %s", score.c_str());
    afficheur.setMessageLigne(Afficheur::Ligne2, String(strMessageDisplay));
    if(numeroPoche >= 1 && numeroPoche <= NB_POCHES)
      sprintf(strMessageDisplay, "> Jaune : poche %d", numeroPoche);
    else if(numeroPoche >= TIR_LOUPE)
      sprintf(strMessageDisplay, "> Jaune : loupé");
    else
      sprintf(strMessageDisplay, "> Jaune : faute !");
    afficheur.setMessageLigne(Afficheur::Ligne3, String(strMessageDisplay));
  }
  afficheur.afficher();
}

/**
 * @brief Simule un tir
 *
 */
bool simulerTir()
{
  // déjà gagné ?
  if(estPartieGagnee())
  {
    return false;
  }

  int tir = random(0, (NB_POCHES*2)) + 1; // 1 chance sur 2 : entre 1 et 12
  #ifdef DEBUG
  if(!joueurGagnant[joueurCourant])
  {
    Serial.print("joueur ");
    Serial.print(((int)joueurCourant == CouleurBille::ROUGE) ? String("ROUGE") : String("JAUNE"));
    Serial.print(" ---> tir ");
    Serial.println((tir <= NB_POCHES) ? "poche " + String(tir) : String("loupé ou faute"));
  }
  #endif

  // empochage ?
  if(tir >= 1 && tir <= NB_POCHES)
  {
    envoyerTrameEmpoche(tir, joueurCourant);
    if(nbBilles[joueurCourant] > 0)
    {
      nbBilles[joueurCourant]--;
      afficherTir(tir);
    }
    else if(nbBilles[joueurCourant] == 0) // simule la boule noire
    {
      joueurGagnant[joueurCourant] = true;
      afficherTir(tir, true);
      return false; // on ne continue pas car on a gagné
    }
    #ifdef DEBUG
    Serial.print("joueur ");
    Serial.print(((int)joueurCourant == CouleurBille::ROUGE) ? String("ROUGE : ") : String("JAUNE : "));
    Serial.print(nbBilles[joueurCourant]);
    Serial.println(" bille(s)");
    #endif

    return true;
  }
  else if(tir >= TIR_LOUPE) // loupé : non détectable
  {
    afficherTir(tir);
  }
  else // faute : empochage d'une mauvaise bille
  {
    tir = random(0, NB_POCHES) + 1; // entre 1 et NB_POCHES
    envoyerTrameFaute(tir, (CouleurBille)random((long)CouleurBille::BLANCHE, (long)CouleurBille::NbCouleurs));
    afficherTir(tir);
  }

  return false;
}

/**
 * @brief 
 * @fn declencherTir()
 */
void declencherTir()
{
  if(etatPartie != EnCours)
    return;

  tirEncours = true;
}

/**
 * @brief Simule un séquence de tirs par un joueur
 * @fn tirer()
 */
void tirer()
{
  // déjà gagné ?
  if(estPartieGagnee())
  {
    tirEncours = false;
    return;
  }

  // déjà tiré ?
  if(etatJoueur[joueurCourant])
    return;

  if(tirEncours)
  {
    do
    {
      delay(random(1000, 4000)); // temps pour joueur
    } while (simulerTir());
    etatJoueur[joueurCourant] = true;
    tirEncours = false;
  }
}

/**
 * @brief Changement de joueur déclenché par interruption sur le bouton SW2
 * @fn terminerTir()
 */
void IRAM_ATTR terminerTir()
{
  // déjà gagné ?
  if(estPartieGagnee())
    return;

  if(etatPartie != EnCours || antiRebond || tirEncours)
    return;

  // pas tiré ?
  if(!etatJoueur[joueurCourant])
    return;

  // joueur suivant
  joueurSuivant = true;
  joueurCourant = (CouleurBille)((int(joueurCourant) + 1)%NbCouleurs);
  afficherSuivant();
  etatJoueur[joueurCourant] = false;
  envoyerTrameNext();
  antiRebond = true;
}

/**
 * @brief Lit une trame via le Bluetooth
 *
 * @fn lireTrame(String &trame)
 * @param trame la trame reçue
 * @return bool true si une trame a été lue, sinon false
 */
bool lireTrame(String &trame)
{
  if (ESPBluetooth.available())
  {
    #ifdef DEBUG_VERIFICATION
    Serial.print("Disponible : ");
    Serial.println(ESPBluetooth.available());
    #endif
    trame.clear();
    trame = ESPBluetooth.readStringUntil(DELIMITEUR_FIN);
    #ifdef DEBUG
    Serial.print("< ");
    Serial.println(trame);
    #endif
    trame.concat(DELIMITEUR_FIN); // remet le DELIMITEUR_FIN
    return true;
  }

  return false;
}

/**
 * @brief Vérifie si la trame reçue est valide et retorune le type de la trame
 *
 * @fn verifierTrame(String &trame)
 * @param trame
 * @return TypeTrame le type de la trame
 */
TypeTrame verifierTrame(String &trame)
{
  String format;

  for(int i=0;i<TypeTrame::NB_TRAMES;i++)
  {
    //Format : ${TYPE}\r\n
    format = entete + separateur + nomsTrame[i] + separateur + delimiteurFin;
    #ifdef DEBUG_VERIFICATION
    Serial.print("Verification trame : ");
    Serial.print(format);
    #endif
    if(trame == format)
    {
      return (TypeTrame)i;
    }
    else
    {
      #ifdef DEBUG_VERIFICATION
      Serial.println("");
      #endif
    }
  }
  #ifdef DEBUG_VERIFICATION
  Serial.println("Type de trame : inconnu");
  #endif
  return Inconnu;
}

/**
 * @brief Réinitialise l'affichage OLED des lignes
 *
 */
void reinitialiserAffichage()
{
  afficheur.setMessageLigne(Afficheur::Ligne1, "");
  afficheur.setMessageLigne(Afficheur::Ligne2, "");
  afficheur.setMessageLigne(Afficheur::Ligne3, "");
  refresh = true;
}

/**
* @brief Initialise les ressources du programme
*
* @fn setup
*
*/
void setup()
{
  Serial.begin(115200);
  while (!Serial);

  pinMode(GPIO_LED_ROUGE, OUTPUT);
  pinMode(GPIO_LED_ORANGE, OUTPUT);
  pinMode(GPIO_LED_VERTE, OUTPUT);

  //pinMode(GPIO_SW1, INPUT_PULLUP);
  //attachInterrupt(digitalPinToInterrupt(GPIO_SW1), declencherTir, FALLING);
  pinMode(GPIO_SW2, INPUT_PULLUP);
  attachInterrupt(digitalPinToInterrupt(GPIO_SW2), terminerTir, FALLING);

  digitalWrite(GPIO_LED_ROUGE, HIGH);
  digitalWrite(GPIO_LED_ORANGE, LOW);
  digitalWrite(GPIO_LED_VERTE, LOW);

  afficheur.initialiser();

  String titre = "";
  String stitre = "=====================";

  #ifdef BLUETOOTH
  #ifdef BLUETOOTH_MASTER
  String nomBluetooth = "iot-esp-maitre";
  ESPBluetooth.begin(nomBluetooth, true);
  const uint8_t* adresseESP32 = esp_bt_dev_get_address();
  char str[18];
  sprintf(str, "%02x:%02x:%02x:%02x:%02x:%02x", adresseESP32[0], adresseESP32[1], adresseESP32[2], adresseESP32[3], adresseESP32[4], adresseESP32[5]);
  stitre = String("== ") + String(str) + String(" ==");
  titre = nomBluetooth;
  #else
  String nomBluetooth = "pool-1";
  ESPBluetooth.begin(nomBluetooth);
  const uint8_t* adresseESP32 = esp_bt_dev_get_address();
  char str[18];
  sprintf(str, "%02x:%02x:%02x:%02x:%02x:%02x", adresseESP32[0], adresseESP32[1], adresseESP32[2], adresseESP32[3], adresseESP32[4], adresseESP32[5]);
  stitre = String("=== ") + String(str) + String(" ===");
  titre = String("Bluetooth : ") + nomBluetooth + String("     2022");
  #endif
  #endif

  #ifdef DEBUG
  Serial.println(titre);
  Serial.println(stitre);
  #endif

  afficheur.setTitre(titre);
  afficheur.setSTitre(stitre);
  afficheur.afficher();

  // initialise le générateur pseudo-aléatoire
  //Serial.println(randomSeed(analogRead(34)));
  Serial.println(esp_random());
}

/**
* @brief Boucle infinie d'exécution du programme
*
* @fn loop
*
*/
void loop()
{
  String trame;
  TypeTrame typeTrame;

  if(refresh)
  {
    afficheur.afficher();
    refresh = false;
  }

  if(antiRebond && !estPartieGagnee())
  {
    afficheur.afficher();
    delay(300);
    antiRebond = false;
  }

  if(etatPartie == EnCours)
  {
    tirer();
  }

  if(joueurSuivant)
  {
    #ifdef DEBUG
    Serial.print("Joueur ROUGE : ");
    Serial.print(nbBilles[CouleurBille::ROUGE]);
    Serial.print(" bille(s) ");
    Serial.print((joueurGagnant[CouleurBille::ROUGE]) ? String("gagné ") : String(" "));
    for(int i=0;i<nbBilles[CouleurBille::ROUGE];++i)
    {
        Serial.print("O");
    }
    for(int i=nbBilles[CouleurBille::ROUGE];i<NB_BILLES_ROUGE;++i)
    {
        Serial.print("X");
    }
    Serial.println("");
    Serial.print("Joueur JAUNE : ");
    Serial.print(nbBilles[CouleurBille::JAUNE]);
    Serial.print(" bille(s) ");
    Serial.print((joueurGagnant[CouleurBille::JAUNE]) ? String("gagné ") : String(" "));
    for(int i=0;i<nbBilles[CouleurBille::JAUNE];++i)
    {
        Serial.print("O");
    }
    for(int i=nbBilles[CouleurBille::JAUNE];i<NB_BILLES_JAUNE;++i)
    {
        Serial.print("X");
    }
    Serial.println("");
    if (joueurCourant == CouleurBille::ROUGE)
    {
      Serial.println("Suivant : ROUGE");
    }
    else
    {
      Serial.println("Suivant : JAUNE");
    }
    #endif
    joueurSuivant = false;
    if(!estPartieGagnee())
      declencherTir(); // au suivant de jouer
  }

  if(lireTrame(trame))
  {
    typeTrame = verifierTrame(trame);
    refresh = true;
    #ifdef DEBUG
    if(typeTrame > Inconnu)
      Serial.println("\nTrame : " + nomsTrame[typeTrame]);
    #endif
    switch (typeTrame)
    {
      case Inconnu:
        envoyerTrameErreur(ERREUR_TRAME_INCONNUE);
        afficheur.setMessageLigne(Afficheur::Ligne4, String("Inconnue --> ?"));
        break;
      case TypeTrame::START:
        if(etatPartie == Finie)
        {
          reinitialiserAffichage();
          envoyerTrameAcquittement();
          joueurCourant = (CouleurBille)random(0, 2);
          nbBilles[CouleurBille::ROUGE] = NB_BILLES_ROUGE;
          nbBilles[CouleurBille::JAUNE] = NB_BILLES_JAUNE;
          etatJoueur[CouleurBille::ROUGE] = false;
          etatJoueur[CouleurBille::JAUNE] = false;
          joueurGagnant[CouleurBille::ROUGE] = false;
          joueurGagnant[CouleurBille::JAUNE] = false;
          etatPartie = EnCours;
          digitalWrite(GPIO_LED_ROUGE, LOW);
          digitalWrite(GPIO_LED_ORANGE, LOW);
          digitalWrite(GPIO_LED_VERTE, HIGH);
          afficherScore();
          afficheur.setMessageLigne(Afficheur::Ligne4, nomsTrame[typeTrame] + String(" --> En cours"));
          afficheur.afficher();
          #ifdef DEBUG
          Serial.println("Nouvelle partie");
          #endif
          declencherTir(); // c'est parti !
        }
        else
          envoyerTrameErreur(ERREUR_TRAME_ETAT);
        break;
      /*case TypeTrame::PAUSE:
        if(etatPartie == EnCours)
        {
          envoyerTrameAcquittement();
          etatPartie = EnPause;
          digitalWrite(GPIO_LED_ROUGE, LOW);
          digitalWrite(GPIO_LED_ORANGE, HIGH);
          digitalWrite(GPIO_LED_VERTE, LOW);
          afficheur.setMessageLigne(Afficheur::Ligne4, nomsTrame[typeTrame] + String(" --> En pause"));
          afficheur.afficher();
        }
        else
          envoyerTrameErreur(ERREUR_TRAME_ETAT);
        break;*/
      /*case TypeTrame::PLAY:
        if(etatPartie == EnPause)
        {
          envoyerTrameAcquittement();
          etatPartie = EnCours;
          digitalWrite(GPIO_LED_ROUGE, LOW);
          digitalWrite(GPIO_LED_ORANGE, LOW);
          digitalWrite(GPIO_LED_VERTE, HIGH);
          afficheur.setMessageLigne(Afficheur::Ligne4, nomsTrame[typeTrame] + String(" --> En cours"));
          afficheur.afficher();
        }
        else
          envoyerTrameErreur(ERREUR_TRAME_ETAT);
        break;*/
      case TypeTrame::STOP:
        if(etatPartie > Finie)
        {
          reinitialiserAffichage();
          envoyerTrameAcquittement();
          etatPartie = Finie;
          digitalWrite(GPIO_LED_ROUGE, HIGH);
          digitalWrite(GPIO_LED_ORANGE, LOW);
          digitalWrite(GPIO_LED_VERTE, LOW);
          afficheur.setMessageLigne(Afficheur::Ligne4, nomsTrame[typeTrame] + String(" --> Finie"));
          afficheur.afficher();
        }
        else
          envoyerTrameErreur(ERREUR_TRAME_ETAT);
        break;
      case TypeTrame::RESET:
          reinitialiserAffichage();
          envoyerTrameAcquittement();
          etatPartie = Finie;
          digitalWrite(GPIO_LED_ROUGE, HIGH);
          digitalWrite(GPIO_LED_ORANGE, LOW);
          digitalWrite(GPIO_LED_VERTE, LOW);
          afficheur.setMessageLigne(Afficheur::Ligne4, nomsTrame[typeTrame] + String(" --> Annulée"));
          reinitialiserAffichage();
        break;
      case TypeTrame::CONFIG:
          envoyerTrameErreur(ERREUR_TRAME_NON_SUPPORTEE);
          digitalWrite(GPIO_LED_ORANGE, HIGH);
          delay(150);
          digitalWrite(GPIO_LED_ORANGE, LOW);
          afficheur.setMessageLigne(Afficheur::Ligne4, nomsTrame[typeTrame]);
        break;
      case TypeTrame::ACK:
          digitalWrite(GPIO_LED_ORANGE, HIGH);
          delay(150);
          digitalWrite(GPIO_LED_ORANGE, LOW);
          afficheur.setMessageLigne(Afficheur::Ligne4, nomsTrame[typeTrame]);
        break;
      default:
        #ifdef DEBUG
        Serial.println("Trame invalide !");
        #endif
        break;
    }
  }
}
