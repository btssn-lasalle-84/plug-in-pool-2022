package com.lasalle.pluginpool;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class IHMSplashScreen extends AppCompatActivity
{
    /**
     * Constantes
     */
    private final static String TAG = "_IHMSplashScreen_";

    /**
     * Ressources IHM
     */
    private Animation animation;
    private ImageView imageView;

    /**
     * @brief Méthode appelée à la création de l'activité
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ihm_splash_screen);
        Log.d(TAG, "onCreate()");

        imageView = (ImageView)findViewById(R.id.logoSplashScreen);
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);

        animation.setAnimationListener(new Animation.AnimationListener()
        {
           @Override
           public void onAnimationStart(Animation animation)
           {
           }

           @RequiresApi(api = Build.VERSION_CODES.O)
           @Override
           public void onAnimationEnd(Animation animation)
           {
               Log.d(TAG, "Animation finie !");
               Intent intent = new Intent(IHMSplashScreen.this, IHMPlugInPool.class);
               startActivity(intent);
           }

           @Override
           public void onAnimationRepeat(Animation animation)
           {
           }
        });
        imageView.startAnimation(animation);
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
}
