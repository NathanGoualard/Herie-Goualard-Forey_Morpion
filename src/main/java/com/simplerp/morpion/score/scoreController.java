package com.simplerp.morpion.score;

import com.simplerp.morpion.Db;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.*;
import java.sql.*;

public class scoreController {

    @FXML private TableView<ScoreEntry> scoreTable;
    @FXML private TableColumn<ScoreEntry, Integer> rankColumn;
    @FXML private TableColumn<ScoreEntry, String> playerColumn;
    @FXML private TableColumn<ScoreEntry, Integer> scoreColumn;
    @FXML private Button homeBtn;

    @FXML
    public void initialize() {
        rankColumn.setCellValueFactory(d -> d.getValue().rankProperty().asObject());
        playerColumn.setCellValueFactory(d -> d.getValue().playerProperty());
        scoreColumn.setCellValueFactory(d -> d.getValue().scoreProperty().asObject());

        scoreTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        loadScores();
        homeBtn.setOnAction(e -> goHome());
    }

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

    private void goHome() {
        try {
            Pane root = FXMLLoader.load(getClass().getResource("/com/simplerp/morpion/PageAccueil.fxml"));
            scoreTable.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}