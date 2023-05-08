package com.rello;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.snbapps.rello.R;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Developed by > Harsh Kumar Singh (snbApps)
 * UI/UX Designer > Harsh Kumar Singh (snbApps)
 * Date > 11/04/2021
 */

public class SettingsActivity extends AppCompatActivity {

    public static final String EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X";
    public static final String EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y";
    View rootLayout;
    private int revealX;
    private static final String PREFS_NAME = "vibration";
    private static final String PREF_VIBRATION = "TicVib";
    private int revealY;
    private boolean Vibration;
    private boolean isChecked;
    private String[] Randomfirst;
    private SharedPreferences sharedPreferences;
    private TextView dif;
    private RelativeLayout layout;
    private ReviewManager reviewManager;
    int RequestUpdate = 1;
    private FirebaseAnalytics analytics;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();
        analytics = FirebaseAnalytics.getInstance(this);
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Vibration = preferences.getBoolean(PREF_VIBRATION, true);
        Window w = getWindow();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        layout = (RelativeLayout) findViewById(R.id.rootlay);

        layout.setOnTouchListener(new OnSwipeTouchListener(SettingsActivity.this) {
            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                finish();
            }
        });

        final Intent intent = getIntent();

        Switch swit = findViewById(R.id.swith2);
        swit.setChecked(Vibration);
        swit.setOnClickListener(view -> {
            if (Vibration) {
                isChecked = false;
                SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                editor.putBoolean(PREF_VIBRATION, isChecked);
                editor.apply();
            } else {
                isChecked = true;
                SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                editor.putBoolean(PREF_VIBRATION, isChecked);
                editor.apply();
            }
        });

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        dif = findViewById(R.id.dif);
        dif.setText(getDiffLabel(sharedPreferences.getInt("key", 0)));

        RelativeLayout r3 = findViewById(R.id.r3);
        r3.setOnClickListener(view -> difficulty());

        RelativeLayout reviewAppl = findViewById(R.id.review_app_settings);
        reviewAppl.setOnClickListener(view -> reviewApp());

        RelativeLayout r7 = findViewById(R.id.r7);
        r7.setOnClickListener(view -> feedbacks());

        RelativeLayout shareApp = findViewById(R.id.share_app_settings);
        shareApp.setOnClickListener(view -> shareAppMethod());

        RelativeLayout update = findViewById(R.id.update_settings);
        update.setOnClickListener(view -> updateNow());

        ImageView back = findViewById(R.id.back_settings);
        back.setOnClickListener(view -> onBackPressed());
    }

    private void init() {
        reviewManager = ReviewManagerFactory.create(this);
    }

    private void updateNow() {
        Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(getString(R.string.app_link) + getApplicationContext().getPackageName())));
        }
    }

    private void shareAppMethod() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name)+",");
            String shareMessage= "\nHello friend,\nI recommend to use this Application "+getString(R.string.app_name)+" and Play.\n";
            shareMessage = shareMessage + getString(R.string.app_link) + getApplicationContext().getPackageName() +"\nDownload the App Now.";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent,"Share " + getString(R.string.app_name)));
        } catch(Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void difficulty() {
        String[] choices = new String[] {" Easy", " Medium", " Hard", " Impossible", " Random"};

        int selected = -1;

        switch (sharedPreferences.getInt("key", 0)) {
            case 1:
                selected = 0;
                break;
            case 2:
                selected = 1;
                break;
            case 3:
                selected = 2;
                break;
            case 4:
                selected = 3;
                break;
            case 5:
                selected = 4;
                break;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Levels")
                .setPositiveButton("Done", (dialog, which) -> {
                    dif.setText(getDiffLabel(sharedPreferences.getInt("key", 0)));
                    dialog.dismiss();
                }).setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss()).setSingleChoiceItems(choices, selected, (dialog, which) -> {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    switch (which) {
                        case 0:
                            editor.putInt("key", 1);
                            break;
                        case 1:
                            editor.putInt("key", 2);
                            break;
                        case 2:
                            editor.putInt("key", 3);
                            break;
                        case 3:
                            editor.putInt("key", 4);
                            break;
                        case 4:
                            editor.putInt("key", 5);
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + which);
                    }
                    editor.apply();
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void reviewApp() {
        Task<ReviewInfo> request = reviewManager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ReviewInfo reviewInfo = task.getResult();
                Task<Void> flow = reviewManager.launchReviewFlow(SettingsActivity.this, reviewInfo);
                flow.addOnCompleteListener(task1 -> Toast.makeText(SettingsActivity.this, "Thanks for your sweet review.", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void feedbacks() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        String[] recipients = {getString(R.string.feedback)};
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name)+", feedback");
        intent.putExtra(Intent.EXTRA_CC, getString(R.string.feedback));
        intent.putExtra(Intent.EXTRA_TEXT, "Hello, \nSir i am user of "+getString(R.string.app_name)+" \nand i am facing an issue\n\n");
        intent.setType("text/html");
        intent.setPackage("com.google.android.gm");
        startActivity(Intent.createChooser(intent, "Send mail"));
    }

    private String getDiffLabel(int level) {
        String label = "";
        switch (level) {
            case 1:
                label = "Easy";
                break;
            case 2:
                label = "Medium";
                break;
            case 3:
                label = "Hard";
                break;
            case 4:
                label = "Impossible";
                break;
            case 5:
                label = "Random";
                break;
        }
        return label;
    }
}
