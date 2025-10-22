package com.simplerp.morpion.score;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import com.simplerp.morpion.score.ScoreDAO;

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


        ObservableList<ScoreEntry> scores = FXCollections.observableArrayList(ScoreDAO.getAllScores());
        scoreTable.setItems(scores);


        scoreTable.widthProperty().addListener((obs, oldVal, newVal) -> {
            double tableWidth = newVal.doubleValue();
            rankColumn.setPrefWidth(tableWidth * 0.1);
            playerColumn.setPrefWidth(tableWidth * 0.79);
            scoreColumn.setPrefWidth(tableWidth * 0.10);
        });
    }
}
