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

public class MainActivityCalcul extends AppCompatActivity {

    private TextView equationTextView, timerTextView;
    private Button option1, option2, option3, exitButton;
    private int correctAnswer, score = 0, roundCount = 0;
    private String difficulty;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_calcule);

        initViews();
        difficulty = getIntent().getStringExtra("difficulty");

        generateEquation();
        startTimer();

        View.OnClickListener answerListener = view -> handleAnswerClick((Button) view);
        option1.setOnClickListener(answerListener);
        option2.setOnClickListener(answerListener);
        option3.setOnClickListener(answerListener);

        exitButton.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }

    private void initViews() {
        equationTextView = findViewById(R.id.equationTextView);
        timerTextView = findViewById(R.id.timerTextView);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        exitButton = findViewById(R.id.backToMainButton);
    }

    private void handleAnswerClick(Button clickedButton) {
        int chosenAnswer = Integer.parseInt(clickedButton.getText().toString());
        highlightCorrectAnswer(clickedButton, chosenAnswer == correctAnswer);

        timer.cancel();
        roundCount++;
        new Handler().postDelayed(this::checkRound, 2000);
    }

    private void highlightCorrectAnswer(Button clickedButton, boolean isCorrect) {
        clickedButton.setBackgroundColor(ContextCompat.getColor(this,
                isCorrect ? android.R.color.holo_green_dark : android.R.color.holo_red_dark));

        if (!isCorrect) {
            getCorrectButton().setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_green_dark));
        } else {
            score++;
        }
    }

    private Button getCorrectButton() {
        Button[] buttons = {option1, option2, option3};
        for (Button button : buttons) {
            if (Integer.parseInt(button.getText().toString()) == correctAnswer) {
                return button;
            }
        }
        return null; // This should never happen
    }

    private void checkRound() {
        if (roundCount < 10) {
            resetButtonColors();
            generateEquation();
            startTimer();
        } else {
            navigateToResultActivity();
        }
    }

    private void resetButtonColors() {
        int defaultColor = ContextCompat.getColor(this, android.R.color.holo_purple);
        option1.setBackgroundColor(defaultColor);
        option2.setBackgroundColor(defaultColor);
        option3.setBackgroundColor(defaultColor);
    }

    private void navigateToResultActivity() {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("difficulty", difficulty);
        startActivity(intent);
        finish();
    }

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

    private int getRandomNumber(Random random) {
        switch (difficulty) {
            case "medium":
                return 10 + random.nextInt(41);
            case "hard":
                return 20 + random.nextInt(81);
            default: // easy
                return random.nextInt(10) + 1;
        }
    }

    private int calculateResult(int num1, int num2, String operation) {
        switch (operation) {
            case "+": return num1 + num2;
            case "-": return num1 - num2;
            case "*": return num1 * num2;
            case "/": return num2 != 0 ? num1 / num2 : 1; // Avoid division by zero
            default: return 0;
        }
    }

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

    private void shuffleArray(Integer[] array) {
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}
