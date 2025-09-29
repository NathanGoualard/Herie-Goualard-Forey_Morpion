package com.simplerp.morpion;

import java.sql.*;


public class Db {
    private static final String URL = "jdbc:sqlite:data/app.db";

    static {
        try (Connection c = get();
             Statement st = c.createStatement()) {

            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS players (
                    id_player INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL
                )
            """);

            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS scores (
                    id_score INTEGER PRIMARY KEY AUTOINCREMENT,
                    nb_victories INTEGER,
                    grid_size INTEGER,
                    id_player INTEGER NOT NULL,
                    FOREIGN KEY(id_player) REFERENCES players(id_player)
                )
            """);

            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS games_in_progress (
                    id_game INTEGER PRIMARY KEY AUTOINCREMENT,
                    score_j1 INTEGER,
                    score_j2 INTEGER,
                    id_player1 INTEGER NOT NULL,
                    id_player2 INTEGER NOT NULL,
                    FOREIGN KEY(id_player1) REFERENCES players(id_player),
                    FOREIGN KEY(id_player2) REFERENCES players(id_player),
                    UNIQUE(id_player1, id_player2)
                )
            """);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection get() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
