package com.example.projetpochet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combat);

        playerPower = getIntent().getIntExtra("playerPower", 100);
        playerHealth = getIntent().getIntExtra("playerHealth", 10);
        enemyPower = new Random().nextInt(50) + 50;

        playerPowerTextView = findViewById(R.id.playerPower);
        playerHealthTextView = findViewById(R.id.playerHealth);
        enemyPowerTextView = findViewById(R.id.enemyPower);

        updateUI();

        Button attackButton = findViewById(R.id.attackButton);
        Button fleeButton = findViewById(R.id.fleeButton);

        attackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performAttack();
            }
        });

        fleeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performFlee();
            }
        });
    }

    private void performAttack() {
        if (playerPower >= enemyPower) {
            playerPower += 10;
        } else {
            playerHealth -= 5;
        }
        endCombat();
    }

    private void performFlee() {
        playerHealth -= 5;
        endCombat();
    }

    private void endCombat() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("playerPower", playerPower);
        resultIntent.putExtra("playerHealth", playerHealth);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void updateUI() {
        playerPowerTextView.setText(String.valueOf(playerPower));
        playerHealthTextView.setText(String.valueOf(playerHealth));
        enemyPowerTextView.setText(String.valueOf(enemyPower));
    }
}

