package com.simplerp.morpion.pageJeu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JeuApplication extends Application {

    // Variables statiques pour stocker les infos de la partie
    public static String nomJoueur1;
    public static String nomJoueur2;
    public static int tailleGrille;
    public static int idJoueur1;
    public static int idJoueur2;

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/simplerp/morpion/PageJeu.fxml"));
        Parent root = loader.load();
        JeuController controller = loader.getController();

        // Passer les infos au controller si nécessaire
        if (controller != null) {
            controller.initialiserPartie(nomJoueur1, nomJoueur2, tailleGrille, idJoueur1, idJoueur2);
        }

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