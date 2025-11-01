package com.simplerp.morpion.accueil;

// Contrôleur de la page de création de partie.
// Gère la sélection ou création des joueurs, la taille de la grille et le lancement du jeu.

import javafx.fxml.FXML;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Toggle;
import javafx.scene.control.Alert;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;
import com.simplerp.morpion.Db;
import com.simplerp.morpion.pageJeu.JeuApplication;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CreationController {

    // Liens avec les éléments FXML
    @FXML private TextField player1TextField;
    @FXML private TextField player2TextField;
    @FXML private ComboBox<String> player1ComboBox;
    @FXML private ComboBox<String> player2ComboBox;
    @FXML private ToggleGroup gridSizeGroup;

    // Initialise la page et charge les joueurs depuis la base de données
    @FXML
    public void initialize() {
        chargerJoueursComboBoxes();
        if (gridSizeGroup != null && gridSizeGroup.getSelectedToggle() == null && !gridSizeGroup.getToggles().isEmpty()) {
            gridSizeGroup.selectToggle(gridSizeGroup.getToggles().get(0));
        }
    }

    // Démarre une nouvelle partie
    @FXML
    public void onStartGame(ActionEvent event) {
        // Récupérer les noms des joueurs
        String nomJoueur1 = obtenirNomJoueur(player1TextField, player1ComboBox);
        String nomJoueur2 = obtenirNomJoueur(player2TextField, player2ComboBox);

        if (nomJoueur1 == null || nomJoueur1.isBlank() || nomJoueur2 == null || nomJoueur2.isBlank()) {
            afficherAlerte("Erreur", "Veuillez entrer les noms des deux joueurs !");
            return;
        }

        int tailleGrille = obtenirTailleGrilleSelectionnee();

        // Sauvegarder les joueurs s'ils n'existent pas
        sauvegarderJoueurSiNonExistant(nomJoueur1);
        sauvegarderJoueurSiNonExistant(nomJoueur2);

        // Récupérer les IDs des joueurs
        int idJoueur1 = obtenirIdJoueur(nomJoueur1);
        int idJoueur2 = obtenirIdJoueur(nomJoueur2);

        if (idJoueur1 == -1 || idJoueur2 == -1) {
            afficherAlerte("Erreur", "Impossible de récupérer les joueurs.");
            return;
        }

        // Vérifier si une partie est déjà en cours
        if (partieEnCours()) {
            afficherAlerte("Partie en cours", "Une partie est déjà en cours. Veuillez la terminer avant d'en commencer une nouvelle.");
            return;
        }

        // Créer une nouvelle partie
        if (creerNouvellePartie(idJoueur1, idJoueur2)) {
            // Stocker les infos de la partie pour JeuApplication
            JeuApplication.nomJoueur1 = nomJoueur1;
            JeuApplication.nomJoueur2 = nomJoueur2;
            JeuApplication.tailleGrille = tailleGrille;
            JeuApplication.idJoueur1 = idJoueur1;
            JeuApplication.idJoueur2 = idJoueur2;

            // Fermer la fenêtre actuelle
            Stage stageActuel = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stageActuel.close();

            // Lancer JeuApplication
            try {
                JeuApplication jeuApp = new JeuApplication();
                jeuApp.start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
                afficherAlerte("Erreur", "Impossible de lancer le jeu.");
            }
        } else {
            afficherAlerte("Erreur", "Impossible de créer la partie.");
        }
    }

    private boolean partieEnCours() {
        String requete = "SELECT COUNT(*) FROM games_in_progress";
        try (Connection conn = Db.get();
             PreparedStatement ps = conn.prepareStatement(requete);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean creerNouvellePartie(int idJoueur1, int idJoueur2) {
        // Vider la table games_in_progress
        String suppression = "DELETE FROM games_in_progress";
        String insertion = "INSERT INTO games_in_progress (id_player1, id_player2, score_j1, score_j2) VALUES (?, ?, 0, 0)";

        try (Connection conn = Db.get()) {
            // Supprimer les anciennes parties
            try (PreparedStatement psSuppression = conn.prepareStatement(suppression)) {
                psSuppression.executeUpdate();
            }

            // Créer la nouvelle partie
            try (PreparedStatement psInsertion = conn.prepareStatement(insertion)) {
                psInsertion.setInt(1, idJoueur1);
                psInsertion.setInt(2, idJoueur2);
                psInsertion.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private int obtenirIdJoueur(String nom) {
        String requete = "SELECT id_player FROM players WHERE name = ?";
        try (Connection conn = Db.get();
             PreparedStatement ps = conn.prepareStatement(requete)) {
            ps.setString(1, nom.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_player");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private void afficherAlerte(String titre, String message) {
        Alert alerte = new Alert(Alert.AlertType.WARNING);
        alerte.setTitle(titre);
        alerte.setHeaderText(null);
        alerte.setContentText(message);
        alerte.showAndWait();
    }

    private int obtenirTailleGrilleSelectionnee() {
        if (gridSizeGroup == null) return 3;
        Toggle selected = gridSizeGroup.getSelectedToggle();
        if (selected != null && selected.getUserData() != null) {
            try { return Integer.parseInt(selected.getUserData().toString()); }
            catch (NumberFormatException ignored) {}
        }
        return 3;
    }

    private String obtenirNomJoueur(TextField tf, ComboBox<String> cb) {
        String texte = (tf != null) ? tf.getText().trim() : "";
        if (texte != null && !texte.isBlank()) return texte;
        if (cb != null && cb.getValue() != null) return cb.getValue().toString();
        return null;
    }

    private void chargerJoueursComboBoxes() {
        List<String> joueurs = obtenirJoueursDepuisBaseDeDonnees();
        ObservableList<String> items = FXCollections.observableArrayList(joueurs);
        player1ComboBox.setItems(items);
        player2ComboBox.setItems(items);
    }

    private List<String> obtenirJoueursDepuisBaseDeDonnees() {
        List<String> joueurs = new ArrayList<>();
        String requete = "SELECT name FROM players ORDER BY name COLLATE NOCASE";
        try (Connection conn = Db.get();
             PreparedStatement ps = conn.prepareStatement(requete);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) joueurs.add(rs.getString("name"));
        } catch (SQLException e) { e.printStackTrace(); }
        return joueurs;
    }

    private void sauvegarderJoueurSiNonExistant(String nom) {
        if (nom == null || nom.isBlank()) return;
        String insertion = "INSERT INTO players(name) SELECT ? WHERE NOT EXISTS(SELECT 1 FROM players WHERE name = ?)";
        try (Connection conn = Db.get();
             PreparedStatement ps = conn.prepareStatement(insertion)) {
            ps.setString(1, nom.trim());
            ps.setString(2, nom.trim());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}