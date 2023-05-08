package com.rello;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Developed by > Harsh Kumar Singh (snbApps)
 * UI/UX Designer > Harsh Kumar Singh (snbApps)
 * Date > 11/04/2021
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        FirebaseAnalytics analytics = FirebaseAnalytics.getInstance(this);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        TextView textView = findViewById(R.id.textView3);
        TextView textView2 = findViewById(R.id.textView2);
            textView.setAlpha(0f);
            textView2.setAlpha(0f);

        textView.animate()
                .translationY(textView.getHeight())
                .alpha(1f)
                .setStartDelay(1000)
                .setDuration(1200);

        textView2.animate()
                .translationY(textView.getHeight())
                .alpha(1f)
                .setStartDelay(1500)
                .setDuration(1000);

        ImageView imageView = findViewById(R.id.imageView3);
        imageView.setAlpha(0f);
        imageView.animate()
                .translationY(textView.getHeight())
                .alpha(1f)
                .setDuration(800);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            finish();
        }, 4000);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel("tikoo", "tikoo", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

    }
}
