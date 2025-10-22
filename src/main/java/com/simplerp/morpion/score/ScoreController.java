package com.simplerp.morpion.score;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class ScoreController {

    @FXML private TableView<ScoreEntry> scoreTable;
    @FXML private TableColumn<ScoreEntry, Integer> rankColumn;
    @FXML private TableColumn<ScoreEntry, String> playerColumn;
    @FXML private TableColumn<ScoreEntry, Integer> scoreColumn;


    @FXML
    public void initialize() {
        rankColumn.setCellValueFactory(data -> data.getValue().rankProperty().asObject());
        playerColumn.setCellValueFactory(data -> data.getValue().playerProperty());
        scoreColumn.setCellValueFactory(data -> data.getValue().scoreProperty().asObject());

        ObservableList<ScoreEntry> scores = FXCollections.observableArrayList(
                new ScoreEntry(1, "Nom du joueur", 10),
                new ScoreEntry(2, "Joueur 2", 8),
                new ScoreEntry(3, "Joueur 3", 6),
                new ScoreEntry(4, "Joueur 4", 5),
                new ScoreEntry(5, "Joueur 5", 3)
        );

        scoreTable.setItems(scores);
        scoreTable.widthProperty().addListener((obs, oldVal, newVal) -> {
            double tableWidth = newVal.doubleValue();
            rankColumn.setPrefWidth(tableWidth * 0.1);
            playerColumn.setPrefWidth(tableWidth * 0.80);
            scoreColumn.setPrefWidth(tableWidth * 0.10);
        });
    }


}
