package com.simplerp.morpion.score;

import javafx.beans.property.*;

public class ScoreEntry {
    private final IntegerProperty rank;
    private final StringProperty player;
    private final IntegerProperty score;

    public ScoreEntry(int rank, String player, int score) {
        this.rank = new SimpleIntegerProperty(rank);
        this.player = new SimpleStringProperty(player);
        this.score = new SimpleIntegerProperty(score);
    }

    public IntegerProperty rankProperty() { return rank; }
    public StringProperty playerProperty() { return player; }
    public IntegerProperty scoreProperty() { return score; }
}
