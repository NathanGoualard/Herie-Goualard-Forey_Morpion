package com.simplerp.morpion.score;

import com.simplerp.morpion.Db;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScoreDAO {

    public static List<ScoreEntry> getAllScores() {
        List<ScoreEntry> scores = new ArrayList<>();
        String sql = """
            SELECT p.name, s.nb_victories
            FROM scores s
            JOIN players p ON s.id_player = p.id_player
            ORDER BY s.nb_victories DESC
        """;

        try (Connection conn = Db.get();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            int rank = 1;
            while (rs.next()) {
                String playerName = rs.getString("name");
                int victories = rs.getInt("nb_victories");
                scores.add(new ScoreEntry(rank, playerName, victories));
                rank++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return scores;
    }
}
