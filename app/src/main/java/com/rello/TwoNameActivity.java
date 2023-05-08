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

/**
 * Developed by > Harsh Kumar Singh (snbApps)
 * UI/UX Designer > Harsh Kumar Singh (snbApps)
 * Date > 11/04/2021
 */

public class TwoNameActivity extends AppCompatActivity {

    public CharSequence player1 = "Player 1";
    public CharSequence player2 = "Player 2";
    public boolean selectedsingleplayer = false;
    private EditText first;
    private EditText second;
    private RelativeLayout layout;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_name);

        FirebaseAnalytics analytics = FirebaseAnalytics.getInstance(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        layout = (RelativeLayout) findViewById(R.id.parent_two);
        Button ds = (Button) findViewById(R.id.button2_two);
        second = (EditText) findViewById(R.id.second_player_two);
        first = (EditText) findViewById(R.id.first_player_two);

        layout.setOnTouchListener(new OnSwipeTouchListener(TwoNameActivity.this) {
            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                finish();
            }

            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                Intent i = new Intent(TwoNameActivity.this, ChooseActivity.class);
                i.putExtra("selectedsingleplayer", selectedsingleplayer);
                CharSequence[] players = {player1, player2};
                i.putExtra("playersnames", players);
                startActivity(i);
            }
        });

        first.addTextChangedListener(new TextWatcher() {
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

        second.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                player2 = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ds.setOnClickListener(v -> {
            Intent i = new Intent(TwoNameActivity.this, ChooseActivity.class);
            i.putExtra("selectedsingleplayer", selectedsingleplayer);
            CharSequence[] players = {player1, player2};
            i.putExtra("playersnames", players);
            startActivity(i);
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(TwoNameActivity.this, MainActivity.class);
        startActivity(intent);
    }
}