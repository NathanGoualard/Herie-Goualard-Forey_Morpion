package com.simplerp.morpion.accueil;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Toggle;
import javafx.event.ActionEvent;
import com.simplerp.morpion.Db;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CreationController {

    @FXML private TextField player1TextField;
    @FXML private TextField player2TextField;
    @FXML private ComboBox<String> player1ComboBox;
    @FXML private ComboBox<String> player2ComboBox;
    @FXML private ToggleGroup gridSizeGroup;

    @FXML
    public void initialize() {
        System.out.println("CreationController initialisé");
        loadPlayersToComboBoxes();
        if (gridSizeGroup != null && gridSizeGroup.getSelectedToggle() == null && !gridSizeGroup.getToggles().isEmpty()) {
            gridSizeGroup.selectToggle(gridSizeGroup.getToggles().get(0));
        }
    }

    @FXML
    public void onStartGame(ActionEvent event) {
        String name1 = getPlayerName(player1TextField, player1ComboBox);
        String name2 = getPlayerName(player2TextField, player2ComboBox);
        if (name1 == null || name1.isBlank() || name2 == null || name2.isBlank()) {
            System.out.println("Veuillez renseigner un nom pour les deux joueurs.");
            return;
        }
        int gridSize = getSelectedGridSize();
        System.out.println("Démarrage : " + name1 + " vs " + name2 + " — grille " + gridSize + "x" + gridSize);
        savePlayerIfNotExists(name1);
        savePlayerIfNotExists(name2);
    }

    private int getSelectedGridSize() {
        if (gridSizeGroup == null) return 3;
        Toggle selected = gridSizeGroup.getSelectedToggle();
        if (selected != null && selected.getUserData() != null) {
            try { return Integer.parseInt(selected.getUserData().toString()); }
            catch (NumberFormatException ignored) {}
        }
        return 3;
    }

    // autres méthodes (getPlayerName, db access...) inchangées — réutilise celles que tu as déjà
    private String getPlayerName(TextField tf, ComboBox<String> cb) {
        String text = (tf != null) ? tf.getText().trim() : "";
        if (text != null && !text.isBlank()) return text;
        if (cb != null && cb.getValue() != null) return cb.getValue().toString();
        return null;
    }

    private void loadPlayersToComboBoxes() {
        List<String> players = getPlayersFromDatabase();
        ObservableList<String> items = FXCollections.observableArrayList(players);
        player1ComboBox.setItems(items);
        player2ComboBox.setItems(items);
    }

    private List<String> getPlayersFromDatabase() {
        List<String> players = new ArrayList<>();
        String query = "SELECT name FROM players ORDER BY name COLLATE NOCASE";
        try (Connection conn = Db.get();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) players.add(rs.getString("name"));
        } catch (SQLException e) { e.printStackTrace(); }
        return players;
    }

    private void savePlayerIfNotExists(String name) {
        if (name == null || name.isBlank()) return;
        String insert = "INSERT INTO players(name) SELECT ? WHERE NOT EXISTS(SELECT 1 FROM players WHERE name = ?)";
        try (Connection conn = Db.get();
             PreparedStatement ps = conn.prepareStatement(insert)) {
            ps.setString(1, name.trim());
            ps.setString(2, name.trim());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}