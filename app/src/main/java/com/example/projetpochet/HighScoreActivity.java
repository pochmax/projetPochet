package com.example.projetpochet;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HighScoreActivity extends AppCompatActivity {

    private TextView highScoresTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        highScoresTextView = findViewById(R.id.highScoresTextView);

        // Load and display high scores
        displayHighScores();
    }

    private void displayHighScores() {
        Map<String, List<Score>> highScores = loadHighScores();

        StringBuilder scoresText = new StringBuilder();
        for (String difficulty : highScores.keySet()) {
            scoresText.append(difficulty).append(":\n");
            List<Score> scoresForDifficulty = highScores.get(difficulty);
            for (Score score : scoresForDifficulty) {
                scoresText.append(score.playerName).append(" - ").append(score.power).append(" - ").append(score.date).append("\n");
            }
            scoresText.append("\n");
        }

        highScoresTextView.setText(scoresText.toString());
    }

    private Map<String, List<Score>> loadHighScores() {
        Map<String, List<Score>> highScores = new HashMap<>();
        SharedPreferences preferences = getSharedPreferences("high_scores", MODE_PRIVATE);

        // Load scores for each difficulty
        for (String difficulty : new String[]{"Facile", "Moyen", "Difficile"}) {
            List<Score> scoresForDifficulty = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                String scoreString = preferences.getString(difficulty + "_" + i, null);
                if (scoreString != null) {
                    Score score = Score.fromString(scoreString);
                    if (score != null) {
                        scoresForDifficulty.add(score);
                    }
                }
            }
            Collections.sort(scoresForDifficulty); // Sort scores for this difficulty
            highScores.put(difficulty, scoresForDifficulty);
        }

        return highScores;
    }
}