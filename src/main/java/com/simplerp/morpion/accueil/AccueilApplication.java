package com.simplerp.morpion.accueil;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.util.Objects;

public class AccueilApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/simplerp/morpion/PageAccueil.fxml")));
        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add(getClass().getResource("/com/simplerp/morpion/CSS/styleAccueil.css").toExternalForm());
        primaryStage.setTitle("Morpion - Accueil");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}