package com.example.projetpochet;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Score implements Comparable<Score> {
    public String playerName;
    public String difficulty;
    public int power;
    public Date date;

    public Score(String playerName, String difficulty, int power, Date date) {
        this.playerName = playerName;
        this.difficulty = difficulty;
        this.power = power;
        this.date = date;
    }

    @Override
    public int compareTo(Score other) {
        return other.power - this.power; // Sort in descending order of power
    }

    // Method to convert Score to a string representation
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return playerName + "," + difficulty + "," + power + "," + dateFormat.format(date);
    }

    // Method to create a Score object from a string representation
    public static Score fromString(String scoreString) {
        String[] parts = scoreString.split(",");
        if (parts.length == 4) {
            try {
                String playerName = parts[0];
                String difficulty = parts[1];
                int power = Integer.parseInt(parts[2]);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                Date date = dateFormat.parse(parts[3]);
                return new Score(playerName, difficulty, power, date);
            } catch (Exception e) {
                // Handle parsing errors
                e.printStackTrace();
            }
        }
        return null; // Or throw an exception
    }
}