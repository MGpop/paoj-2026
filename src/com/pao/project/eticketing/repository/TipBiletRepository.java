package com.pao.project.eticketing.repository;

import com.pao.project.eticketing.model.TipBilet;
import com.pao.project.eticketing.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TipBiletRepository implements Repository<TipBilet, Integer> {
    private final Connection connection;

    public TipBiletRepository() {
        this.connection = DatabaseConnection
                .getInstance()
                .getConnection();
    }

    public void save(TipBilet tipBilet, int evenimentId) {
        String sql = """
                INSERT INTO tip_bilet(id, nume, pret, stoc_disponibil, eveniment_id)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, tipBilet.getId());
            statement.setString(2, tipBilet.getNume());
            statement.setDouble(3, tipBilet.getPret());
            statement.setInt(4, tipBilet.getStocDisponibil());
            statement.setInt(5, evenimentId);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea tipului de bilet.", e);
        }
    }

    @Override
    public void save(TipBilet tipBilet) {
        throw new UnsupportedOperationException(
                "Folosește save(TipBilet tipBilet, int evenimentId)."
        );
    }

    @Override
    public Optional<TipBilet> findById(Integer id) {
        String sql = """
                SELECT *
                FROM tip_bilet
                WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToTipBilet(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la căutarea tipului de bilet.", e);
        }

        return Optional.empty();
    }

    public List<TipBilet> findByEvenimentId(int evenimentId) {
        String sql = """
                SELECT *
                FROM tip_bilet
                WHERE eveniment_id = ?
                """;

        List<TipBilet> tipuriBilete = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, evenimentId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    tipuriBilete.add(mapResultSetToTipBilet(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Eroare la listarea tipurilor de bilete pentru eveniment.",
                    e
            );
        }

        return tipuriBilete;
    }

    @Override
    public List<TipBilet> findAll() {
        String sql = """
                SELECT *
                FROM tip_bilet
                """;

        List<TipBilet> tipuriBilete = new ArrayList<>();

        try (
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                tipuriBilete.add(mapResultSetToTipBilet(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea tipurilor de bilete.", e);
        }

        return tipuriBilete;
    }

    @Override
    public void update(TipBilet tipBilet) {
        String sql = """
                UPDATE tip_bilet
                SET nume = ?,
                    pret = ?,
                    stoc_disponibil = ?
                WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, tipBilet.getNume());
            statement.setDouble(2, tipBilet.getPret());
            statement.setInt(3, tipBilet.getStocDisponibil());
            statement.setInt(4, tipBilet.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea tipului de bilet.", e);
        }
    }

    public void updateStoc(int tipBiletId, int stocNou) {
        String sql = """
                UPDATE tip_bilet
                SET stoc_disponibil = ?
                WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, stocNou);
            statement.setInt(2, tipBiletId);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea stocului.", e);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = """
                DELETE FROM tip_bilet
                WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la ștergerea tipului de bilet.", e);
        }
    }

    private TipBilet mapResultSetToTipBilet(ResultSet resultSet)
            throws SQLException {

        return new TipBilet(
                resultSet.getInt("id"),
                resultSet.getString("nume"),
                resultSet.getDouble("pret"),
                resultSet.getInt("stoc_disponibil")
        );
    }
}