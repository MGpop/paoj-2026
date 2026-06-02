package com.pao.project.eticketing.repository;

import com.pao.project.eticketing.model.Client;
import com.pao.project.eticketing.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientRepository implements Repository<Client, Integer> {

    private final Connection connection;

    public ClientRepository() {
        this.connection = DatabaseConnection
                .getInstance()
                .getConnection();
    }

    @Override
    public void save(Client client) {
        String sql = """
                INSERT INTO client(id, nume, email, parola)
                VALUES (?, ?, ?, ?)
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, client.getId());
            statement.setString(2, client.getNume());
            statement.setString(3, client.getEmail());
            statement.setString(4, client.getParola());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Eroare la salvarea clientului.",
                    e
            );
        }
    }

    @Override
    public Optional<Client> findById(Integer id) {
        String sql = """
                SELECT *
                FROM client
                WHERE id = ?
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    Client client = mapResultSetToClient(resultSet);
                    return Optional.of(client);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Eroare la căutarea clientului.",
                    e
            );
        }

        return Optional.empty();
    }

    @Override
    public List<Client> findAll() {
        String sql = """
                SELECT *
                FROM client
                """;

        List<Client> clienti = new ArrayList<>();

        try (PreparedStatement statement =
                     connection.prepareStatement(sql);

             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {
                clienti.add(mapResultSetToClient(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Eroare la listarea clienților.",
                    e
            );
        }

        return clienti;
    }

    @Override
    public void update(Client client) {
        String sql = """
                UPDATE client
                SET nume = ?,
                    email = ?,
                    parola = ?
                WHERE id = ?
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, client.getNume());
            statement.setString(2, client.getEmail());
            statement.setString(3, client.getParola());
            statement.setInt(4, client.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Eroare la actualizarea clientului.",
                    e
            );
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = """
                DELETE FROM client
                WHERE id = ?
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Eroare la ștergerea clientului.",
                    e
            );
        }
    }

    private Client mapResultSetToClient(ResultSet resultSet)
            throws SQLException {

        return new Client(
                resultSet.getInt("id"),
                resultSet.getString("nume"),
                resultSet.getString("email"),
                resultSet.getString("parola")
        );
    }

    public Optional<Client> autentifica(String email, String parola) {
        String sql = """
            SELECT *
            FROM client
            WHERE email = ? AND parola = ?
            """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setString(2, parola);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToClient(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la autentificarea clientului.", e);
        }

        return Optional.empty();
    }
}