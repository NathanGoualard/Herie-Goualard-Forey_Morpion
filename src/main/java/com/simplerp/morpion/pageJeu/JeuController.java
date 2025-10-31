package com.simplerp.morpion.pageJeu;

import com.simplerp.morpion.Db;
import com.simplerp.morpion.engine.MorpionPlateau;
import com.simplerp.morpion.engine.Pion;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.geometry.Pos;
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


    @FXML
    public void initialize() {
        plateau = new MorpionPlateau(3, 3, 3);
        boutons = new Button[3][3];
        joueurActuel = Pion.CROIX;

        labelJoueur.setVisible(false);
        chargerJoueursDepuisGameInProgress();

        configurerGrille();
        mettreAJourLabelJoueur();
        mettreAJourScores();
    }

    private void chargerJoueursDepuisGameInProgress() {
        try (Connection connexion = Db.get()) {

            PreparedStatement requetePartie = connexion.prepareStatement("SELECT id_player1, id_player2, score_j1, score_j2 FROM games_in_progress LIMIT 1");
            ResultSet resultatPartie = requetePartie.executeQuery();

            if (resultatPartie.next()) {
                idJoueur1 = resultatPartie.getInt("id_player1");
                idJoueur2 = resultatPartie.getInt("id_player2");
                scoreJ1 = resultatPartie.getInt("score_j1");
                scoreJ2 = resultatPartie.getInt("score_j2");
            } else {
                // Pas de partie en cours
                idJoueur1 = -1;
                idJoueur2 = -1;
                nomJoueur1 = "Joueur 1 X";
                nomJoueur2 = "Joueur 2 O";
                resultatPartie.close();
                requetePartie.close();
                return;
            }

            PreparedStatement requeteNomJoueur = connexion.prepareStatement("SELECT name FROM players WHERE id_player = ?");

            // Joueur 1
            requeteNomJoueur.setInt(1, idJoueur1);
            try (ResultSet resultatNomJoueur1 = requeteNomJoueur.executeQuery()) {
                if (resultatNomJoueur1.next()) {
                    nomJoueur1 = resultatNomJoueur1.getString("name") + " X";
                } else {
                    nomJoueur1 = "Joueur 1 X";
                }
            }

            // Joueur 2
            requeteNomJoueur.setInt(1, idJoueur2);
            try (ResultSet resultatNomJoueur2 = requeteNomJoueur.executeQuery()) {
                if (resultatNomJoueur2.next()) {
                    nomJoueur2 = resultatNomJoueur2.getString("name") + " O";
                } else {
                    nomJoueur2 = "Joueur 2 O";
                }
            }

            requeteNomJoueur.close();
            resultatPartie.close();
            requetePartie.close();

        } catch (SQLException e) {
            e.printStackTrace();
            nomJoueur1 = "Joueur 1 X";
            nomJoueur2 = "Joueur 2 O";
        }
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
        try (Connection connexion = Db.get()) {

            PreparedStatement requeteScore = connexion.prepareStatement("SELECT score_j1, score_j2 FROM games_in_progress");

            ResultSet resultatScore = requeteScore.executeQuery();

            scoreJ1 = resultatScore.getInt("score_j1");
            scoreJ2 = resultatScore.getInt("score_j2");

            resultatScore.close();
            requeteScore.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }


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
        String gagnantNom;
        int idVainqueur;

        if (joueurActuel == Pion.CROIX) {
            gagnantNom = nomJoueur1;
            idVainqueur = idJoueur1;
            scoreJ1++;
        } else {
            gagnantNom = nomJoueur2;
            idVainqueur = idJoueur2;
            scoreJ2++;
        }

        try (Connection connexion = Db.get()) {

            try (PreparedStatement psGame = connexion.prepareStatement("""
                UPDATE games_in_progress
                SET score_j1 = ?, score_j2 = ?
            """)) {
                psGame.setInt(1, scoreJ1);
                psGame.setInt(2, scoreJ2);
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

    @FXML
    private void allerAccueil() {
        sauvegarderPartieEnCours();

        try {
            javafx.scene.layout.Pane root = javafx.fxml.FXMLLoader.load(
                    getClass().getResource("/com/simplerp/morpion/PageAccueil.fxml")
            );
            grilleJeu.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void sauvegarderPartieEnCours() {
        try (Connection connexion = Db.get()) {

            PreparedStatement ps1 = connexion.prepareStatement("""
            UPDATE scores
            SET nb_victories = nb_victories + ?
            WHERE id_player = ? AND grid_size = 3
        """);
            ps1.setInt(1, scoreJ1);
            ps1.setInt(2, idJoueur1);
            int rows1 = ps1.executeUpdate();
            ps1.close();

            if (rows1 == 0) {
                PreparedStatement psInsert1 = connexion.prepareStatement("""
                INSERT INTO scores (id_player, nb_victories, grid_size)
                VALUES (?, ?, 3)
            """);
                psInsert1.setInt(1, idJoueur1);
                psInsert1.setInt(2, scoreJ1);
                psInsert1.executeUpdate();
                psInsert1.close();
            }

            PreparedStatement ps2 = connexion.prepareStatement("""
            UPDATE scores
            SET nb_victories = nb_victories + ?
            WHERE id_player = ? AND grid_size = 3
        """);
            ps2.setInt(1, scoreJ2);
            ps2.setInt(2, idJoueur2);
            int rows2 = ps2.executeUpdate();
            ps2.close();

            if (rows2 == 0) {
                PreparedStatement psInsert2 = connexion.prepareStatement("""
                INSERT INTO scores (id_player, nb_victories, grid_size)
                VALUES (?, ?, 3)
            """);
                psInsert2.setInt(1, idJoueur2);
                psInsert2.setInt(2, scoreJ2);
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
