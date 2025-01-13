package com.example.calculementalproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Classe principale de l'application qui permet à l'utilisateur
 * de sélectionner un niveau de difficulté et de naviguer vers l'écran de calcul.
 */
public class MainActivity extends AppCompatActivity {

    private String selectedDifficulty = null; // Niveau de difficulté sélectionné
    private Button btnNext; // Bouton "Next" pour passer à l'écran suivant
    private Button btnEasy, btnMedium, btnHard; // Boutons pour les niveaux de difficulté

    /**
     * Méthode appelée lors de la création de l'activité.
     * @param savedInstanceState État sauvegardé de l'application.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialiser les boutons
        initButtons();

        // Configurer les actions des boutons
        setupDifficultyButtons();
        setupNextButton();
    }

    /**
     * Initialise les références aux boutons de l'interface utilisateur.
     */
    private void initButtons() {
        btnEasy = findViewById(R.id.btnEasy);
        btnMedium = findViewById(R.id.btnMedium);
        btnHard = findViewById(R.id.btnHard);
        btnNext = findViewById(R.id.btnNext);

        // Désactiver le bouton "Next" au démarrage
        btnNext.setEnabled(false);
    }

    /**
     * Configure les actions des boutons de sélection de la difficulté.
     */
    private void setupDifficultyButtons() {
        btnEasy.setOnClickListener(v -> updateDifficulty("easy"));
        btnMedium.setOnClickListener(v -> updateDifficulty("medium"));
        btnHard.setOnClickListener(v -> updateDifficulty("hard"));
    }

    /**
     * Configure l'action du bouton "Next" pour naviguer vers l'écran suivant.
     */
    private void setupNextButton() {
        btnNext.setOnClickListener(v -> {
            if (selectedDifficulty != null) {
                navigateToCalculActivity();
            }
        });
    }

    /**
     * Met à jour la difficulté sélectionnée, active le bouton "Next"
     * et met à jour l'apparence du bouton correspondant.
     * @param difficulty Niveau de difficulté sélectionné (easy, medium, hard).
     */
    private void updateDifficulty(String difficulty) {
        selectedDifficulty = difficulty;
        btnNext.setEnabled(true);

        // Réinitialiser les couleurs des boutons
        resetButtonColors();

        // Mettre en évidence le bouton sélectionné
        getButtonByDifficulty(difficulty).setBackgroundColor(
                getResources().getColor(R.color.purple_700)
        );
    }

    /**
     * Réinitialise les couleurs des boutons de difficulté à leur couleur par défaut.
     */
    private void resetButtonColors() {
        int defaultColor = getResources().getColor(android.R.color.darker_gray);
        btnEasy.setBackgroundColor(defaultColor);
        btnMedium.setBackgroundColor(defaultColor);
        btnHard.setBackgroundColor(defaultColor);
    }

    /**
     * Renvoie le bouton correspondant au niveau de difficulté spécifié.
     * @param difficulty Niveau de difficulté (easy, medium, hard).
     * @return Le bouton correspondant au niveau de difficulté.
     */
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

    /**
     * Navigue vers l'écran de calcul en transmettant le niveau de difficulté sélectionné.
     */
    private void navigateToCalculActivity() {
        Intent intent = new Intent(MainActivity.this, MainActivityCalcul.class);
        intent.putExtra("difficulty", selectedDifficulty);
        startActivity(intent);
    }
}
