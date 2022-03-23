#include "ecranpool.h"
#include "ui_ecranpool.h"
#include <QDebug>

/**
 * @file ecranpool.cpp
 *
 * @brief Définition de la classe EcranPool
 * @author
 * @version 1.0
 *
 */

/**
 * @brief Constructeur de la classe EcranPool
 *
 * @fn EcranPool::EcranPool
 * @param parent L'adresse de l'objet parent, si nullptr EcranPool sera la
 * fenêtre principale de l'application
 */
EcranPool::EcranPool(QWidget* parent) :
    QMainWindow(parent), ui(new Ui::EcranPool)
{
    ui->setupUi(this);
    qDebug() << Q_FUNC_INFO;

#ifdef TEST_ECRANPOOL
    creerRaccourcisClavier();
#endif

    horloge = new QTimer(this);
    connect(horloge, SIGNAL(timeout()), this, SLOT(afficherHeure()));
    horloge->start(500);

    afficherEcran(EcranPool::Accueil);

#ifdef PLEIN_ECRAN
    showFullScreen();
// showMaximized();
#endif
}

/**
 * @brief Destructeur de la classe EcranPool
 *
 * @fn EcranPool::~EcranPool
 * @details Libère les ressources de l'application
 */
EcranPool::~EcranPool()
{
    delete ui;
    qDebug() << Q_FUNC_INFO;
}

void EcranPool::afficherHeure()
{
    qDebug() << Q_FUNC_INFO;
    QDateTime maintenant = QDateTime::currentDateTime();
    ui->labelHorodatage->setText(maintenant.toString("hh:mm"));
}

/**
 * @brief Méthode qui permet d'afficher un numéro d'écran de la pile
 * QStackedWidget
 *
 * @fn EcranPool::afficherEcran
 * @param ecran le numéro d'écran à afficher
 */
void EcranPool::afficherEcran(EcranPool::Ecran ecran)
{
    qDebug() << Q_FUNC_INFO << "ecran" << ecran;
    ui->ecrans->setCurrentIndex(ecran);
}

/**
 * @brief Méthode qui permet d'afficher l'écran suivant dans la pile
 * QStackedWidget
 *
 * @fn EcranPool::afficherEcranSuivant
 */
void EcranPool::afficherEcranSuivant()
{
    int ecranCourant = EcranPool::Ecran(ui->ecrans->currentIndex());
    int ecranSuivant = (ecranCourant + 1) % int(EcranPool::NbEcrans);
    afficherEcran(EcranPool::Ecran(ecranSuivant));
}

/**
 * @brief Méthode qui permet d'afficher l'écran précédent dans la pile
 * QStackedWidget
 *
 * @fn EcranPool::afficherEcranPrecedent
 */
void EcranPool::afficherEcranPrecedent()
{
    int ecranCourant   = ui->ecrans->currentIndex();
    int ecranPrecedent = (ecranCourant - 1) % int(EcranPool::NbEcrans);
    if(ecranPrecedent == -1)
        ecranPrecedent = int(EcranPool::NbEcrans) - 1;
    afficherEcran(EcranPool::Ecran(ecranPrecedent));
}

/**
 * @brief Méthode qui permet de fermer la fenêtre principale de l'application
 *
 * @fn EcranPool::fermerApplication
 */
void EcranPool::fermerApplication()
{
    this->close();
    qDebug() << Q_FUNC_INFO;
}

#ifdef TEST_ECRANPOOL
/**
 * @brief Méthode qui initialise les raccourcis clavier
 *
 * @fn EcranPool::creerRaccourcisClavier
 */
void EcranPool::creerRaccourcisClavier()
{
    // Ctrl-Q pour quitter
    QAction* quitter = new QAction(this);
    quitter->setShortcut(QKeySequence(Qt::CTRL + Qt::Key_Q));
    addAction(quitter);
    connect(quitter, SIGNAL(triggered()), this, SLOT(fermerApplication()));

    // Flèche droite pour écran suivant
    QAction* actionAllerDroite = new QAction(this);
    actionAllerDroite->setShortcut(QKeySequence(Qt::Key_Right));
    addAction(actionAllerDroite);
    connect(actionAllerDroite,
            SIGNAL(triggered()),
            this,
            SLOT(afficherEcranSuivant()));

    // Flèche gauche pour écran précédent
    QAction* actionAllerGauche = new QAction(this);
    actionAllerGauche->setShortcut(QKeySequence(Qt::Key_Left));
    addAction(actionAllerGauche);
    connect(actionAllerGauche,
            SIGNAL(triggered()),
            this,
            SLOT(afficherEcranPrecedent()));
}
#endif
