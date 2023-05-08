package com.rello;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.analytics.FirebaseAnalytics;

import com.snbapps.rello.R;

import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.GifImageView;

/**
 * Developed by > Harsh Kumar Singh (snbApps)
 * UI/UX Designer > Harsh Kumar Singh (snbApps)
 * Date > 11/04/2021
 */

public class ChooseActivity extends AppCompatActivity {

    CharSequence player1 = "Player 1";
    CharSequence player2 = "Player 2";
    public boolean selectedsingleplayer;
    private AdView mAdView;
    private InterstitialAd interstitialAd;
    boolean player1ax, isSelected, isFirstChoose;
    private String shape;
    private GifImageView hotFace, beamingFace, clappingHands, coldFace, confettiFace, crying, explodingFace, openMouth, loudlyCrying, partyFace, seeNoEvil, thumbsUp, triumphImage, zanyFace;
    private TextView label;
    private Timer timer;
    private ImageButton back, continue_next;
    private RelativeLayout layout;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        loadAd();
        FirebaseAnalytics analytics = FirebaseAnalytics.getInstance(this);
        Window w = getWindow();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final SharedPreferences sharedPreferences = getSharedPreferences("pref", MODE_PRIVATE);

        layout = (RelativeLayout) findViewById(R.id.layout_choose);
        layout.setOnTouchListener(new OnSwipeTouchListener(ChooseActivity.this) {
            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                finish();
            }

            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                if (isSelected) {
                    CharSequence[] players1 = getIntent().getCharSequenceArrayExtra("playersnames");
                    player1 = players1[0];
                    player2 = players1[1];
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    if (!isFirstChoose) {
                        editor.putString("first", shape);
                        editor.apply();
                        clearAll(shape);
                        isFirstChoose = true;
                    } else {
                        editor.putString("second", shape);
                        editor.apply();
                        Intent i = new Intent(ChooseActivity.this, SceneActivity.class);
                        CharSequence[] players = {player1, player2};
                        i.putExtra("playersnames", players);
                        i.putExtra("player1ax", player1ax);
                        i.putExtra("selectedsingleplayer", selectedsingleplayer);
                        startActivity(i);
                    }
                }
            }
        });

        hotFace = (GifImageView) findViewById(R.id.hotface_image);
        beamingFace = (GifImageView) findViewById(R.id.beaming_face_ws_image);
        clappingHands = (GifImageView) findViewById(R.id.clapping_hands_image);
        coldFace = (GifImageView) findViewById(R.id.cold_face_image);
        confettiFace = (GifImageView) findViewById(R.id.confetti_ball_image);
        crying = (GifImageView) findViewById(R.id.crying_image);
        explodingFace = (GifImageView) findViewById(R.id.exploding_head_image);
        openMouth = (GifImageView) findViewById(R.id.open_mouth_face_image);
        loudlyCrying = (GifImageView) findViewById(R.id.loudly_cying_image);
        partyFace = (GifImageView) findViewById(R.id.party_face_image);
        seeNoEvil = (GifImageView) findViewById(R.id.see_no_evil_image);
        thumbsUp = (GifImageView) findViewById(R.id.thumbs_up_image);
        triumphImage = (GifImageView) findViewById(R.id.triumph_image);
        zanyFace = (GifImageView) findViewById(R.id.zany_face_image);
        back = (ImageButton) findViewById(R.id.back_ib_choose);
        label = (TextView) findViewById(R.id.label);
        continue_next = (ImageButton) findViewById(R.id.continue_ib_choose);

        CharSequence[] players = getIntent().getCharSequenceArrayExtra("playersnames");
        player1 = players[0];
        player2 = players[1];

        selectedsingleplayer = getIntent().getBooleanExtra("selectedsingleplayer", true);
        if (!selectedsingleplayer) {
            label.setText(player1 + " pick your side");
        } else {
            label.setText("Pick your side");
        }

        hotFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.transparent));
        beamingFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
        clappingHands.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
        coldFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
        confettiFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
        crying.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
        explodingFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
        openMouth.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
        loudlyCrying.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
        partyFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
        seeNoEvil.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
        thumbsUp.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
        triumphImage.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
        zanyFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));

        back.setOnClickListener(v -> {
            if (interstitialAd != null) {
                interstitialAd.show(ChooseActivity.this);
            } else {
                finish();
            }
        });

        hotFace.setOnClickListener(v -> {
            isSelected = true;
            player1ax = false;
            shape = "hotFace";

            hotFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.transparent));
            beamingFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            clappingHands.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            coldFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            confettiFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            crying.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            explodingFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            openMouth.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            loudlyCrying.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            partyFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            seeNoEvil.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            thumbsUp.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            triumphImage.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            zanyFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
        });

        beamingFace.setOnClickListener(v -> {
            isSelected = true;
            player1ax = false;
            shape = "beamingFace";

            hotFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            beamingFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.transparent));
            clappingHands.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            coldFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            confettiFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            crying.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            explodingFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            openMouth.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            loudlyCrying.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            partyFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            seeNoEvil.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            thumbsUp.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            triumphImage.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            zanyFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
        });

        clappingHands.setOnClickListener(v -> {
            isSelected = true;
            player1ax = false;
            shape = "clappingHands";

            hotFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            beamingFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            clappingHands.setColorFilter(getApplicationContext().getResources().getColor(R.color.transparent));
            coldFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            confettiFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            crying.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            explodingFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            openMouth.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            loudlyCrying.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            partyFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            seeNoEvil.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            thumbsUp.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            triumphImage.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            zanyFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
        });

        coldFace.setOnClickListener(v -> {
            isSelected = true;
            player1ax = false;
            shape = "coldFace";

            hotFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            beamingFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            clappingHands.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            coldFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.transparent));
            confettiFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            crying.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            explodingFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            openMouth.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            loudlyCrying.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            partyFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            seeNoEvil.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            thumbsUp.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            triumphImage.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            zanyFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
        });

        confettiFace.setOnClickListener(v -> {
            isSelected = true;
            player1ax = false;
            shape = "confettiFace";

            hotFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            beamingFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            clappingHands.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            coldFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            confettiFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.transparent));
            crying.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            explodingFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            openMouth.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            loudlyCrying.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            partyFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            seeNoEvil.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            thumbsUp.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            triumphImage.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            zanyFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
        });

        crying.setOnClickListener(v -> {
            isSelected = true;
            player1ax = false;
            shape = "cryingFace";

            hotFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            beamingFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            clappingHands.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            coldFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            confettiFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            crying.setColorFilter(getApplicationContext().getResources().getColor(R.color.transparent));
            explodingFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            openMouth.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            loudlyCrying.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            partyFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            seeNoEvil.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            thumbsUp.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            triumphImage.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            zanyFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
        });

        explodingFace.setOnClickListener(v -> {
            isSelected = true;
            player1ax = false;
            shape = "explodingFace";

            hotFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            beamingFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            clappingHands.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            coldFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            confettiFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            crying.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            explodingFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.transparent));
            openMouth.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            loudlyCrying.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            partyFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            seeNoEvil.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            thumbsUp.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            triumphImage.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            zanyFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
        });

        openMouth.setOnClickListener(v -> {
            isSelected = true;
            player1ax = false;
            shape = "openMouth";

            hotFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            beamingFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            clappingHands.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            coldFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            confettiFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            crying.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            explodingFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            openMouth.setColorFilter(getApplicationContext().getResources().getColor(R.color.transparent));
            loudlyCrying.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            partyFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            seeNoEvil.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            thumbsUp.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            triumphImage.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            zanyFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
        });

        loudlyCrying.setOnClickListener(v -> {
            isSelected = true;
            player1ax = false;
            shape = "loudlyCrying";

            hotFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            beamingFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            clappingHands.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            coldFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            confettiFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            crying.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            explodingFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            openMouth.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            loudlyCrying.setColorFilter(getApplicationContext().getResources().getColor(R.color.transparent));
            partyFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            seeNoEvil.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            thumbsUp.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            triumphImage.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            zanyFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
        });

        partyFace.setOnClickListener(v -> {
            isSelected = true;
            player1ax = false;
            shape = "partyFace";

            hotFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            beamingFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            clappingHands.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            coldFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            confettiFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            crying.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            explodingFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            openMouth.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            loudlyCrying.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            partyFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.transparent));
            seeNoEvil.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            thumbsUp.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            triumphImage.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            zanyFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
        });

        seeNoEvil.setOnClickListener(v -> {
            isSelected = true;
            player1ax = false;
            shape = "seeNoEvil";

            hotFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            beamingFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            clappingHands.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            coldFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            confettiFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            crying.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            explodingFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            openMouth.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            loudlyCrying.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            partyFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            seeNoEvil.setColorFilter(getApplicationContext().getResources().getColor(R.color.transparent));
            thumbsUp.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            triumphImage.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            zanyFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
        });

        thumbsUp.setOnClickListener(v -> {
            isSelected = true;
            player1ax = false;
            shape = "thumbsUp";

            hotFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            beamingFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            clappingHands.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            coldFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            confettiFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            crying.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            explodingFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            openMouth.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            loudlyCrying.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            partyFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            seeNoEvil.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            thumbsUp.setColorFilter(getApplicationContext().getResources().getColor(R.color.transparent));
            triumphImage.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            zanyFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
        });

        triumphImage.setOnClickListener(v -> {
            isSelected = true;
            player1ax = false;
            shape = "triumph";

            hotFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            beamingFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            clappingHands.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            coldFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            confettiFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            crying.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            explodingFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            openMouth.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            loudlyCrying.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            partyFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            seeNoEvil.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            thumbsUp.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            triumphImage.setColorFilter(getApplicationContext().getResources().getColor(R.color.transparent));
            zanyFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
        });

        zanyFace.setOnClickListener(v -> {
            isSelected = true;
            player1ax = false;
            shape = "zanyFace";

            hotFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            beamingFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            clappingHands.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            coldFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            confettiFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            crying.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            explodingFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            openMouth.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            loudlyCrying.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            partyFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            seeNoEvil.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            thumbsUp.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            triumphImage.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
            zanyFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.transparent));
        });

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (isSelected) {
                    continue_next.setOnClickListener(v -> {
                        CharSequence[] players1 = getIntent().getCharSequenceArrayExtra("playersnames");
                        player1 = players1[0];
                        player2 = players1[1];
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        if (!isFirstChoose) {
                            editor.putString("first", shape);
                            editor.apply();
                            clearAll(shape);
                            isFirstChoose = true;
                        } else {
                            editor.putString("second", shape);
                            editor.apply();
                            Intent i = new Intent(ChooseActivity.this, SceneActivity.class);
                            CharSequence[] players2 = {player1, player2};
                            i.putExtra("playersnames", players2);
                            i.putExtra("player1ax", player1ax);
                            i.putExtra("selectedsingleplayer", selectedsingleplayer);
                            startActivity(i);
                        }
                    });
                }
            }
        }, 0, 20);
    }

    @SuppressLint("SetTextI18n")
    private void clearAll(String shape) {
        timer.cancel();
        this.shape = "";

        if (!selectedsingleplayer) {
            label.setText(player2 + " pick your side");
        } else {
            label.setText("Pick Computer side");
        }

        hotFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.transparent));
        beamingFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
        clappingHands.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
        coldFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
        confettiFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
        crying.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
        explodingFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
        openMouth.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
        loudlyCrying.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
        partyFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
        seeNoEvil.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
        thumbsUp.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
        triumphImage.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
        zanyFace.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));

        switch (shape) {
            case "hotFace":
                hotFace.setEnabled(false);
                break;
            case "beamingFace":
                beamingFace.setEnabled(false);
                break;
            case "clappingHands":
                clappingHands.setEnabled(false);
                break;
            case "coldFace":
                coldFace.setEnabled(false);
                break;
            case "confettiFace":
                confettiFace.setEnabled(false);
                break;
            case "cryingFace":
                crying.setEnabled(false);
                break;
            case "explodingFace":
                explodingFace.setEnabled(false);
                break;
            case "openMouth":
                openMouth.setEnabled(false);
                break;
            case "loudlyCrying":
                loudlyCrying.setEnabled(false);
                break;
            case "partyFace":
                partyFace.setEnabled(false);
                break;
            case "seeNoEvil":
                seeNoEvil.setEnabled(false);
                break;
            case "thumbsUp":
                thumbsUp.setEnabled(false);
                break;
            case "triumph":
                triumphImage.setEnabled(false);
                break;
            case "zanyFace":
                zanyFace.setEnabled(false);
                break;
        }

    }

    private void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(ChooseActivity.this, JoyManager.INTERSTITIAL, adRequest, new InterstitialAdLoadCallback() {
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
    public void onBackPressed() {
        if (interstitialAd != null) {
            interstitialAd.show(ChooseActivity.this);
        } else {
            Intent intent = new Intent(ChooseActivity.this, NameActivity.class);
            startActivity(intent);
        }
    }
}
