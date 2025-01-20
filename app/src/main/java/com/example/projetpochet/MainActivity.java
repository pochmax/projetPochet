package com.example.projetpochet;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private int unexploredRooms = 16;
    private int playerPower = 100;
    private int playerHealth = 10;

    private TextView unexploredRoomsTextView;
    private TextView playerPowerTextView;
    private TextView playerHealthTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        unexploredRoomsTextView = findViewById(R.id.unexploredRooms);
        playerPowerTextView = findViewById(R.id.playerPower);
        playerHealthTextView = findViewById(R.id.playerHealth);

        updateUI();

        for (int i = 1; i <= 16; i++) {
            int buttonId = getResources().getIdentifier("room" + i, "id", getPackageName());
            Button roomButton = findViewById(buttonId);
            roomButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    enterRoom();
                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void enterRoom() {
        if (unexploredRooms > 0) {
            unexploredRooms--;
            Intent intent = new Intent(this, CombatActivity.class);
            intent.putExtra("playerPower", playerPower);
            intent.putExtra("playerHealth", playerHealth);
            startActivityForResult(intent, 1);
        }
        updateUI();
    }

    private void updateUI() {
        unexploredRoomsTextView.setText(String.valueOf(unexploredRooms));
        playerPowerTextView.setText(String.valueOf(playerPower));
        playerHealthTextView.setText(String.valueOf(playerHealth));
    }

    private void showMenu() {
        unexploredRooms = 16;
        playerPower = 100;
        playerHealth = 10;
        updateUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            playerPower = data.getIntExtra("playerPower", playerPower);
            playerHealth = data.getIntExtra("playerHealth", playerHealth);
            updateUI();
        }
    }
}