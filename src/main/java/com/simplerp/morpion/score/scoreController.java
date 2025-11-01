package com.simplerp.morpion.score;

// Contrôleur de la page des scores.
// Gère l’affichage du classement des joueurs, le chargement des données depuis la base,
// et la navigation vers la page d’accueil.

import com.simplerp.morpion.Db;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.*;

public class scoreController {

    // Références aux éléments de l’interface
    @FXML private TableView<ScoreEntry> scoreTable;
    @FXML private TableColumn<ScoreEntry, Integer> rankColumn;
    @FXML private TableColumn<ScoreEntry, String> playerColumn;
    @FXML private TableColumn<ScoreEntry, Integer> scoreColumn;
    @FXML private Button homeBtn;

    // Initialise la table et charge les scores au démarrage
    @FXML
    public void initialize() {
        rankColumn.setCellValueFactory(d -> d.getValue().rankProperty().asObject());
        playerColumn.setCellValueFactory(d -> d.getValue().playerProperty());
        scoreColumn.setCellValueFactory(d -> d.getValue().scoreProperty().asObject());

        scoreTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        loadScores();
        homeBtn.setOnAction(e -> versAccueil());
    }

    // Charge les scores depuis la base de données et les affiche dans la table
    private void loadScores() {
        scoreTable.getItems().clear();
        try (Connection c = Db.get();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT p.name, s.nb_victories " +
                             "FROM scores s JOIN players p ON s.id_player=p.id_player " +
                             "ORDER BY s.nb_victories DESC")) {

            ResultSet rs = ps.executeQuery();
            int r = 1;
            while (rs.next()) {
                scoreTable.getItems().add(
                        new ScoreEntry(r++, rs.getString("name"), rs.getInt("nb_victories"))
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Retourne à la page d’accueil
    private void versAccueil() {
        try {
            Stage currentStage = (Stage) scoreTable.getScene().getWindow();
            currentStage.close();

            com.simplerp.morpion.accueil.AccueilApplication accueilApp = new com.simplerp.morpion.accueil.AccueilApplication();
            accueilApp.start(new Stage());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}