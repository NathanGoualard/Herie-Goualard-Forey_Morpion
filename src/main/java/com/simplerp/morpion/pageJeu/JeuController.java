package com.simplerp.morpion.pageJeu;

import com.simplerp.morpion.Db;
import com.simplerp.morpion.engine.MorpionPlateau;
import com.simplerp.morpion.engine.Pion;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Node;
import com.simplerp.morpion.accueil.AccueilApplication;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    private String nomJoueur1;
    private String nomJoueur2;
    private int idJoueur1 = -1;
    private int idJoueur2 = -1;
    private int tailleGrille = 3;
    private boolean partieInitialisee = false;

    @FXML
    public void initialize() {
        // Ne rien faire ici, on attend initialiserPartie()
        labelJoueur.setVisible(false);
    }

    public void initialiserPartie(String nomJ1, String nomJ2, int taille, int idJ1, int idJ2) {
        this.nomJoueur1 = nomJ1 + " X";
        this.nomJoueur2 = nomJ2 + " O";
        this.tailleGrille = taille;
        this.idJoueur1 = idJ1;
        this.idJoueur2 = idJ2;
        this.partieInitialisee = true;

        System.out.println("Partie initialisée : " + nomJ1 + " vs " + nomJ2 + " (Grille " + taille + "x" + taille + ")");

        // Charger les scores depuis la base de données
        chargerScoresDepuisBase();

        // Initialiser le jeu
        plateau = new MorpionPlateau(taille, taille, taille);
        boutons = new Button[taille][taille];
        joueurActuel = Pion.CROIX;

        configurerGrille();
        mettreAJourLabelJoueur();
        mettreAJourScores();
    }

    private void chargerScoresDepuisBase() {
        try (Connection connexion = Db.get()) {
            PreparedStatement requetePartie = connexion.prepareStatement(
                    "SELECT score_j1, score_j2 FROM games_in_progress WHERE id_player1 = ? AND id_player2 = ?"
            );
            requetePartie.setInt(1, idJoueur1);
            requetePartie.setInt(2, idJoueur2);
            ResultSet resultatPartie = requetePartie.executeQuery();

            if (resultatPartie.next()) {
                scoreJ1 = resultatPartie.getInt("score_j1");
                scoreJ2 = resultatPartie.getInt("score_j2");
            } else {
                scoreJ1 = 0;
                scoreJ2 = 0;
            }

            resultatPartie.close();
            requetePartie.close();

        } catch (SQLException e) {
            e.printStackTrace();
            scoreJ1 = 0;
            scoreJ2 = 0;
        }
    }

    private void configurerGrille() {
        grilleJeu.getChildren().clear();
        grilleJeu.setHgap(5);
        grilleJeu.setVgap(5);
        grilleJeu.setAlignment(Pos.CENTER);

        for (int ligne = 0; ligne < tailleGrille; ligne++) {
            for (int colonne = 0; colonne < tailleGrille; colonne++) {
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
        for (int ligne = 0; ligne < tailleGrille; ligne++) {
            for (int colonne = 0; colonne < tailleGrille; colonne++) {
                if (boutons[ligne][colonne].getText().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void gererVictoire() {
        String gagnantNom;

        if (joueurActuel == Pion.CROIX) {
            gagnantNom = nomJoueur1;
            scoreJ1++;
        } else {
            gagnantNom = nomJoueur2;
            scoreJ2++;
        }

        try (Connection connexion = Db.get()) {
            try (PreparedStatement psGame = connexion.prepareStatement("""
                UPDATE games_in_progress
                SET score_j1 = ?, score_j2 = ?
                WHERE id_player1 = ? AND id_player2 = ?
            """)) {
                psGame.setInt(1, scoreJ1);
                psGame.setInt(2, scoreJ2);
                psGame.setInt(3, idJoueur1);
                psGame.setInt(4, idJoueur2);
                psGame.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        mettreAJourScores();

        Alert alerte = new Alert(Alert.AlertType.INFORMATION);
        alerte.setTitle("Victoire !");
        alerte.setHeaderText(gagnantNom + " a gagné !");
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
        plateau = new MorpionPlateau(tailleGrille, tailleGrille, tailleGrille);
        joueurActuel = Pion.CROIX;

        for (int ligne = 0; ligne < tailleGrille; ligne++) {
            for (int colonne = 0; colonne < tailleGrille; colonne++) {
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

    @FXML
    private void allerAccueil() {
        sauvegarderPartieEnCours();

        try {
            // Fermer la fenêtre actuelle
            Stage stageActuel = (Stage) grilleJeu.getScene().getWindow();
            stageActuel.close();

            // Lancer AccueilApplication
            AccueilApplication accueilApp = new AccueilApplication();
            accueilApp.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sauvegarderPartieEnCours() {
        try (Connection connexion = Db.get()) {

            PreparedStatement ps1 = connexion.prepareStatement("""
            UPDATE scores
            SET nb_victories = nb_victories + ?
            WHERE id_player = ? AND grid_size = ?
        """);
            ps1.setInt(1, scoreJ1);
            ps1.setInt(2, idJoueur1);
            ps1.setInt(3, tailleGrille);
            int rows1 = ps1.executeUpdate();
            ps1.close();

            if (rows1 == 0) {
                PreparedStatement psInsert1 = connexion.prepareStatement("""
                INSERT INTO scores (id_player, nb_victories, grid_size)
                VALUES (?, ?, ?)
            """);
                psInsert1.setInt(1, idJoueur1);
                psInsert1.setInt(2, scoreJ1);
                psInsert1.setInt(3, tailleGrille);
                psInsert1.executeUpdate();
                psInsert1.close();
            }

            PreparedStatement ps2 = connexion.prepareStatement("""
            UPDATE scores
            SET nb_victories = nb_victories + ?
            WHERE id_player = ? AND grid_size = ?
        """);
            ps2.setInt(1, scoreJ2);
            ps2.setInt(2, idJoueur2);
            ps2.setInt(3, tailleGrille);
            int rows2 = ps2.executeUpdate();
            ps2.close();

            if (rows2 == 0) {
                PreparedStatement psInsert2 = connexion.prepareStatement("""
                INSERT INTO scores (id_player, nb_victories, grid_size)
                VALUES (?, ?, ?)
            """);
                psInsert2.setInt(1, idJoueur2);
                psInsert2.setInt(2, scoreJ2);
                psInsert2.setInt(3, tailleGrille);
                psInsert2.executeUpdate();
                psInsert2.close();
            }

            PreparedStatement psDel = connexion.prepareStatement("DELETE FROM games_in_progress");
            psDel.executeUpdate();
            psDel.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}