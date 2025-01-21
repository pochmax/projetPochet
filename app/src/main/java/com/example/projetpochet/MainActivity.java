package com.example.projetpochet;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private int unexploredRooms = 16;
    private int playerPower;
    private int playerHealth;
    private int maxEnemyPower;

    private String result = "En attente...";

    private TextView unexploredRoomsTextView;
    private TextView playerPowerTextView;
    private TextView playerHealthTextView;

    private TextView resultTextView;

    private ImageButton[] roomButtons = new ImageButton[16];
    private int[] roomEnemies = new int[16];
    private boolean[] roomStatus = new boolean[16]; //
    private int potionRoomIndex = -1; // Pièce contenant la potion
    private int charmRoomIndex = -1; // Pièce contenant le charme

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Charger les paramètres du jeu
        loadGameSettings();
        // Initialize UI elements
        unexploredRoomsTextView = findViewById(R.id.unexploredRooms);
        playerPowerTextView = findViewById(R.id.playerPower);
        playerHealthTextView = findViewById(R.id.playerHealth);
        resultTextView = findViewById(R.id.result);

        Random random = new Random();

        // Initialize rooms with random enemy power and unexplored status
        for (int i = 0; i < 16; i++) {
            roomEnemies[i] = random.nextInt(maxEnemyPower) + 1;
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
        // Générer les indices des pièces contenant la potion et le charme
        do {
            potionRoomIndex = random.nextInt(16);
            charmRoomIndex = random.nextInt(16);
        } while (potionRoomIndex == charmRoomIndex);

        updateUI();
    }

    private void enterRoom(int roomIndex) {
        // Vérifier si la pièce est vide
        if (roomStatus[roomIndex]) {
            resultTextView.setText("Cette pièce est vide");
            return;
        }
        // Mettre à jour le statut de la pièce
        if (roomIndex == potionRoomIndex) {
            int healthBonus = new Random().nextInt(3) + 1; // Entre 1 et 3
            playerHealth += healthBonus;
            showCustomToast("Vous avez trouvé une potion magique ! +" + healthBonus + " PV", R.drawable.potion);
        }
        // Mettre à jour le statut de la pièce
        if (roomIndex == charmRoomIndex) {
            int powerBonus = new Random().nextInt(6) + 5; // Entre 5 et 10
            playerPower += powerBonus;
            showCustomToast("Vous avez trouvé un charme de puissance ! +" + powerBonus + " Puissance", R.drawable.charm);
        }
        // Démarrer l'activité du combat
        Intent intent = new Intent(this, CombatActivity.class);
        intent.putExtra("playerPower", playerPower);
        intent.putExtra("playerHealth", playerHealth);
        intent.putExtra("enemyPower", roomEnemies[roomIndex]);

        startActivityForResult(intent, roomIndex);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Mettre à jour les statistiques du joueur
        if (resultCode == RESULT_OK && data != null) {
            playerPower = data.getIntExtra("playerPower", playerPower);
            playerHealth = data.getIntExtra("playerHealth", playerHealth);
            result = data.getStringExtra("result");
            // Vérifier si le joueur est mort
            if (playerHealth <= 0) {
                showGameOverMessage();
                return;
            }
            // Récupérer les données du combat
            boolean enemyDefeated = data.getBooleanExtra("enemyDefeated", false);
            boolean roomIncomplete = data.getBooleanExtra("roomIncomplete", false);
            // Mettre à jour le statut de la pièce
            if (enemyDefeated) {
                unexploredRooms--;
                roomStatus[requestCode] = true;
                roomButtons[requestCode].setImageResource(R.drawable.room_completed);
                roomButtons[requestCode].setEnabled(false);
            } else if (roomIncomplete) {
                roomButtons[requestCode].setImageResource(R.drawable.room_incompleted);
            }
            // Vérifier si le joueur a gagné
            if (unexploredRooms == 0) {
                showVictoryMessage();
                return;
            }

            updateUI();
        }
    }

    private void showGameOverMessage() {
        // Afficher un message de défaite
        resultTextView.setText("Vous avez perdu la partie.");
        disableGame();
    }

    private void showVictoryMessage() {
        // Afficher un message de victoire
        resultTextView.setText("Bravo! Vous avez gagné la partie.");
        disableGame();
    }

    private void disableGame() {
        // Désactiver les boutons des pièces
        for (ImageButton roomButton : roomButtons) {
            roomButton.setEnabled(false);
        }
    }

    private void updateUI() {
        // Mettre à jour les vues
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
        // Gérer les clics sur les items du menu
        if (item.getItemId() == R.id.restart) {
            restartGame();
            return true;
        }
        if (item.getItemId() == R.id.settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void restartGame() {
        // Recharger les paramètres du jeu
        loadGameSettings();
        unexploredRooms = 16;
        Arrays.fill(roomStatus, false);
        result = "En attente...";
        Random random = new Random();
        for (int i = 0; i < 16; i++) {
            roomEnemies[i] = random.nextInt(maxEnemyPower) + 1;
            roomButtons[i].setImageResource(R.drawable.room_unexplored);
            roomButtons[i].setEnabled(true);
        }

        updateUI();
    }


    private void showCustomToast(String message, int imageResourceId) {
        // Créer un Toast personnalisé
        View toastView = getLayoutInflater().inflate(R.layout.custom_toast, null);

        // Set the image and text in the custom layout
        ImageView toastImage = toastView.findViewById(R.id.toastImage);
        toastImage.setImageResource(imageResourceId);

        TextView toastText = toastView.findViewById(R.id.toastText);
        toastText.setText(message);

        // Créer et affiche le toast
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(toastView);
        toast.show();
    }

    private void loadGameSettings() {
        // Charger les paramètres du jeu à partir des SharedPreferences
        SharedPreferences preferences = getSharedPreferences("GameSettings", MODE_PRIVATE);
        playerPower = preferences.getInt("initialPower", 100);
        playerHealth = preferences.getInt("initialHealth", 10);
        maxEnemyPower = preferences.getInt("maxEnemyPower", 150);
        String difficulty = preferences.getString("difficulty", "Moyen");

        // Ajuster les ennemis en fonction de la difficulté
        Random random = new Random();
        for (int i = 0; i < 16; i++) {
            roomEnemies[i] = random.nextInt(maxEnemyPower) + 1;
        }

        // Ajustements en fonction de la difficulté
        switch (difficulty) {
            case "Facile":
                playerHealth += 5;
                playerPower += 10;
                break;
            case "Difficile":
                playerHealth -= 2;
                playerPower -= 5;
                break;
        }
    }
}
