package com.jabezmagomere.keepfit;

import android.content.Intent;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.daimajia.androidanimations.library.Techniques;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

import spencerstudios.com.bungeelib.Bungee;

public class SplashScreen extends AwesomeSplash {
    private SessionManager sessionManager;
    private Intent intent;


    @Override
    public void initSplash(ConfigSplash configSplash) {
        sessionManager=new SessionManager(SplashScreen.this);

        //Customize Circular Reveal
        configSplash.setBackgroundColor(R.color.card_backgroundColor); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(2000); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP

        //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default

        //Customize Logo
        configSplash.setLogoSplash(R.drawable.dumbell100); //or any other drawabl
        configSplash.setAnimLogoSplashDuration(2000); //int ms
        configSplash.setAnimLogoSplashTechnique(Techniques.Bounce); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)


        //Customize Path

        configSplash.setOriginalHeight(400); //in relation to your svg (path) resource
        configSplash.setOriginalWidth(400); //in relation to your svg (path) resource
        configSplash.setAnimPathStrokeDrawingDuration(3000);
        configSplash.setPathSplashStrokeSize(3); //I advise value be <5
        configSplash.setPathSplashStrokeColor(R.color.info_background); //any color you want form colors.xml
        configSplash.setAnimPathFillingDuration(3000);
        configSplash.setPathSplashFillColor(R.color.work_out_bkg); //path object filling color


        //Customize Title
        configSplash.setTitleSplash("Keep Fit");
        configSplash.setTitleTextColor(R.color.info_background);
        configSplash.setTitleTextSize(30f); //float value
        configSplash.setAnimTitleDuration(3000);
        configSplash.setAnimTitleTechnique(Techniques.FlipInX);



    }

    @Override
    public void animationsFinished() {
        if(sessionManager.isLoggedIn()){
            intent=new Intent(SplashScreen.this,MainActivity.class);
            startActivity(intent);
            Bungee.swipeLeft(SplashScreen.this);
        }else{
            intent=new Intent(SplashScreen.this,LoginRegHost.class);
            startActivity(intent);
            Bungee.swipeLeft(SplashScreen.this);
        }

    }
}
