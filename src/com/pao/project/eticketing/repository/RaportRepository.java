package com.pao.project.eticketing.repository;

import com.pao.project.eticketing.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RaportRepository {
    private final Connection connection;

    public RaportRepository() {
        this.connection = DatabaseConnection
                .getInstance()
                .getConnection();
    }

    /// Afișați detalii despre toate biletele cumpărate și despre cumpărătorii și evenimentele asociate acestora.
    public void afiseazaBileteCuDetalii() {
        String sql = """
                SELECT
                    b.cod_bilet,
                    c.nume AS nume_client,
                    e.nume AS nume_eveniment,
                    tb.nume AS tip_bilet,
                    tb.pret,
                    b.data_cumparare
                FROM bilet b
                JOIN client c
                    ON b.client_id = c.id
                JOIN tip_bilet tb
                    ON b.tip_bilet_id = tb.id
                JOIN eveniment e
                    ON tb.eveniment_id = e.id
                """;

        try (
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                System.out.println(
                        "Bilet{" +
                                "cod='" + resultSet.getString("cod_bilet") + '\'' +
                                ", client='" + resultSet.getString("nume_client") + '\'' +
                                ", eveniment='" + resultSet.getString("nume_eveniment") + '\'' +
                                ", tipBilet='" + resultSet.getString("tip_bilet") + '\'' +
                                ", pret=" + resultSet.getDouble("pret") +
                                ", dataCumparare=" + resultSet.getString("data_cumparare") +
                                '}'
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Eroare la afișarea biletelor cu detalii.",
                    e
            );
        }
    }

    /// Afișați toate evenimentele împreună cu orașul și capacitatea locației.
    public void afiseazaEvenimenteCuLocatii() {
        String sql = """
                SELECT
                    e.id,
                    e.nume AS nume_eveniment,
                    e.tip_eveniment,
                    e.data_eveniment,
                    l.nume AS nume_locatie,
                    l.oras,
                    l.capacitate
                FROM eveniment e
                JOIN locatie l
                    ON e.locatie_id = l.id
                """;

        try (
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                System.out.println(
                        "EvenimentLocatie{" +
                                "id=" + resultSet.getInt("id") +
                                ", eveniment='" + resultSet.getString("nume_eveniment") + '\'' +
                                ", tip='" + resultSet.getString("tip_eveniment") + '\'' +
                                ", data=" + resultSet.getString("data_eveniment") +
                                ", locatie='" + resultSet.getString("nume_locatie") + '\'' +
                                ", oras='" + resultSet.getString("oras") + '\'' +
                                ", capacitate=" + resultSet.getInt("capacitate") +
                                '}'
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Eroare la afișarea evenimentelor cu locații.",
                    e
            );
        }
    }

    /// Calculați ce venit a fost obținut în urma vânzării biletelor pentru fiecare eveniment.
    public void afiseazaVenituriPeEvenimente() {
        String sql = """
                SELECT
                    e.id,
                    e.nume AS nume_eveniment,
                    COUNT(b.cod_bilet) AS numar_bilete_vandute,
                    SUM(tb.pret) AS venit_total
                FROM eveniment e
                JOIN tip_bilet tb
                    ON tb.eveniment_id = e.id
                JOIN bilet b
                    ON b.tip_bilet_id = tb.id
                GROUP BY e.id, e.nume
                """;

        try (
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                System.out.println(
                        "VenitEveniment{" +
                                "id=" + resultSet.getInt("id") +
                                ", eveniment='" + resultSet.getString("nume_eveniment") + '\'' +
                                ", bileteVandute=" + resultSet.getInt("numar_bilete_vandute") +
                                ", venitTotal=" + resultSet.getDouble("venit_total") +
                                '}'
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Eroare la afișarea veniturilor pe evenimente.",
                    e
            );
        }
    }

    public void afiseazaClientiPentruEveniment(int evenimentId) {
        String sql = """
            SELECT DISTINCT
                c.id,
                c.nume,
                c.email
            FROM client c
            JOIN bilet b
                ON b.client_id = c.id
            JOIN tip_bilet tb
                ON b.tip_bilet_id = tb.id
            JOIN eveniment e
                ON tb.eveniment_id = e.id
            WHERE e.id = ?
            """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, evenimentId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    System.out.println(
                            "ClientEveniment{" +
                                    "id=" + resultSet.getInt("id") +
                                    ", nume='" + resultSet.getString("nume") + '\'' +
                                    ", email='" + resultSet.getString("email") + '\'' +
                                    '}'
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea clienților pentru eveniment.", e);
        }
    }

    public void afiseazaBiletePentruClient(int clientId) {
        String sql = """
            SELECT
                b.cod_bilet,
                e.nume AS nume_eveniment,
                tb.nume AS tip_bilet,
                tb.pret,
                b.data_cumparare
            FROM bilet b
            JOIN tip_bilet tb
                ON b.tip_bilet_id = tb.id
            JOIN eveniment e
                ON tb.eveniment_id = e.id
            WHERE b.client_id = ?
            """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, clientId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    System.out.println(
                            "BiletClient{" +
                                    "cod='" + resultSet.getString("cod_bilet") + '\'' +
                                    ", eveniment='" + resultSet.getString("nume_eveniment") + '\'' +
                                    ", tipBilet='" + resultSet.getString("tip_bilet") + '\'' +
                                    ", pret=" + resultSet.getDouble("pret") +
                                    ", dataCumparare=" + resultSet.getString("data_cumparare") +
                                    '}'
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea biletelor clientului.", e);
        }
    }
}