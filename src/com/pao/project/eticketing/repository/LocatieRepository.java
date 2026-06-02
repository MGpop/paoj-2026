package com.pao.project.eticketing.repository;

import com.pao.project.eticketing.model.Adresa;
import com.pao.project.eticketing.model.Locatie;
import com.pao.project.eticketing.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LocatieRepository implements Repository<Locatie, Integer> {
    private final Connection connection;

    public LocatieRepository() {
        this.connection = DatabaseConnection
                .getInstance()
                .getConnection();
    }

    @Override
    public void save(Locatie locatie) {
        String sql = """
                INSERT INTO locatie(id, nume, strada, numar, oras, judet, capacitate)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, locatie.getId());
            statement.setString(2, locatie.getNume());
            statement.setString(3, locatie.getAdresa().getStrada());
            statement.setString(4, locatie.getAdresa().getNumar());
            statement.setString(5, locatie.getAdresa().getOras());
            statement.setString(6, locatie.getAdresa().getJudet());
            statement.setInt(7, locatie.getCapacitate());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea locației.", e);
        }
    }

    @Override
    public Optional<Locatie> findById(Integer id) {
        String sql = """
                SELECT *
                FROM locatie
                WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToLocatie(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la căutarea locației.", e);
        }

        return Optional.empty();
    }

    @Override
    public List<Locatie> findAll() {
        String sql = """
                SELECT *
                FROM locatie
                """;

        List<Locatie> locatii = new ArrayList<>();

        try (
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                locatii.add(mapResultSetToLocatie(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea locațiilor.", e);
        }

        return locatii;
    }

    @Override
    public void update(Locatie locatie) {
        String sql = """
                UPDATE locatie
                SET nume = ?,
                    strada = ?,
                    numar = ?,
                    oras = ?,
                    judet = ?,
                    capacitate = ?
                WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, locatie.getNume());
            statement.setString(2, locatie.getAdresa().getStrada());
            statement.setString(3, locatie.getAdresa().getNumar());
            statement.setString(4, locatie.getAdresa().getOras());
            statement.setString(5, locatie.getAdresa().getJudet());
            statement.setInt(6, locatie.getCapacitate());
            statement.setInt(7, locatie.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea locației.", e);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = """
                DELETE FROM locatie
                WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la ștergerea locației.", e);
        }
    }

    private Locatie mapResultSetToLocatie(ResultSet resultSet)
            throws SQLException {

        Adresa adresa = new Adresa(
                resultSet.getString("strada"),
                resultSet.getString("numar"),
                resultSet.getString("oras"),
                resultSet.getString("judet")
        );

        return new Locatie(
                resultSet.getInt("id"),
                resultSet.getString("nume"),
                adresa,
                resultSet.getInt("capacitate")
        );
    }

    public List<Locatie> cautaDupaFiltre(String oras, String tipEveniment) {
        String sql = """
            SELECT DISTINCT l.*
            FROM locatie l
            LEFT JOIN eveniment e
                ON e.locatie_id = l.id
            WHERE (? IS NULL OR l.oras = ?)
              AND (? IS NULL OR e.tip_eveniment = ?)
            """;

        List<Locatie> locatii = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, oras);
            statement.setString(2, oras);
            statement.setString(3, tipEveniment);
            statement.setString(4, tipEveniment);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    locatii.add(mapResultSetToLocatie(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la căutarea locațiilor după filtre.", e);
        }

        return locatii;
    }
}