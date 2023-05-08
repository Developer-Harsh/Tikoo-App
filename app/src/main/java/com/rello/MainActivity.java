package com.rello;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.snbapps.rello.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.review.ReviewManager;
import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Developed by > Harsh Kumar Singh (snbApps)
 * UI/UX Designer > Harsh Kumar Singh (snbApps)
 * Date > 11/04/2021
 */

public class MainActivity extends AppCompatActivity {

    private boolean Vibration;
    private static final String PREFS_NAME = "vibration";
    private static final String PREF_VIBRATION = "TicVib";
    private RelativeLayout layout;
    private ReviewManager reviewManager;
    AppUpdateManager appUpdateManager;
    int RequestUpdate = 1;
    private RewardedAd rewardedAd;
    private InterstitialAd interstitialAd;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadRewardedAd();
        loadInterstitial();

        FirebaseAnalytics analytics = FirebaseAnalytics.getInstance(this);
        Window w = getWindow();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Button Cmp = (Button) findViewById(R.id.computer_mode);
        Button FM = (Button) findViewById(R.id.friends_mode);
        Button online = (Button) findViewById(R.id.online_mode);
        layout = (RelativeLayout) findViewById(R.id.layout_main);

        layout.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            }
        });

        Cmp.setOnClickListener(v -> {
            if (interstitialAd != null) {
                interstitialAd.show(MainActivity.this);
            } else {
                Intent intent = new Intent(MainActivity.this, NameActivity.class);
                startActivity(intent);
            }
        });

        online.setOnClickListener(v -> {
            if (interstitialAd != null) {
                interstitialAd.show(MainActivity.this);
            } else {
                Intent intent = new Intent(MainActivity.this, OnlineNameActivity.class);
                startActivity(intent);
            }
        });

        FM.setOnClickListener(v -> {
            if (interstitialAd != null) {
                interstitialAd.show(MainActivity.this);
            } else {
                Intent intent = new Intent(MainActivity.this, TwoNameActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout settings = findViewById(R.id.settings);
        settings.setOnClickListener(v -> {
            if (rewardedAd != null) {
                Activity activity = MainActivity.this;
                rewardedAd.show(activity, rewardItem -> {
                    Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(intent);
                });
            } else {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        appUpdateManager = AppUpdateManagerFactory.create(this);
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(result -> {
            if((result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE)
                    && result.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            result,
                            AppUpdateType.IMMEDIATE,
                            MainActivity.this,
                            RequestUpdate);
                }
                catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(MainActivity.this, JoyManager.INTERSTITIAL, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(result -> {
            if(result.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            result,
                            AppUpdateType.IMMEDIATE,
                            MainActivity.this,
                            RequestUpdate);
                }
                catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadRewardedAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, JoyManager.REWARDED, adRequest, new RewardedAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                super.onAdLoaded(rewardedAd);
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}