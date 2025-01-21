package com.example.projetpochet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class CombatActivity extends AppCompatActivity {

    private int playerPower;
    private int playerHealth;
    private int enemyPower;

    private TextView playerPowerTextView;
    private TextView playerHealthTextView;
    private TextView enemyPowerTextView;

    private String resultTextView;

    private int[] monsterImages = {
        R.drawable.cat1,
        R.drawable.cat2,
        R.drawable.cat3,
        R.drawable.cat4,
        R.drawable.cat5,
        R.drawable.bossfinal
    };

    private String[] monsterNames ={
        "Boss des lamentations",
        "Boss de la puissance",
        "Boss de l'intelligence",
        "Boss de la mignonnerie",
            "Boss de la grimace",
            "Boss final"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combat);

        // Charger les données du combat à partir de l'intent
        playerPower = getIntent().getIntExtra("playerPower", 100);
        playerHealth = getIntent().getIntExtra("playerHealth", 10);
        enemyPower = getIntent().getIntExtra("enemyPower", 1);
        if (resultTextView == null) {
            resultTextView = "En attente";
        }

        // Initialize UI elements
        playerPowerTextView = findViewById(R.id.playerPower);
        playerHealthTextView = findViewById(R.id.playerHealth);
        enemyPowerTextView = findViewById(R.id.enemyPower);

        // Set initial values
        Random random = new Random();
        int randomIndex = random.nextInt(monsterImages.length);
        int monsterImageRes = monsterImages[randomIndex];
        String monsterName = monsterNames[randomIndex];

        ImageView monsterImageView = findViewById(R.id.monsterImage);
        TextView monsterNameTextView = findViewById(R.id.monsterName);

        monsterImageView.setImageResource(monsterImageRes);
        monsterNameTextView.setText(monsterName);

        updateUI();

        Button attackButton = findViewById(R.id.attackButton);
        Button fleeButton = findViewById(R.id.fleeButton);

        attackButton.setOnClickListener(v -> performAttack());
        fleeButton.setOnClickListener(v -> performFlee());
    }

    private void performAttack() {
        // Gerer les facteurs aléatoires
        Random random = new Random();
        double playerFactor = random.nextDouble();
        double enemyFactor = random.nextDouble();
        // Calculer le résultat du combat
        double result = playerPower * playerFactor - enemyPower * enemyFactor;

        if (result >= 0) {
            // Victoire
            playerPower += 10;
            resultTextView = "Victoire!";
            endCombat(true, false);
        } else {
            // Defaite
            playerHealth -= 3;
            if (playerHealth <= 0) {
                resultTextView = "Vous avez perdu la partie";
            } else {
                resultTextView = "Défaite...";
            }
            endCombat(false, true);
        }
    }

    private void performFlee() {
        // Gerer les facteurs aléatoires
        playerHealth -= 1;

        if (playerHealth <= 0) {
            resultTextView = "Vous avez perdu la partie";
        } else {
            resultTextView = "Vous avez fui !!!";
        }
        endCombat(false, true);
    }

    private void endCombat(boolean enemyDefeated, boolean roomIncomplete) {
        // Retourner les données du combat
        Intent resultIntent = new Intent();
        resultIntent.putExtra("playerPower", playerPower);
        resultIntent.putExtra("playerHealth", playerHealth);
        resultIntent.putExtra("enemyDefeated", enemyDefeated);
        resultIntent.putExtra("result", resultTextView);
        resultIntent.putExtra("roomIncomplete", roomIncomplete);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void updateUI() {
        // Mettre à jour les vues
        playerPowerTextView.setText(String.valueOf(playerPower));
        playerHealthTextView.setText(String.valueOf(playerHealth));
        enemyPowerTextView.setText(String.valueOf(enemyPower));
    }
}


