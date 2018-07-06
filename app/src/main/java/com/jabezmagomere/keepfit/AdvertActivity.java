package com.jabezmagomere.keepfit;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import es.dmoral.toasty.Toasty;
import spencerstudios.com.bungeelib.Bungee;

public class AdvertActivity extends AppCompatActivity {
    private InterstitialAd interstitialAd;
    private Toolbar toolbar;
    private Button btnRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert);
        btnRefresh=(Button)findViewById(R.id.btnRefresh);
        toolbar=(Toolbar)findViewById(R.id.advert_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.advertisements));
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        MobileAds.initialize(this,getString(R.string.admob_id));
        interstitialAd=new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        interstitialAd.loadAd(new AdRequest.Builder().build());
        interstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                showInterstitial();
                Toasty.success(AdvertActivity.this,"Ad Loaded", Toast.LENGTH_SHORT,true).show();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Toasty.error(AdvertActivity.this,"Ad Failed to Load, Retry", Toast.LENGTH_SHORT,true).show();
            }

            @Override
            public void onAdClosed() {
                Toasty.info(AdvertActivity.this,"Thank you for the Support", Toast.LENGTH_SHORT,true).show();
            }
        });

        showInterstitial();
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interstitialAd.loadAd(new AdRequest.Builder().build());
                interstitialAd.setAdListener(new AdListener(){
                    @Override
                    public void onAdLoaded() {
                        showInterstitial();
                        Toasty.success(AdvertActivity.this,"Ad Loaded", Toast.LENGTH_SHORT,true).show();
                    }

                    @Override
                    public void onAdFailedToLoad(int i) {
                        super.onAdFailedToLoad(i);
                        Toasty.error(AdvertActivity.this,"Ad Failed to Load, Retry", Toast.LENGTH_SHORT,true).show();
                    }

                    @Override
                    public void onAdClosed() {
                        Toasty.info(AdvertActivity.this,"Thank you for the Support", Toast.LENGTH_SHORT,true).show();
                    }
                });

                showInterstitial();
            }
        });

    }
    private void showInterstitial(){
        if(interstitialAd.isLoaded()){
            interstitialAd.show();
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Bungee.swipeRight(AdvertActivity.this);
    }
}
