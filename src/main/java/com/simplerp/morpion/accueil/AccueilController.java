package com.simplerp.morpion.accueil;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;


public class AccueilController {

        @FXML
        public void initialize() {
            System.out.println("AccueilleController initialisé");
        }

        @FXML
        public void onJouer(ActionEvent event) {
            System.out.println("Bouton Jouer cliqué !");
            String fxmlPath = "/com/simplerp/morpion/PageCreation.fxml";
            try {
                URL fxmlUrl = getClass().getResource(fxmlPath);
                if (fxmlUrl == null) {
                    System.err.println("PageCreation.fxml introuvable : " + fxmlPath);
                    return;
                }
                Parent creationRoot = FXMLLoader.load(fxmlUrl);
                Scene creationScene = new Scene(creationRoot);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                URL cssCommon = getClass().getResource("/com/simplerp/morpion/CSS/styleAccueille.css");
                URL cssCreation = getClass().getResource("/com/simplerp/morpion/CSS/styleCreation.css");
                if (cssCommon != null) creationScene.getStylesheets().add(cssCommon.toExternalForm());
                if (cssCreation != null) creationScene.getStylesheets().add(cssCreation.toExternalForm());
                stage.setScene(creationScene);
                stage.setTitle("Morpion - Créer une partie");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    @FXML
    public void onScores(ActionEvent event) {
        try {
            // Fermer la fenêtre actuelle
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            // Lancer scoreApplication
            com.simplerp.morpion.score.scoreApplication scoreApp = new com.simplerp.morpion.score.scoreApplication();
            scoreApp.start(new Stage());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }