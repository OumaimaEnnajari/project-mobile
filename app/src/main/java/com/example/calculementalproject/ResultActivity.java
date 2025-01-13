package com.example.calculementalproject;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity qui affiche le résultat du quiz de calcul mental.
 * Cette activité affiche le score de l'utilisateur et lui donne un retour sur ses performances.
 */
public class ResultActivity extends AppCompatActivity {

    /**
     * Score minimal pour un niveau "Excellent".
     */
    private static final int EXCELLENT_MIN = 8;

    /**
     * Score minimal pour un niveau "Bien".
     */
    private static final int GOOD_MIN = 5;

    /**
     * Score minimal pour un niveau "Assez Bien".
     */
    private static final int FAIR_MIN = 3;

    /**
     * Méthode appelée lors de la création de l'activité. Elle récupère les informations passées depuis
     * l'activité précédente et initialise l'interface utilisateur.
     *
     * @param savedInstanceState L'état sauvegardé de l'activité, ou null si aucune donnée n'est sauvegardée.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Récupération du score et de la difficulté depuis l'intent
        int score = getIntent().getIntExtra("score", 0);
        String difficulty = getIntent().getStringExtra("difficulty");

        // Mise en place du bouton pour recommencer le quiz
        findViewById(R.id.restartButton).setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivityCalcul.class)
                    .putExtra("difficulty", difficulty)); // Transfert de la difficulté au redémarrage
        });

        // Affichage du score de l'utilisateur
        ((TextView) findViewById(R.id.scoreTextView)).setText("Votre score : " + score);

        // Affichage du feedback en fonction du score
        showFeedback(score);
    }

    /**
     * Affiche un retour visuel et textuel en fonction du score de l'utilisateur.
     *
     * @param score Le score de l'utilisateur pour déterminer le niveau.
     */
    private void showFeedback(int score) {
        // Obtenir le feedback basé sur le score
        Feedback feedback = getFeedback(score);

        // Affichage du feedback sous forme de pop-up
        showAlertPopup(feedback);
    }

    /**
     * Retourne un objet Feedback contenant un titre, un message et une image en fonction du score de l'utilisateur.
     *
     * @param score Le score de l'utilisateur.
     * @return Un objet Feedback contenant des informations sur le niveau de performance.
     */
    private Feedback getFeedback(int score) {
        // Déterminer le niveau de l'utilisateur en fonction du score
        if (score >= EXCELLENT_MIN) {
            return new Feedback("Niveau : Excellent 🎉",
                    "Félicitations ! Vous avez fait un travail exceptionnel, continuez comme ça !",
                    R.drawable.eleve); // Ressource image pour l'excellent niveau
        } else if (score >= GOOD_MIN) {
            return new Feedback("Niveau : Bien 👍",
                    "Bon travail ! Vous êtes sur la bonne voie, mais il y a encore de la place pour l'amélioration.",
                    R.drawable.bien); // Ressource image pour le niveau "Bien"
        } else if (score >= FAIR_MIN) {
            return new Feedback("Niveau : Assez Bien 💪",
                    "Pas mal ! Vous pouvez faire mieux avec un peu plus de pratique. Ne baissez pas les bras !",
                    R.drawable.assezbien); // Ressource image pour le niveau "Assez Bien"
        } else {
            return new Feedback("Niveau : Faible 😔",
                    "Ne vous découragez pas. Reprenez l'exercice et vous verrez des progrès rapidement !",
                    R.drawable.faible); // Ressource image pour le niveau "Faible"
        }
    }

    /**
     * Affiche une boîte de dialogue contenant le feedback, avec une image et un message.
     *
     * @param feedback L'objet Feedback contenant le titre, le message et la ressource de l'image.
     */
    private void showAlertPopup(Feedback feedback) {
        // Créer une ImageView pour afficher l'image de feedback
        ImageView stickerView = new ImageView(this);
        stickerView.setImageResource(feedback.stickerResId); // Assigner l'image à l'ImageView
        stickerView.setPadding(20, 20, 20, 20); // Ajouter un peu de padding à l'image

        // Créer et afficher la boîte de dialogue
        new AlertDialog.Builder(this)
                .setTitle(feedback.title) // Définir le titre de la boîte de dialogue
                .setMessage(feedback.message) // Définir le message de la boîte de dialogue
                .setView(stickerView) // Ajouter l'ImageView à la boîte de dialogue
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss()) // Fermeture de la boîte de dialogue
                .show();
    }

    /**
     * Classe interne représentant un feedback à afficher à l'utilisateur,
     * contenant le titre, le message et l'image associée au niveau de performance.
     */
    private static class Feedback {
        final String title; // Titre du feedback
        final String message; // Message du feedback
        final int stickerResId; // ID de la ressource de l'image (sticker)

        /**
         * Constructeur de la classe Feedback.
         *
         * @param title        Le titre du feedback.
         * @param message      Le message du feedback.
         * @param stickerResId L'ID de la ressource de l'image à afficher.
         */
        Feedback(String title, String message, int stickerResId) {
            this.title = title;
            this.message = message;
            this.stickerResId = stickerResId;
        }
    }
}
