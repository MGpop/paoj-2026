package com.pao.project.eticketing.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    private DatabaseInitializer() {
    }

    public static void initialize() {
        Connection connection = DatabaseConnection
                .getInstance()
                .getConnection();

        try (InputStream inputStream =
                     DatabaseInitializer.class
                             .getClassLoader()
                             .getResourceAsStream("schema.sql")) {

            if (inputStream == null) {
                throw new RuntimeException("Nu a fost găsit fișierul schema.sql.");
            }

            String schema = readSqlFile(inputStream);
            String[] statements = schema.split(";");

            try (Statement statement = connection.createStatement()) {
                for (String sql : statements) {
                    String cleanedSql = sql.trim();

                    if (!cleanedSql.isEmpty()) {
                        statement.execute(cleanedSql);
                    }
                }
            }

        } catch (IOException | SQLException e) {
            throw new RuntimeException(
                    "Eroare la inițializarea bazei de date.",
                    e
            );
        }
    }

    private static String readSqlFile(InputStream inputStream)
            throws IOException {

        StringBuilder builder = new StringBuilder();

        try (BufferedReader reader =
                     new BufferedReader(
                             new InputStreamReader(inputStream))) {

            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
        }

        return builder.toString();
    }
}