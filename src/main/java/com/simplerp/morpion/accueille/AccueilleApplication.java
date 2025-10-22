package com.simplerp.morpion.accueille;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class AccueilleApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/simplerp/morpion/PageAccueille.fxml"));
        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add(getClass().getResource("/com/simplerp/morpion/CSS/styleAccueille.css").toExternalForm());
        primaryStage.setTitle("Morpion - Accueil");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}