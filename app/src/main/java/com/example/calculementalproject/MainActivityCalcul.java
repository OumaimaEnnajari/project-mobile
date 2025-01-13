package com.example.calculementalproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Classe principale pour l'activité de calcul mental.
 * Gère la génération d'équations, la vérification des réponses,
 * le chronomètre et le score de l'utilisateur.
 */
public class MainActivityCalcul extends AppCompatActivity {

    // Éléments de l'interface utilisateur
    private TextView equationTextView, timerTextView;
    private Button option1, option2, option3, exitButton;

    // Variables de gestion de la logique
    private int correctAnswer, score = 0, roundCount = 0;
    private String difficulty;
    private CountDownTimer timer;

    /**
     * Méthode appelée lors de la création de l'activité.
     * Initialise les éléments de l'interface utilisateur et configure les listeners.
     *
     * @param savedInstanceState état de l'activité précédemment sauvegardé.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_calcule);

        initViews();
        difficulty = getIntent().getStringExtra("difficulty");

        generateEquation();
        startTimer();

        // Définir les listeners pour les options
        View.OnClickListener answerListener = view -> handleAnswerClick((Button) view);
        option1.setOnClickListener(answerListener);
        option2.setOnClickListener(answerListener);
        option3.setOnClickListener(answerListener);

        // Listener pour le bouton de sortie
        exitButton.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }

    /**
     * Initialise les vues de l'interface utilisateur.
     */
    private void initViews() {
        equationTextView = findViewById(R.id.equationTextView);
        timerTextView = findViewById(R.id.timerTextView);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        exitButton = findViewById(R.id.backToMainButton);
    }

    /**
     * Gère l'événement de clic sur une réponse.
     * Vérifie si la réponse est correcte, met à jour l'interface utilisateur,
     * puis passe à l'équation suivante.
     *
     * @param clickedButton bouton cliqué par l'utilisateur.
     */
    private void handleAnswerClick(Button clickedButton) {
        int chosenAnswer = Integer.parseInt(clickedButton.getText().toString());
        highlightCorrectAnswer(clickedButton, chosenAnswer == correctAnswer);

        timer.cancel();
        roundCount++;
        new Handler().postDelayed(this::checkRound, 2000);
    }

    /**
     * Met en surbrillance la réponse correcte et met à jour le score.
     *
     * @param clickedButton bouton sélectionné par l'utilisateur.
     * @param isCorrect indique si la réponse est correcte.
     */
    private void highlightCorrectAnswer(Button clickedButton, boolean isCorrect) {
        clickedButton.setBackgroundColor(ContextCompat.getColor(this,
                isCorrect ? android.R.color.holo_green_dark : android.R.color.holo_red_dark));

        if (!isCorrect) {
            getCorrectButton().setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_green_dark));
        } else {
            score++;
        }
    }

    /**
     * Obtient le bouton correspondant à la réponse correcte.
     *
     * @return bouton contenant la réponse correcte.
     */
    private Button getCorrectButton() {
        Button[] buttons = {option1, option2, option3};
        for (Button button : buttons) {
            if (Integer.parseInt(button.getText().toString()) == correctAnswer) {
                return button;
            }
        }
        return null; // Ne devrait jamais arriver
    }

    /**
     * Vérifie si le jeu doit continuer ou se terminer.
     * Passe à une nouvelle équation ou navigue vers l'activité des résultats.
     */
    private void checkRound() {
        if (roundCount < 10) {
            resetButtonColors();
            generateEquation();
            startTimer();
        } else {
            navigateToResultActivity();
        }
    }

    /**
     * Réinitialise les couleurs des boutons.
     */
    private void resetButtonColors() {
        int defaultColor = ContextCompat.getColor(this, android.R.color.holo_purple);
        option1.setBackgroundColor(defaultColor);
        option2.setBackgroundColor(defaultColor);
        option3.setBackgroundColor(defaultColor);
    }

    /**
     * Navigue vers l'activité des résultats avec les données du score.
     */
    private void navigateToResultActivity() {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("difficulty", difficulty);
        startActivity(intent);
        finish();
    }

    /**
     * Génère une nouvelle équation et met à jour les options de réponse.
     */
    private void generateEquation() {
        Random random = new Random();
        int num1 = getRandomNumber(random);
        int num2 = getRandomNumber(random);

        String[] operations = {"+", "-", "*", "/"};
        String operation = operations[random.nextInt(operations.length)];
        correctAnswer = calculateResult(num1, num2, operation);

        equationTextView.setText(String.format("%d %s %d = ?", num1, operation, num2));
        setButtonOptions(random);
    }

    /**
     * Génère un nombre aléatoire basé sur le niveau de difficulté.
     *
     * @param random instance de Random pour la génération aléatoire.
     * @return un nombre aléatoire.
     */
    private int getRandomNumber(Random random) {
        switch (difficulty) {
            case "medium":
                return 10 + random.nextInt(41);
            case "hard":
                return 20 + random.nextInt(81);
            default: // Facile
                return random.nextInt(10) + 1;
        }
    }

    /**
     * Calcule le résultat de l'équation en fonction de l'opération.
     *
     * @param num1 premier opérande.
     * @param num2 deuxième opérande.
     * @param operation opération mathématique.
     * @return résultat de l'opération.
     */
    private int calculateResult(int num1, int num2, String operation) {
        switch (operation) {
            case "+": return num1 + num2;
            case "-": return num1 - num2;
            case "*": return num1 * num2;
            case "/": {
                if (num2 == 0) num2 = 1; // Éviter la division par zéro
                return num1 / num2;
            }
            default: return 0;
        }
    }

    /**
     * Configure les options de réponse avec une réponse correcte et deux incorrectes.
     *
     * @param random instance de Random pour la génération aléatoire.
     */
    private void setButtonOptions(Random random) {
        Set<Integer> answers = new HashSet<>();
        answers.add(correctAnswer);

        while (answers.size() < 3) {
            int wrongAnswer = correctAnswer + random.nextInt(21) - 10;
            answers.add(wrongAnswer);
        }

        Integer[] shuffledAnswers = answers.toArray(new Integer[0]);
        shuffleArray(shuffledAnswers);

        option1.setText(String.valueOf(shuffledAnswers[0]));
        option2.setText(String.valueOf(shuffledAnswers[1]));
        option3.setText(String.valueOf(shuffledAnswers[2]));
    }

    /**
     * Mélange les éléments d'un tableau.
     *
     * @param array tableau d'entiers à mélanger.
     */
    private void shuffleArray(Integer[] array) {
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    /**
     * Démarre un chronomètre de 10 secondes pour chaque équation.
     */
    private void startTimer() {
        timer = new CountDownTimer(11000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerTextView.setText(String.format("Temps restant : %d s", millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                timerTextView.setText("Temps écoulé !");
                roundCount++;
                checkRound();
            }
        };
        timer.start();
    }

    /**
     * Annule le chronomètre lorsqu'il n'est plus nécessaire.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}
