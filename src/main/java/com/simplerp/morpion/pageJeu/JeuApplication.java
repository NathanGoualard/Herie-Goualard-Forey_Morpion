package com.simplerp.morpion.pageJeu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JeuApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/simplerp/morpion/PageJeu.fxml"));
        Parent root = loader.load();
        JeuController controller = loader.getController();

        Scene scene = new Scene(root, 700, 700);
        scene.getStylesheets().add(getClass().getResource("/com/simplerp/morpion/CSS/StyleScore.css").toExternalForm());

        stage.setResizable(false);
        stage.setTitle("Jeu");
        stage.setScene(scene);

        stage.setOnCloseRequest(event -> {
            if (controller != null) {
                controller.sauvegarderPartieEnCours();
            }
        });

        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}