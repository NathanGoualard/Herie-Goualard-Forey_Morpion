package com.simplerp.morpion.score;

// Classe modèle représentant une ligne du tableau des scores.
// Contient le rang du joueur, son nom et son nombre de victoires.
// Utilise les propriétés JavaFX pour permettre la liaison avec la TableView.

import javafx.beans.property.*;

public class ScoreEntry {

    private final IntegerProperty rank;
    private final StringProperty player;
    private final IntegerProperty score;

    // Constructeur : initialise les propriétés avec les valeurs données
    public ScoreEntry(int rank, String player, int score) {
        this.rank = new SimpleIntegerProperty(rank);
        this.player = new SimpleStringProperty(player);
        this.score = new SimpleIntegerProperty(score);
    }

    // Accès aux propriétés utilisées par la TableView
    public IntegerProperty rankProperty() { return rank; }
    public StringProperty playerProperty() { return player; }
    public IntegerProperty scoreProperty() { return score; }
}
