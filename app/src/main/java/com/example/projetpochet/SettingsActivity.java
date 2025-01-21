package com.example.projetpochet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        EditText initialPowerEditText = findViewById(R.id.initialPlayerPower);
        EditText initialHealthEditText = findViewById(R.id.initialPlayerHealth);
        EditText maxEnemyPowerEditText = findViewById(R.id.maxEnemyPower);
        Spinner difficultySpinner = findViewById(R.id.difficultySpinner);

        // Charger les valeurs actuelles
        SharedPreferences preferences = getSharedPreferences("GameSettings", MODE_PRIVATE);
        initialPowerEditText.setText(String.valueOf(preferences.getInt("initialPower", 100)));
        initialHealthEditText.setText(String.valueOf(preferences.getInt("initialHealth", 10)));
        maxEnemyPowerEditText.setText(String.valueOf(preferences.getInt("maxEnemyPower", 150)));
        int difficultyPosition = preferences.getInt("difficultyPosition", 1); // 1 = Moyen
        difficultySpinner.setSelection(difficultyPosition);

        // Bouton pour sauvegarder les paramètres
        Button saveButton = findViewById(R.id.saveSettingsButton);
        saveButton.setOnClickListener(v -> {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("initialPower", Integer.parseInt(initialPowerEditText.getText().toString()));
            editor.putInt("initialHealth", Integer.parseInt(initialHealthEditText.getText().toString()));
            editor.putInt("maxEnemyPower", Integer.parseInt(maxEnemyPowerEditText.getText().toString()));
            editor.putInt("difficultyPosition", difficultySpinner.getSelectedItemPosition());
            editor.putString("difficulty", difficultySpinner.getSelectedItem().toString());
            editor.apply();

            Toast.makeText(this, "Paramètres enregistrés", Toast.LENGTH_SHORT).show();
            // Redémarrer l'activité pour appliquer les modifications
            Intent restartIntent = new Intent(this, MainActivity.class);
            restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(restartIntent);
            finish();
        });
    }
}
