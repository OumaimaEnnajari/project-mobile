package com.example.calculementalproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private String selectedDifficulty = null; // Niveau sélectionné
    private Button btnNext;
    private Button btnEasy, btnMedium, btnHard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialiser les boutons
        initButtons();

        // Configurer les listeners
        setupDifficultyButtons();
        setupNextButton();
    }

    private void initButtons() {
        btnEasy = findViewById(R.id.btnEasy);
        btnMedium = findViewById(R.id.btnMedium);
        btnHard = findViewById(R.id.btnHard);
        btnNext = findViewById(R.id.btnNext);
        btnNext.setEnabled(false); // Désactiver le bouton "Next" au début
    }

    private void setupDifficultyButtons() {
        btnEasy.setOnClickListener(v -> updateDifficulty("easy"));
        btnMedium.setOnClickListener(v -> updateDifficulty("medium"));
        btnHard.setOnClickListener(v -> updateDifficulty("hard"));
    }

    private void setupNextButton() {
        btnNext.setOnClickListener(v -> {
            if (selectedDifficulty != null) {
                navigateToCalculActivity();
            }
        });
    }

    private void updateDifficulty(String difficulty) {
        selectedDifficulty = difficulty;
        btnNext.setEnabled(true); // Activer le bouton "Next"

        // Réinitialiser les couleurs des boutons
        resetButtonColors();

        // Mettre en évidence le bouton sélectionné
        getButtonByDifficulty(difficulty).setBackgroundColor(
                getResources().getColor(R.color.purple_700)
        );
    }

    private void resetButtonColors() {
        int defaultColor = getResources().getColor(android.R.color.darker_gray);
        btnEasy.setBackgroundColor(defaultColor);
        btnMedium.setBackgroundColor(defaultColor);
        btnHard.setBackgroundColor(defaultColor);
    }

    private Button getButtonByDifficulty(String difficulty) {
        switch (difficulty) {
            case "easy":
                return btnEasy;
            case "medium":
                return btnMedium;
            case "hard":
                return btnHard;
            default:
                throw new IllegalArgumentException("Invalid difficulty: " + difficulty);
        }
    }

    private void navigateToCalculActivity() {
        Intent intent = new Intent(MainActivity.this, MainActivityCalcul.class);
        intent.putExtra("difficulty", selectedDifficulty);
        startActivity(intent);
    }
}
