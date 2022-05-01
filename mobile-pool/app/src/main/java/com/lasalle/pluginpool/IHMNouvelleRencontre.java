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
    private static final String trameArreter = "$PLUG;STOP;\r\n";
    private static final String trameCommencer = "$PLUG;START;\r\n";
    private static final String trameAnnuler = "$PLUG;RESET;\r\n";
    private static final String trameAcquitemment = "$PLUG;ACK;\r\n";

    private static final String empoche = "EMPOCHE";
    private static final String faute = "FAUTE";
    private static final String suivant = "NEXT";

    /**
     * Attributs
     */
    PeripheriqueBluetooth peripheriqueBluetooth = null;
    private Handler handler = null;

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

    private void initialiserRessourcesBluetooth()
    {
        Log.d(TAG,"initialiserRessourcesIHMNouvelleRencontre()");
        peripheriqueBluetooth = new PeripheriqueBluetooth(handler);
        if(peripheriqueBluetooth.rechercherTable("pool-1"))
        {
            peripheriqueBluetooth.connecter();
        }
    }

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
                        peripheriqueBluetooth.envoyer(trameCommencer);
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

    private void gererMessage(String message)
    {
        String[] messageDecoupe = message.split(";");
        for (int i = 0; i < messageDecoupe.length; i++)
        {
            Log.d(TAG, "gererMessage() = " + messageDecoupe[i]);
        }

        switch (messageDecoupe[1])
        {
            case empoche:
                Log.d(TAG, "gererMessage() : cas empoche");
                
                break;
            case faute:
                Log.d(TAG, "gererMessage() = cas faute");
                break;
            case suivant:
                Log.d(TAG, "gererMessage() = cas suivant");
                break;
        }

    }
}