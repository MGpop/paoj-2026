package com.pao.project.eticketing.repository;

import com.pao.project.eticketing.model.Concert;
import com.pao.project.eticketing.model.Eveniment;
import com.pao.project.eticketing.model.Locatie;
import com.pao.project.eticketing.model.PiesaTeatru;
import com.pao.project.eticketing.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EvenimentRepository implements Repository<Eveniment, Integer> {
    private final Connection connection;
    private final LocatieRepository locatieRepository;

    public EvenimentRepository() {
        this.connection = DatabaseConnection
                .getInstance()
                .getConnection();

        this.locatieRepository = new LocatieRepository();
    }

    @Override
    public void save(Eveniment eveniment) {
        String sql = """
                INSERT INTO eveniment(
                    id,
                    nume,
                    data_eveniment,
                    locatie_id,
                    tip_eveniment,
                    gen_muzical,
                    autor,
                    regizor,
                    durata_minute
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, eveniment.getId());
            statement.setString(2, eveniment.getNume());
            statement.setString(3, eveniment.getData().toString());
            statement.setInt(4, eveniment.getLocatie().getId());
            statement.setString(5, eveniment.getTipEveniment());

            if (eveniment instanceof Concert concert) {
                statement.setString(6, concert.getGenMuzical());
                statement.setNull(7, Types.VARCHAR);
                statement.setNull(8, Types.VARCHAR);
                statement.setNull(9, Types.INTEGER);
            } else if (eveniment instanceof PiesaTeatru piesa) {
                statement.setNull(6, Types.VARCHAR);
                statement.setString(7, piesa.getAutor());
                statement.setString(8, piesa.getRegizor());
                statement.setInt(9, piesa.getDurataMinute());
            } else {
                statement.setNull(6, Types.VARCHAR);
                statement.setNull(7, Types.VARCHAR);
                statement.setNull(8, Types.VARCHAR);
                statement.setNull(9, Types.INTEGER);
            }

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea evenimentului.", e);
        }
    }

    @Override
    public Optional<Eveniment> findById(Integer id) {
        String sql = """
                SELECT *
                FROM eveniment
                WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToEveniment(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la căutarea evenimentului.", e);
        }

        return Optional.empty();
    }

    @Override
    public List<Eveniment> findAll() {
        String sql = """
                SELECT *
                FROM eveniment
                """;

        List<Eveniment> evenimente = new ArrayList<>();

        try (
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                evenimente.add(mapResultSetToEveniment(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea evenimentelor.", e);
        }

        return evenimente;
    }

    @Override
    public void update(Eveniment eveniment) {
        String sql = """
                UPDATE eveniment
                SET nume = ?,
                    data_eveniment = ?,
                    locatie_id = ?,
                    tip_eveniment = ?,
                    gen_muzical = ?,
                    autor = ?,
                    regizor = ?,
                    durata_minute = ?
                WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, eveniment.getNume());
            statement.setString(2, eveniment.getData().toString());
            statement.setInt(3, eveniment.getLocatie().getId());
            statement.setString(4, eveniment.getTipEveniment());

            if (eveniment instanceof Concert concert) {
                statement.setString(5, concert.getGenMuzical());
                statement.setNull(6, Types.VARCHAR);
                statement.setNull(7, Types.VARCHAR);
                statement.setNull(8, Types.INTEGER);
            } else if (eveniment instanceof PiesaTeatru piesa) {
                statement.setNull(5, Types.VARCHAR);
                statement.setString(6, piesa.getAutor());
                statement.setString(7, piesa.getRegizor());
                statement.setInt(8, piesa.getDurataMinute());
            } else {
                statement.setNull(5, Types.VARCHAR);
                statement.setNull(6, Types.VARCHAR);
                statement.setNull(7, Types.VARCHAR);
                statement.setNull(8, Types.INTEGER);
            }

            statement.setInt(9, eveniment.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea evenimentului.", e);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = """
                DELETE FROM eveniment
                WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la ștergerea evenimentului.", e);
        }
    }

    private Eveniment mapResultSetToEveniment(ResultSet resultSet)
            throws SQLException {

        int locatieId = resultSet.getInt("locatie_id");

        Locatie locatie = locatieRepository
                .findById(locatieId)
                .orElse(null);

        int id = resultSet.getInt("id");
        String nume = resultSet.getString("nume");
        LocalDateTime data = LocalDateTime.parse(
                resultSet.getString("data_eveniment")
        );
        String tipEveniment = resultSet.getString("tip_eveniment");

        if ("Concert".equalsIgnoreCase(tipEveniment)) {
            return new Concert(
                    id,
                    nume,
                    data,
                    locatie,
                    resultSet.getString("gen_muzical")
            );
        }

        return new PiesaTeatru(
                id,
                nume,
                data,
                locatie,
                resultSet.getString("autor"),
                resultSet.getString("regizor"),
                resultSet.getInt("durata_minute")
        );
    }

    public List<Eveniment> findAllSortateDupaData() {
        String sql = """
            SELECT *
            FROM eveniment
            ORDER BY data_eveniment
            """;

        List<Eveniment> evenimente = new ArrayList<>();

        try (
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                evenimente.add(mapResultSetToEveniment(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea evenimentelor sortate.", e);
        }

        return evenimente;
    }

    public List<Eveniment> cautaDupaFiltre(
            String tipEveniment,
            Integer locatieId,
            String data,
            Double pretMaxim
    ) {
        String sql = """
            SELECT DISTINCT e.*
            FROM eveniment e
            JOIN tip_bilet tb
                ON tb.eveniment_id = e.id
            WHERE (? IS NULL OR e.tip_eveniment = ?)
              AND (? IS NULL OR e.locatie_id = ?)
              AND (? IS NULL OR DATE(e.data_eveniment) = ?)
              AND (? IS NULL OR tb.pret <= ?)
            """;

        List<Eveniment> evenimente = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, tipEveniment);
            statement.setString(2, tipEveniment);

            if (locatieId == null) {
                statement.setNull(3, Types.INTEGER);
                statement.setNull(4, Types.INTEGER);
            } else {
                statement.setInt(3, locatieId);
                statement.setInt(4, locatieId);
            }

            statement.setString(5, data);
            statement.setString(6, data);

            if (pretMaxim == null) {
                statement.setNull(7, Types.DOUBLE);
                statement.setNull(8, Types.DOUBLE);
            } else {
                statement.setDouble(7, pretMaxim);
                statement.setDouble(8, pretMaxim);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    evenimente.add(mapResultSetToEveniment(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la căutarea evenimentelor după filtre.", e);
        }

        return evenimente;
    }
}