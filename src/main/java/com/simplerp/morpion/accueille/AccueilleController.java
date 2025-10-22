package com.simplerp.morpion.accueille;

import javafx.event.ActionEvent;

public class AccueilleController {

    public void onJoueur1(ActionEvent event) {
        System.out.println("Bouton 1 Joueur cliqué !");
    }

    public void onJoueur2(ActionEvent event) {
        System.out.println("Bouton 2 Joueurs cliqué !");
    }

    public void onScores(ActionEvent event) {
        System.out.println("Bouton Scores cliqué !");
    }
}