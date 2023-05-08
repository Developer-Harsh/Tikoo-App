package com.rello;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.snbapps.rello.R;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Developed by > Harsh Kumar Singh (snbApps)
 * UI/UX Designer > Harsh Kumar Singh (snbApps)
 * Date > 11/04/2021
 */

public class NameActivity extends AppCompatActivity {

    public CharSequence player1 = "1";
    public CharSequence player2 = "2";
    private int length;
    public boolean selectedsingleplayer = true;
    private EditText nameAi;
    private RelativeLayout layout;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        Button ds = findViewById(R.id.button2);

        FirebaseAnalytics analytics = FirebaseAnalytics.getInstance(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        layout = (RelativeLayout) findViewById(R.id.layout_name);

        layout.setOnTouchListener(new OnSwipeTouchListener(NameActivity.this) {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                finish();
            }

            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                Intent i = new Intent(NameActivity.this, ChooseActivity.class);
                CharSequence[] players = {player1, player2};
                i.putExtra("playersnames", players);
                i.putExtra("selectedsingleplayer", selectedsingleplayer);
                startActivity(i);
            }
        });

        nameAi = (EditText) findViewById(R.id.player_name_ai);

        nameAi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                player1 = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                length = nameAi.getText().length();
            }
        }, 0, 2);

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (length > 1) {
                    ds.setOnClickListener(v -> {
                        Intent i = new Intent(NameActivity.this, ChooseActivity.class);
                        CharSequence[] players = {player1, player2};
                        i.putExtra("playersnames", players);
                        i.putExtra("selectedsingleplayer", selectedsingleplayer);
                        startActivity(i);
                    });
                }
            }
        }, 0, 20);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(NameActivity.this, MainActivity.class);
        startActivity(intent);
    }
}