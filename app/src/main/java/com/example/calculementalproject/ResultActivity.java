package com.example.calculementalproject;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    private static final int EXCELLENT_MIN = 8;
    private static final int GOOD_MIN = 5;
    private static final int FAIR_MIN = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        int score = getIntent().getIntExtra("score", 0);
        String difficulty = getIntent().getStringExtra("difficulty");

        findViewById(R.id.restartButton).setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivityCalcul.class)
                    .putExtra("difficulty", difficulty));
        });

        ((TextView) findViewById(R.id.scoreTextView)).setText("Votre score : " + score);

        showFeedback(score);
    }

    private void showFeedback(int score) {
        Feedback feedback = getFeedback(score);
        showAlertPopup(feedback);
    }

    private Feedback getFeedback(int score) {
        if (score >= EXCELLENT_MIN) {
            return new Feedback("Niveau : Excellent 🎉",
                    "Félicitations ! Vous avez fait un travail exceptionnel, continuez comme ça !",
                    R.drawable.eleve);
        } else if (score >= GOOD_MIN) {
            return new Feedback("Niveau : Bien 👍",
                    "Bon travail ! Vous êtes sur la bonne voie, mais il y a encore de la place pour l'amélioration.",
                    R.drawable.bien);
        } else if (score >= FAIR_MIN) {
            return new Feedback("Niveau : Assez Bien 💪",
                    "Pas mal ! Vous pouvez faire mieux avec un peu plus de pratique. Ne baissez pas les bras !",
                    R.drawable.assezbien);
        } else {
            return new Feedback("Niveau : Faible 😔",
                    "Ne vous découragez pas. Reprenez l'exercice et vous verrez des progrès rapidement !",
                    R.drawable.faible);
        }
    }

    private void showAlertPopup(Feedback feedback) {
        ImageView stickerView = new ImageView(this);
        stickerView.setImageResource(feedback.stickerResId);
        stickerView.setPadding(20, 20, 20, 20);

        new AlertDialog.Builder(this)
                .setTitle(feedback.title)
                .setMessage(feedback.message)
                .setView(stickerView)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private static class Feedback {
        final String title;
        final String message;
        final int stickerResId;

        Feedback(String title, String message, int stickerResId) {
            this.title = title;
            this.message = message;
            this.stickerResId = stickerResId;
        }
    }
}
