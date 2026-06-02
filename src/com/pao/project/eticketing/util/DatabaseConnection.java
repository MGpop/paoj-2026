package com.pao.project.eticketing.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static DatabaseConnection instance;

    private Connection connection;

    private DatabaseConnection() {
        try {
            Properties properties = new Properties();

            try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("db.properties")) {

                if (input == null) {
                    throw new RuntimeException("Nu a fost găsit fișierul db.properties.");
                }

                properties.load(input);
            }

            String url = properties.getProperty("db.url");

            connection = DriverManager.getConnection(url);

        } catch (IOException | SQLException e) {
            throw new RuntimeException(
                    "Eroare la inițializarea conexiunii cu baza de date.", e
            );
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }

        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}