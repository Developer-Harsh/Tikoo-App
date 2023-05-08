package com.rello;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Developed by > Harsh Kumar Singh (snbApps)
 * UI/UX Designer > Harsh Kumar Singh (snbApps)
 * Date > 11/04/2021
 */

public class OnlineNameActivity extends AppCompatActivity {

    private EditText nameAi;
    private RelativeLayout layout;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        Button online = findViewById(R.id.button2);

        FirebaseAnalytics analytics = FirebaseAnalytics.getInstance(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        layout = (RelativeLayout) findViewById(R.id.layout_name);

        layout.setOnTouchListener(new OnSwipeTouchListener(OnlineNameActivity.this) {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                finish();
            }

            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                Intent i = new Intent(OnlineNameActivity.this, OnlineSceneActivity.class);
                i.putExtra("name", nameAi.getText().toString());
                startActivity(i);
            }
        });

        nameAi = (EditText) findViewById(R.id.player_name_ai);

        online.setOnClickListener(v -> {
            MyConstents.playerName = nameAi.getText().toString();

            if(MyConstents.playerName.isEmpty()){
                Toast.makeText(OnlineNameActivity.this, "Please enter player name", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(OnlineNameActivity.this, OnlineSceneActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(OnlineNameActivity.this, MainActivity.class);
        startActivity(intent);
    }
}