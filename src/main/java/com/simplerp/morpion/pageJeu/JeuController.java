package com.simplerp.morpion.pageJeu;

import com.simplerp.morpion.engine.MorpionPlateau;
import com.simplerp.morpion.engine.Pion;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.geometry.Pos;

public class JeuController {

    @FXML private GridPane grilleJeu;
    @FXML private Label labelJoueur;
    @FXML private Label scoreJ1Label;
    @FXML private Label scoreJ2Label;
    @FXML private Button boutonRecommencer;
    @FXML private Button boutonAccueil;

    private MorpionPlateau plateau;
    private Button[][] boutons;
    private Pion joueurActuel;
    private int scoreJ1 = 0;
    private int scoreJ2 = 0;
    private String nomJoueur1 = "Joueur 1 (X)";
    private String nomJoueur2 = "Joueur 2 (O)";

    @FXML
    public void initialize() {
        plateau = new MorpionPlateau(3, 3, 3);
        boutons = new Button[3][3];
        joueurActuel = Pion.CROIX;

        labelJoueur.setVisible(false);

        configurerGrille();
        mettreAJourLabelJoueur();
        mettreAJourScores();
    }

    private void configurerGrille() {
        grilleJeu.getChildren().clear();
        grilleJeu.setHgap(5);
        grilleJeu.setVgap(5);
        grilleJeu.setAlignment(Pos.CENTER);

        for (int ligne = 0; ligne < 3; ligne++) {
            for (int colonne = 0; colonne < 3; colonne++) {
                Button bouton = creerBoutonCase(ligne, colonne);
                boutons[ligne][colonne] = bouton;
                grilleJeu.add(bouton, colonne, ligne);
            }
        }
    }

    private Button creerBoutonCase(int ligne, int colonne) {
        Button bouton = new Button("");
        bouton.setPrefSize(80, 80);
        bouton.setStyle(
                "-fx-background-color: black;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-color: #FFF;"
        );

        bouton.setOnAction(e -> gererClicCase(ligne, colonne, bouton));

        bouton.setOnMouseEntered(e -> {
            if (bouton.getText().isEmpty()) {
                bouton.setStyle(
                        "-fx-background-color: #333333;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-size: 24px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-cursor: hand;" +
                                "-fx-border-width: 2;" +
                                "-fx-border-color: #FFF;"
                );
            }
        });

        bouton.setOnMouseExited(e -> {
            if (bouton.getText().isEmpty()) {
                bouton.setStyle(
                        "-fx-background-color: black;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-size: 24px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-cursor: hand;" +
                                "-fx-border-width: 2;" +
                                "-fx-border-color: #FFF;"
                );
            }
        });

        return bouton;
    }

    private void gererClicCase(int ligne, int colonne, Button bouton) {
        if (!bouton.getText().isEmpty()) {
            return;
        }

        try {
            plateau.placerPion(ligne, colonne, joueurActuel);
            mettreAJourAffichageBouton(bouton, joueurActuel);

            if (plateau.verifierVictoire(ligne, colonne, joueurActuel)) {
                gererVictoire();
                return;
            }

            if (grilleEstPleine()) {
                gererMatchNul();
                return;
            }

            changerJoueur();

        } catch (IllegalArgumentException e) {
            afficherErreur(e.getMessage());
        }
    }

    private void mettreAJourAffichageBouton(Button bouton, Pion pion) {
        if (pion == Pion.CROIX) {
            bouton.setText("X");
            bouton.setStyle(
                    "-fx-background-color: black;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 24px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-border-width: 2;" +
                            "-fx-border-color: #FFF;"
            );
        } else {
            bouton.setText("O");
            bouton.setStyle(
                    "-fx-background-color: black;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 24px;" +
                            "-fx-font-weight: bold;"+
                            "-fx-border-width: 2;" +
                            "-fx-border-color: #FFF;"
            );
        }
    }

    private void changerJoueur() {
        joueurActuel = (joueurActuel == Pion.CROIX) ? Pion.ROND : Pion.CROIX;
        mettreAJourLabelJoueur();
    }

    private void mettreAJourLabelJoueur() {
        if (joueurActuel == Pion.CROIX) {
            scoreJ1Label.setStyle(
                    "-fx-font-size: 20px;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-color: rgba(155, 155, 155, 0.2);" +
                            "-fx-padding: 5px 10px;" +
                            "-fx-background-radius: 5px;"
            );
            scoreJ2Label.setStyle(
                    "-fx-font-size: 18px;" +
                            "-fx-text-fill: white;"
            );
        } else {
            scoreJ1Label.setStyle(
                    "-fx-font-size: 18px;" +
                            "-fx-text-fill: white;"
            );
            scoreJ2Label.setStyle(
                    "-fx-font-size: 20px;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-color: rgba(155, 155, 155, 0.2);" +
                            "-fx-padding: 5px 10px;" +
                            "-fx-background-radius: 5px;"
            );
        }
    }

    private void mettreAJourScores() {
        scoreJ1Label.setText(nomJoueur1 + " : " + scoreJ1);
        scoreJ2Label.setText(nomJoueur2 + " : " + scoreJ2);
        mettreAJourLabelJoueur();
    }

    private boolean grilleEstPleine() {
        for (int ligne = 0; ligne < 3; ligne++) {
            for (int colonne = 0; colonne < 3; colonne++) {
                if (boutons[ligne][colonne].getText().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void gererVictoire() {
        String gagnant = (joueurActuel == Pion.CROIX) ? nomJoueur1 : nomJoueur2;

        if (joueurActuel == Pion.CROIX) {
            scoreJ1++;
        } else {
            scoreJ2++;
        }
        mettreAJourScores();

        Alert alerte = new Alert(Alert.AlertType.INFORMATION);
        alerte.setTitle("Victoire !");
        alerte.setHeaderText(gagnant + " a gagné !");
        alerte.setContentText("Félicitations !");
        DialogPane dialogPane = alerte.getDialogPane();
        dialogPane.setStyle("-fx-background-color: black;");
        dialogPane.setGraphic(null);
        alerte.showAndWait();

        recommencerJeu();
    }

    private void gererMatchNul() {
        Alert alerte = new Alert(Alert.AlertType.INFORMATION);
        alerte.setTitle("Match nul");
        alerte.setHeaderText("Match nul !");
        alerte.setContentText("Aucun joueur n'a gagné.");
        alerte.showAndWait();

        recommencerJeu();
    }

    @FXML
    private void recommencerJeu() {
        plateau = new MorpionPlateau(3, 3, 3);
        joueurActuel = Pion.CROIX;

        for (int ligne = 0; ligne < 3; ligne++) {
            for (int colonne = 0; colonne < 3; colonne++) {
                boutons[ligne][colonne].setText("");
                boutons[ligne][colonne].setStyle(
                        "-fx-background-color: black;" +
                                "-fx-text-fill: white;" +
                                "-fx-border-width: 2;" +
                                "-fx-border-color: #FFF;" +
                                "-fx-font-size: 24px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-cursor: hand;"
                );
            }
        }

        mettreAJourLabelJoueur();
    }

    private void afficherErreur(String message) {
        Alert alerte = new Alert(Alert.AlertType.ERROR);
        alerte.setTitle("Erreur");
        alerte.setHeaderText("Erreur de jeu");
        alerte.setContentText(message);
        alerte.showAndWait();
    }

    public void definirNomsJoueurs(String joueur1, String joueur2) {
        this.nomJoueur1 = joueur1 + " (X)";
        this.nomJoueur2 = joueur2 + " (O)";
        mettreAJourScores();
    }

    @FXML
    private void allerAccueil() {
        try {
            javafx.scene.layout.Pane root = javafx.fxml.FXMLLoader.load(
                    getClass().getResource("/com/simplerp/morpion/PageAccueil.fxml")
            );
            grilleJeu.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}