package com.example.projetpochet;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private int unexploredRooms = 16;
    private int playerPower = 100;
    private int playerHealth = 10;

    private String result = "En attente...";

    private TextView unexploredRoomsTextView;
    private TextView playerPowerTextView;
    private TextView playerHealthTextView;

    private TextView resultTextView;

    private ImageButton[] roomButtons = new ImageButton[16];
    private int[] roomEnemies = new int[16];
    private boolean[] roomStatus = new boolean[16];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        unexploredRoomsTextView = findViewById(R.id.unexploredRooms);
        playerPowerTextView = findViewById(R.id.playerPower);
        playerHealthTextView = findViewById(R.id.playerHealth);
        resultTextView = findViewById(R.id.result);

        Random random = new Random();

        // Initialize rooms with random enemy power and unexplored status
        for (int i = 0; i < 16; i++) {
            roomEnemies[i] = random.nextInt(150) + 1;
            roomStatus[i] = false; // Room is unexplored

            int buttonId = getResources().getIdentifier("room" + (i + 1), "id", getPackageName());
            roomButtons[i] = findViewById(buttonId);
            roomButtons[i].setImageResource(R.drawable.room_unexplored);
            int sizeInDp = 90;
            float scale = getResources().getDisplayMetrics().density;
            int sizeInPx = (int) (sizeInDp * scale + 0.5f);

            roomButtons[i].getLayoutParams().width = sizeInPx;
            roomButtons[i].getLayoutParams().height = sizeInPx;


            roomButtons[i].requestLayout();

            final int roomIndex = i;
            roomButtons[i].setOnClickListener(v -> enterRoom(roomIndex));
        }

        updateUI();
    }

    private void enterRoom(int roomIndex) {
        if (roomStatus[roomIndex]) {
            resultTextView.setText("Cette pièce est vide");
            return;
        }

        Intent intent = new Intent(this, CombatActivity.class);
        intent.putExtra("playerPower", playerPower);
        intent.putExtra("playerHealth", playerHealth);
        intent.putExtra("enemyPower", roomEnemies[roomIndex]);
        startActivityForResult(intent, roomIndex);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            playerPower = data.getIntExtra("playerPower", playerPower);
            playerHealth = data.getIntExtra("playerHealth", playerHealth);
            result = data.getStringExtra("result");

            if (playerHealth <= 0) {
                showGameOverMessage();
                return;
            }

            boolean enemyDefeated = data.getBooleanExtra("enemyDefeated", false);
            boolean roomIncomplete = data.getBooleanExtra("roomIncomplete", false);

            if (enemyDefeated) {
                unexploredRooms--;
                roomStatus[requestCode] = true;
                roomButtons[requestCode].setImageResource(R.drawable.room_completed);
                roomButtons[requestCode].setEnabled(false);
            } else if (roomIncomplete) {
                roomButtons[requestCode].setImageResource(R.drawable.room_incompleted);
            }

            if (unexploredRooms == 0) {
                showVictoryMessage();
                return;
            }

            updateUI();
        }
    }

    private void showGameOverMessage() {
        resultTextView.setText("Vous avez perdu la partie.");
        disableGame();
    }

    private void showVictoryMessage() {
        resultTextView.setText("Bravo! Vous avez gagné la partie.");
        disableGame();
    }

    private void disableGame() {
        for (ImageButton roomButton : roomButtons) {
            roomButton.setEnabled(false);
        }
    }

    private void updateUI() {
        unexploredRoomsTextView.setText(String.valueOf(unexploredRooms));
        playerPowerTextView.setText(String.valueOf(playerPower));
        playerHealthTextView.setText(String.valueOf(playerHealth));
        resultTextView.setText(result);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.restart) {
            restartGame();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void restartGame() {
        playerPower = 100;
        playerHealth = 10;
        unexploredRooms = 16;
        Arrays.fill(roomStatus, false);
        result = "En attente...";
        Random random = new Random();
        for (int i = 0; i < 16; i++) {
            roomEnemies[i] = random.nextInt(150) + 1;
            roomButtons[i].setImageResource(R.drawable.room_unexplored);
            roomButtons[i].setEnabled(true);
        }

        updateUI();
    }
}
