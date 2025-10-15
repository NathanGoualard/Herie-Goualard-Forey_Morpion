package com.simplerp.morpion.accueille;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AccueilleController {

    @FXML
    private Button bouton1Joueur;

    @FXML
    private Button bouton2Joueurs;

    @FXML
    private Button boutonScores;

    @FXML
    private void lancer1Joueur() {
        System.out.println("Page 1 Joueur !");
        // TODO: charger une autre scène ou fenêtre
    }

    @FXML
    private void lancer2Joueurs() {
        System.out.println("Page 2 Joueurs !");
        // TODO: charger une autre scène ou fenêtre
    }

    @FXML
    private void afficherScores() {
        System.out.println("Page Scores !");
        // TODO: charger une autre scène ou fenêtre
    }
}