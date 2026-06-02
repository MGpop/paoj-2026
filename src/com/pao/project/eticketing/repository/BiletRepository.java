package com.pao.project.eticketing.repository;

import com.pao.project.eticketing.model.Bilet;
import com.pao.project.eticketing.model.Client;
import com.pao.project.eticketing.model.Eveniment;
import com.pao.project.eticketing.model.TipBilet;
import com.pao.project.eticketing.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BiletRepository implements Repository<Bilet, String> {
    private final Connection connection;

    private final ClientRepository clientRepository;
    private final TipBiletRepository tipBiletRepository;
    private final EvenimentRepository evenimentRepository;

    public BiletRepository() {
        this.connection = DatabaseConnection
                .getInstance()
                .getConnection();

        this.clientRepository = new ClientRepository();
        this.tipBiletRepository = new TipBiletRepository();
        this.evenimentRepository = new EvenimentRepository();
    }

    @Override
    public void save(Bilet bilet) {
        String sql = """
                INSERT INTO bilet(cod_bilet, client_id, tip_bilet_id, data_cumparare)
                VALUES (?, ?, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, bilet.getCodBilet());
            statement.setInt(2, bilet.getClient().getId());
            statement.setInt(3, bilet.getTipBilet().getId());
            statement.setString(4, bilet.getDataCumparare().toString());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea biletului.", e);
        }
    }

    public void saveCuTranzactie(Bilet bilet) {
        String insertBiletSql = """
                INSERT INTO bilet(cod_bilet, client_id, tip_bilet_id, data_cumparare)
                VALUES (?, ?, ?, ?)
                """;

        String updateStocSql = """
                UPDATE tip_bilet
                SET stoc_disponibil = stoc_disponibil - 1
                WHERE id = ? AND stoc_disponibil > 0
                """;

        try {
            connection.setAutoCommit(false);

            try (
                    PreparedStatement insertBiletStatement =
                            connection.prepareStatement(insertBiletSql);
                    PreparedStatement updateStocStatement =
                            connection.prepareStatement(updateStocSql)
            ) {
                insertBiletStatement.setString(1, bilet.getCodBilet());
                insertBiletStatement.setInt(2, bilet.getClient().getId());
                insertBiletStatement.setInt(3, bilet.getTipBilet().getId());
                insertBiletStatement.setString(4, bilet.getDataCumparare().toString());

                insertBiletStatement.executeUpdate();

                updateStocStatement.setInt(1, bilet.getTipBilet().getId());

                int randuriModificate = updateStocStatement.executeUpdate();

                if (randuriModificate == 0) {
                    throw new SQLException("Stoc insuficient pentru tipul de bilet ales.");
                }

                connection.commit();

            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Eroare la salvarea biletului în tranzacție.",
                    e
            );
        }
    }

    @Override
    public Optional<Bilet> findById(String codBilet) {
        String sql = """
                SELECT
                    b.cod_bilet,
                    b.client_id,
                    b.tip_bilet_id,
                    b.data_cumparare,
                    tb.eveniment_id
                FROM bilet b
                JOIN tip_bilet tb
                    ON b.tip_bilet_id = tb.id
                WHERE b.cod_bilet = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, codBilet);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToBilet(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la căutarea biletului.", e);
        }

        return Optional.empty();
    }

    @Override
    public List<Bilet> findAll() {
        String sql = """
                SELECT
                    b.cod_bilet,
                    b.client_id,
                    b.tip_bilet_id,
                    b.data_cumparare,
                    tb.eveniment_id
                FROM bilet b
                JOIN tip_bilet tb
                    ON b.tip_bilet_id = tb.id
                """;

        List<Bilet> bilete = new ArrayList<>();

        try (
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                bilete.add(mapResultSetToBilet(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea biletelor.", e);
        }

        return bilete;
    }

    public List<Bilet> findByClientId(int clientId) {
        String sql = """
                SELECT
                    b.cod_bilet,
                    b.client_id,
                    b.tip_bilet_id,
                    b.data_cumparare,
                    tb.eveniment_id
                FROM bilet b
                JOIN tip_bilet tb
                    ON b.tip_bilet_id = tb.id
                WHERE b.client_id = ?
                """;

        List<Bilet> bilete = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, clientId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    bilete.add(mapResultSetToBilet(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Eroare la listarea biletelor clientului.",
                    e
            );
        }

        return bilete;
    }

    @Override
    public void update(Bilet bilet) {
        String sql = """
                UPDATE bilet
                SET client_id = ?,
                    tip_bilet_id = ?,
                    data_cumparare = ?
                WHERE cod_bilet = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bilet.getClient().getId());
            statement.setInt(2, bilet.getTipBilet().getId());
            statement.setString(3, bilet.getDataCumparare().toString());
            statement.setString(4, bilet.getCodBilet());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea biletului.", e);
        }
    }

    @Override
    public void delete(String codBilet) {
        String sql = """
                DELETE FROM bilet
                WHERE cod_bilet = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, codBilet);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la ștergerea biletului.", e);
        }
    }

    private Bilet mapResultSetToBilet(ResultSet resultSet)
            throws SQLException {

        int clientId = resultSet.getInt("client_id");
        int tipBiletId = resultSet.getInt("tip_bilet_id");
        int evenimentId = resultSet.getInt("eveniment_id");

        Client client = clientRepository
                .findById(clientId)
                .orElse(null);

        TipBilet tipBilet = tipBiletRepository
                .findById(tipBiletId)
                .orElse(null);

        Eveniment eveniment = evenimentRepository
                .findById(evenimentId)
                .orElse(null);

        return new Bilet(
                resultSet.getString("cod_bilet"),
                client,
                eveniment,
                tipBilet
        );
    }
}