package com.lasalle.pluginpool;

/**
 * @file IHMNouvelleRencontre.java
 * @brief Déclaration de la classe IHMNouvelleRencontre
 * @author MERAS Pierre
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * @class IHMNouvelleRencontre
 * @brief L'activité pour une nouvelle rencontre
 */

public class IHMNouvelleRencontre extends AppCompatActivity
{
    /**
     * Constantes
     */
    private static final String TAG = "_IHMNouvelleRencontre_";  //!< TAG pour les logs

    /**
     * Attributs
     */
    PeripheriqueBluetooth peripheriqueBluetooth = null;
    private Handler handler = null;
    String[] messageDecoupe = null;

    /**
     * Ressources IHM
     */
    private Button boutonLancerRencontre;//!< Le bouton permettant d'accèder à l'historique des rencontres

    /**
     * @brief Méthode appelée à la création de l'activité
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ihm_nouvelle_rencontre);
        Log.d(TAG, "onCreate()");
        gererHandler();
        initialiserRessourcesBluetooth();
        initialiserRessourcesIHMNouvelleRencontre();
    }

    /**
     * @brief Méthode appelée au démarrage après le onCreate() ou un restart après un onStop()
     */
    @Override
    protected void onStart()
    {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    /**
     * @brief Méthode appelée après onStart() ou après onPause()
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    /**
     * @brief Méthode appelée après qu'une boîte de dialogue s'est affichée (on reprend sur un onResume()) ou avant onStop() (activité plus visible)
     */
    @Override
    protected void onPause()
    {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    /**
     * @brief Méthode appelée lorsque l'activité n'est plus visible
     */
    @Override
    protected void onStop()
    {
        super.onStop();
        Log.d(TAG, "onStop()");
        peripheriqueBluetooth.deconnecter();
    }

    /**
     * @brief Méthode appelée à la destruction de l'application (après onStop() et détruite par le système Android)
     */
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    /**
     * @brief Initialise les ressources graphiques de l'activité
     */
    private void initialiserRessourcesIHMNouvelleRencontre()
    {
        boutonLancerRencontre = (Button)findViewById(R.id.boutonLancerRencontre);

        boutonLancerRencontre.setOnClickListener(
        new View.OnClickListener()
        {
            public void onClick(View v)
            {
                initialiserRessourcesBluetooth();
                Intent intent = new Intent(IHMNouvelleRencontre.this, IHMRencontreEnCours.class);
                startActivity(intent);
            }
        });
    }

    /**
     * @brief Initialise les ressources bluetooth
     */
    private void initialiserRessourcesBluetooth()
    {
        Log.d(TAG,"initialiserRessourcesIHMNouvelleRencontre()");
        peripheriqueBluetooth = PeripheriqueBluetooth.getInstance(handler);
        if(peripheriqueBluetooth.rechercherTable("pool-1"))
        {
            peripheriqueBluetooth.connecter();
        }
    }

    /**
     * @brief Gère le handler pour la communication
     */
    private void gererHandler()
    {
        this.handler = new Handler(this.getMainLooper())
        {
            @Override
            public void handleMessage(@NonNull Message message)
            {
                Log.d(TAG, "[Handler] id du message = " + message.what);
                Log.d(TAG, "[Handler] contenu du message = " + message.obj.toString());

                switch (message.what)
                {
                    case PeripheriqueBluetooth.CODE_CREATION_SOCKET:
                        Log.d(TAG, "[Handler] CREATION_SOCKET = " + message.obj.toString());
                        break;
                    case PeripheriqueBluetooth.CODE_CONNEXION_SOCKET:
                        Log.d(TAG, "[Handler] CODE_CONNEXION_SOCKET = " + message.obj.toString());
                        peripheriqueBluetooth.envoyer(Protocole.trameCommencer);
                        break;
                    case PeripheriqueBluetooth.CODE_DECONNEXION_SOCKET:
                        Log.d(TAG, "[Handler] DECONNEXION_SOCKET = " + message.obj.toString());
                        break;
                    case PeripheriqueBluetooth.CODE_RECEPTION_TRAME:
                        Log.d(TAG, "[Handler] RECEPTION_TRAME = " + message.obj.toString());
                        gererMessage(message.obj.toString());
                        break;
                }
            }
        };
    }

    /**
     * @brief Gère le message reçu pour détecter une mauvaise trame
     */
    private void gererMessage(String message)
    {
        messageDecoupe = message.split(Protocole.delimiteurChamp);

        if(!messageDecoupe[0].equals(Protocole.delimiteurDebut) && !messageDecoupe[messageDecoupe.length].equals(Protocole.delimiteurFin))
        {
            Log.d(TAG, "[Erreur] : " + Protocole.CODE_ERREUR_TRAME_NONSUPPORTEE);
            return;
        }
        else if(!messageDecoupe[0].equals(Protocole.delimiteurDebut))
        {
            Log.d(TAG, "[Erreur] : " + Protocole.CODE_ERREUR_TRAME_INCONNUE);
            return;
        }

        switch (messageDecoupe[0])
        {
            case Protocole.EMPOCHE:
            case Protocole.FAUTE:
                Log.d(TAG, "Joueur = " + messageDecoupe[2] + "Blouse = " + messageDecoupe[3]);
                break;
            case Protocole.SUIVANT:
                Log.d(TAG, "gererMessage() = suivant");
                break;
        }
    }
}