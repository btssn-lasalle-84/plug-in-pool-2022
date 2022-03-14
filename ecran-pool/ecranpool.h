#ifndef ECRANPOOL_H
#define ECRANPOOL_H

/**
 * @file ecranpool.h
 *
 * @brief Déclaration de la classe EcranPool
 * @author
 * @version 1.0
 *
 */

#include <QtWidgets>

/**
 * @def TEST_ECRANPOOL
 * @brief Pour le mode test (raccourcis clavier)
 */
#define TEST_ECRANPOOL

/**
 * @def PLEIN_ECRAN
 * @brief Pour le mode kiosque
 */
#define PLEIN_ECRAN

QT_BEGIN_NAMESPACE
namespace Ui
{
class EcranPool;
}
QT_END_NAMESPACE

/**
 * @class EcranPool
 * @brief Déclaration de la classe EcranPool
 * @details Cette classe s'occupe de l'affichage sur l'écran
 */
class EcranPool : public QMainWindow
{
    Q_OBJECT

  public:
    EcranPool(QWidget* parent = nullptr);
    ~EcranPool();

  private:
    Ui::EcranPool* ui; //!< la fenêtre graphique associée à cette classe

    /**
     * @enum Ecran
     * @brief Définit les différents écrans de l'IHM
     *
     */
    enum Ecran
    {
        Accueil = 0,
        Manche,
        Resume,
        NbEcrans
    };

#ifdef TEST_ECRANPOOL
    void creerRaccourcisClavier();
#endif

  public slots:
    void afficherEcran(EcranPool::Ecran ecran);
    void afficherEcranPrecedent();
    void afficherEcranSuivant();
    void fermerApplication();
};

#endif // ECRANPOOL_H
