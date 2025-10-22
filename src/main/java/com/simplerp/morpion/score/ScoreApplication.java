package com.simplerp.morpion.score;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

public class ScoreApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/simplerp/morpion/PageScore.fxml"));
        Scene scene = new Scene(root, 500, 450);
        scene.getStylesheets().add(getClass().getResource("/com/simplerp/morpion/CSS/StyleScore.css").toExternalForm());

        primaryStage.setTitle("Scores");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
