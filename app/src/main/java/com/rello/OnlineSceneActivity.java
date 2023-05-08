package com.rello;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import pl.droidsonroids.gif.GifImageView;

public class OnlineSceneActivity extends AppCompatActivity {

    private GifImageView img1, img2, img3, img4, img5, img6, img7, img8, img9;
    private String name;
    private ImageButton close;
    private TextView myName, otherName;
    private LinearLayout pl1, pl2;
    private final List<String> doneBoxes = new ArrayList<>();
    private final List<int[]> combinationsList = new ArrayList<>();
    private String playerUniqueId = "0";
    DatabaseReference databaseReference;
    private boolean opponentFound = false;
    private String opponentUniqueId = "0";
    private String status = "matching";
    private String playerTurn = "";
    private String connectionId = "";
    private String connectionUniqueId;
    private AlertDialog mProgressDialog;
    private AlertDialog score;
    ValueEventListener turnsEventListener, wonEventListener;
    private final String[] boxesSelectedBy = {"", "", "", "", "", "", "", "", ""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_scene);

        name = getIntent().getStringExtra("name");
        databaseReference = FirebaseDatabase.getInstance().getReference();

        myName = findViewById(R.id.NameText_online);
        pl1 = findViewById(R.id.player1Layout);
        close = findViewById(R.id.closeOnlineMatch);
        pl2 = findViewById(R.id.player2Layout);
        otherName = findViewById(R.id.NameText2_online);
        img1 = findViewById(R.id.tzz_online);
        img2 = findViewById(R.id.tzo_online);
        img3 = findViewById(R.id.tzt_online);
        img4 = findViewById(R.id.toz_online);
        img5 = findViewById(R.id.too_online);
        img6 = findViewById(R.id.tot_online);
        img7 = findViewById(R.id.ttz_online);
        img8 = findViewById(R.id.tto_online);
        img9 = findViewById(R.id.ttt_online);

        combinationsList.add(new int[]{0, 1, 2});
        combinationsList.add(new int[]{3, 4, 5});
        combinationsList.add(new int[]{6, 7, 8});
        combinationsList.add(new int[]{0, 3, 6});
        combinationsList.add(new int[]{1, 4, 7});
        combinationsList.add(new int[]{2, 5, 8});
        combinationsList.add(new int[]{2, 4, 6});
        combinationsList.add(new int[]{0, 4, 8});

        showProgressDialog();

        if (MemoryData.getData("player_id", "", this).isEmpty()) {
            playerUniqueId = String.valueOf(System.currentTimeMillis());
            MemoryData.saveData("player_id", playerUniqueId, this);
        } else {
            playerUniqueId = MemoryData.getData("player_id", "", this);
        }

        myName.setText(MyConstents.playerName);

        databaseReference.child("connections").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!opponentFound) {
                    if (snapshot.hasChildren()) {
                        for (DataSnapshot connections : snapshot.getChildren()) {
                            String conId = connections.getKey();
                            int getPlayersCount = (int) connections.getChildrenCount();
                            if (status.equals("waiting")) {
                                if (getPlayersCount == 2) {
                                    playerTurn = playerUniqueId;
                                    applyPlayerTurn(playerTurn);
                                    boolean playerFound = false;
                                    for (DataSnapshot players : connections.getChildren()) {
                                        String getPlayerUniqueId = players.getKey();
                                        if (getPlayerUniqueId != null) {
                                            if (getPlayerUniqueId.equals(playerUniqueId)) {
                                                playerFound = true;
                                            } else if (playerFound) {
                                                String getOpponentPlayerName = players.child("player_name").getValue(String.class);
                                                opponentUniqueId = players.getKey();
                                                otherName.setText(getOpponentPlayerName);
                                                connectionId = conId;
                                                opponentFound = true;
                                                databaseReference.child("turns").child(connectionId).addValueEventListener(turnsEventListener);
                                                databaseReference.child("won").child(connectionId).addValueEventListener(wonEventListener);
                                                dismissProgressDialog();
                                                databaseReference.child("connections").removeEventListener(this);
                                            }
                                        }
                                    }
                                }
                            } else {
                                if (getPlayersCount == 1) {
                                    boolean samePlayer = false;
                                    for (DataSnapshot players : connections.getChildren()) {
                                        if (players.getKey().equals(playerUniqueId)) {
                                            samePlayer = true;
                                            break;
                                        }
                                    }

                                    if (!samePlayer) {
                                        connections.child(playerUniqueId).child("player_name").getRef().setValue(MyConstents.playerName);

                                        for (DataSnapshot players : connections.getChildren()) {
                                            String getOpponentName = players.child("player_name").getValue(String.class);
                                            opponentUniqueId = players.getKey();
                                            playerTurn = opponentUniqueId;
                                            applyPlayerTurn(playerTurn);
                                            otherName.setText(getOpponentName);
                                            connectionId = conId;
                                            opponentFound = true;
                                            databaseReference.child("turns").child(connectionId).addValueEventListener(turnsEventListener);
                                            databaseReference.child("won").child(connectionId).addValueEventListener(wonEventListener);
                                            dismissProgressDialog();
                                            databaseReference.child("connections").removeEventListener(this);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (!opponentFound && !status.equals("waiting")) {
                        connectionUniqueId = String.valueOf(System.currentTimeMillis());
                        snapshot.child(connectionUniqueId).child(playerUniqueId).child("player_name").getRef().setValue(MyConstents.playerName);
                        status = "waiting";
                    }
                } else {
                    connectionUniqueId = String.valueOf(System.currentTimeMillis());
                    snapshot.child(connectionUniqueId).child(playerUniqueId).child("player_name").getRef().setValue(MyConstents.playerName);
                    status = "waiting";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        turnsEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.getChildrenCount() == 2) {
                        final int getBoxPosition = Integer.parseInt(Objects.requireNonNull(dataSnapshot.child("box_position").getValue(String.class)));
                        final String getPlayerId = dataSnapshot.child("player_id").getValue(String.class);
                        if (doneBoxes.contains(String.valueOf(getBoxPosition))) {
                            doneBoxes.add(String.valueOf(getBoxPosition));
                            if (getPlayerId != null) {
                                if (getBoxPosition == 1) {
                                    selectBox(img1, getBoxPosition, getPlayerId);
                                } else if (getBoxPosition == 2) {
                                    selectBox(img2, getBoxPosition, getPlayerId);
                                } else if (getBoxPosition == 3) {
                                    selectBox(img3, getBoxPosition, getPlayerId);
                                } else if (getBoxPosition == 4) {
                                    selectBox(img4, getBoxPosition, getPlayerId);
                                } else if (getBoxPosition == 5) {
                                    selectBox(img5, getBoxPosition, getPlayerId);
                                } else if (getBoxPosition == 6) {
                                    selectBox(img6, getBoxPosition, getPlayerId);
                                } else if (getBoxPosition == 7) {
                                    selectBox(img7, getBoxPosition, getPlayerId);
                                } else if (getBoxPosition == 8) {
                                    selectBox(img8, getBoxPosition, getPlayerId);
                                } else if (getBoxPosition == 9) {
                                    selectBox(img9, getBoxPosition, getPlayerId);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        wonEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("player_id")) {
                    String getWinPlayerId = snapshot.child("player_id").getValue(String.class);

                    if (getWinPlayerId != null) {
                        if (getWinPlayerId.equals(playerUniqueId)) {
                            showOnlineScoreDialog(name + " Won!" + "\n🎉🎉🎆🎆🎉🎉");
                        } else {
                            showOnlineScoreDialog(otherName.getText().toString() + "Won!");
                        }
                    }
                    databaseReference.child("turns").child(connectionId).removeEventListener(turnsEventListener);
                    databaseReference.child("won").child(connectionId).removeEventListener(wonEventListener);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        close.setOnClickListener(v -> finish());

        img1.setOnClickListener(v -> {
            if (!doneBoxes.contains("1") && playerTurn.equals(playerUniqueId)) {
                ((GifImageView) v).setImageResource(R.drawable.party_face);
                databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("box_position").setValue("1");
                databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("player_id").setValue(playerUniqueId);
                playerTurn = opponentUniqueId;
            }
        });

        img2.setOnClickListener(v -> {
            if (!doneBoxes.contains("2") && playerTurn.equals(playerUniqueId)) {
                ((GifImageView) v).setImageResource(R.drawable.party_face);
                databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("box_position").setValue("2");
                databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("player_id").setValue(playerUniqueId);
                playerTurn = opponentUniqueId;
            }
        });

        img3.setOnClickListener(v -> {
            if (!doneBoxes.contains("3") && playerTurn.equals(playerUniqueId)) {
                ((GifImageView) v).setImageResource(R.drawable.party_face);
                databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("box_position").setValue("3");
                databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("player_id").setValue(playerUniqueId);
                playerTurn = opponentUniqueId;
            }
        });

        img4.setOnClickListener(v -> {
            if (!doneBoxes.contains("4") && playerTurn.equals(playerUniqueId)) {
                ((GifImageView) v).setImageResource(R.drawable.party_face);
                databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("box_position").setValue("4");
                databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("player_id").setValue(playerUniqueId);
                playerTurn = opponentUniqueId;
            }
        });

        img5.setOnClickListener(v -> {
            if (!doneBoxes.contains("5") && playerTurn.equals(playerUniqueId)) {
                ((GifImageView) v).setImageResource(R.drawable.party_face);
                databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("box_position").setValue("5");
                databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("player_id").setValue(playerUniqueId);
                playerTurn = opponentUniqueId;
            }
        });

        img6.setOnClickListener(v -> {
            if (!doneBoxes.contains("6") && playerTurn.equals(playerUniqueId)) {
                ((GifImageView) v).setImageResource(R.drawable.party_face);
                databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("box_position").setValue("6");
                databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("player_id").setValue(playerUniqueId);
                playerTurn = opponentUniqueId;
            }
        });

        img7.setOnClickListener(v -> {
            if (!doneBoxes.contains("7") && playerTurn.equals(playerUniqueId)) {
                ((GifImageView) v).setImageResource(R.drawable.party_face);
                databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("box_position").setValue("7");
                databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("player_id").setValue(playerUniqueId);
                playerTurn = opponentUniqueId;
            }
        });

        img8.setOnClickListener(v -> {
            if (!doneBoxes.contains("8") && playerTurn.equals(playerUniqueId)) {
                ((GifImageView) v).setImageResource(R.drawable.party_face);
                databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("box_position").setValue("8");
                databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("player_id").setValue(playerUniqueId);
                playerTurn = opponentUniqueId;
            }
        });

        img9.setOnClickListener(v -> {
            if (!doneBoxes.contains("9") && playerTurn.equals(playerUniqueId)) {
                ((GifImageView) v).setImageResource(R.drawable.party_face);
                databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("box_position").setValue("9");
                databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("player_id").setValue(playerUniqueId);
                playerTurn = opponentUniqueId;
            }
        });
    }

    private void applyPlayerTurn(String playerUniqueId2) {
        if (playerUniqueId2.equals(playerUniqueId)) {
            pl1.setBackgroundResource(R.drawable.button_none_stroked);
            pl2.setBackgroundResource(android.R.color.transparent);
        } else {
            pl2.setBackgroundResource(R.drawable.button_none_stroked);
            pl1.setBackgroundResource(android.R.color.transparent);
        }
    }

    private void selectBox(GifImageView imageView, int selectedBoxPosition, String selectedByPlayer) {
        boxesSelectedBy[selectedBoxPosition - 1] = selectedByPlayer;

        if (selectedByPlayer.equals(playerUniqueId)) {
            imageView.setImageResource(R.drawable.clapping_hands);
            playerTurn = opponentUniqueId;
        } else {
            imageView.setImageResource(R.drawable.see_no_evil_monkey);
            playerTurn = playerUniqueId;
        }

        applyPlayerTurn(playerTurn);

        if (checkPlayerWin(selectedByPlayer)) {
            databaseReference.child("won").child(connectionId).child("player_id").setValue(selectedByPlayer);
        }

        if (doneBoxes.size() == 9) {
            showOnlineScoreDialog("Match Draw!");
        }
    }

    private boolean checkPlayerWin(String playerId) {
        boolean isPlayerWon = false;

        for (int i = 0; i < combinationsList.size(); i++) {
            final int[] combination = combinationsList.get(i);
            if (boxesSelectedBy[combination[0]].equals(playerId) &&
                    boxesSelectedBy[combination[1]].equals(playerId) &&
                    boxesSelectedBy[combination[2]].equals(playerId)) {
                isPlayerWon = true;
            }
        }

        return isPlayerWon;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (status.equals("waiting")) {
            databaseReference.child("connections").child(connectionId).removeValue();
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(OnlineSceneActivity.this);
            View view = LayoutInflater.from(this).inflate(R.layout.progress_dialog_layout, findViewById(R.id.prog_layout));
            builder.setView(view).setCancelable(false);
            mProgressDialog = builder.create();
            if (mProgressDialog.getWindow() != null) {
                mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
        }
        mProgressDialog.show();
    }

    private void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    private void showOnlineScoreDialog(String title) {
        if (score == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(OnlineSceneActivity.this);
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_layout_online, findViewById(R.id.score_online_layout));
            builder.setView(view).setCancelable(false);
            TextView titleTxt = findViewById(R.id.player_score_online);
            TextView timer = findViewById(R.id.timer_online_score);
            timerShow(timer);
            titleTxt.setText(title);
            Button closeOnlineScore = findViewById(R.id.close_button_online);
            closeOnlineScore.setText("Close");
            closeOnlineScore.setOnClickListener(v -> {
                finish();
                dismissOnlineScoreDialog();
            });
            score = builder.create();
            if (score.getWindow() != null) {
                score.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
        }
        score.show();
    }

    private void timerShow(TextView view) {
        long duration = TimeUnit.SECONDS.toMillis(5);
        new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long l) {
                String sDuration = String.format(Locale.ENGLISH, "%02d : %02d",
                        TimeUnit.MILLISECONDS.toMinutes(l),
                        TimeUnit.MILLISECONDS.toSeconds(l) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l))
                );
                view.setText(sDuration);
            }

            @Override
            public void onFinish() {
                view.setText("0");
                finish();
                dismissOnlineScoreDialog();
            }
        };
    }

    private void dismissOnlineScoreDialog() {
        if (score != null) {
            score.dismiss();
        }
    }
}