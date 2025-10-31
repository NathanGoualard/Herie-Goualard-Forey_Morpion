package com.simplerp.morpion.accueil;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class CreationApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        String fxmlPath = "/com/simplerp/morpion/PageCreation.fxml";
        String cssCommon = "/com/simplerp/morpion/CSS/styleAccueille.css";
        String cssCreation = "/com/simplerp/morpion/CSS/styleCreation.css";

        URL fxmlUrl = getClass().getResource(fxmlPath);
        if (fxmlUrl == null) {
            throw new IllegalStateException("FXML file not found: " + fxmlPath);
        }
        Parent root = FXMLLoader.load(fxmlUrl);
        primaryStage.setResizable(false);

        Scene scene = new Scene(root, 800, 500);

        URL cssUrl1 = getClass().getResource(cssCommon);
        URL cssUrl2 = getClass().getResource(cssCreation);

        if (cssUrl1 != null) scene.getStylesheets().add(cssUrl1.toExternalForm());
        if (cssUrl2 != null) scene.getStylesheets().add(cssUrl2.toExternalForm());

        primaryStage.setTitle("Morpion - Créer une partie");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}