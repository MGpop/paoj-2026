package com.pao.laboratory11.exercise3;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        List<Transaction> transactions = List.of(
                new Transaction(1, new BigDecimal("120.50"), LocalDate.of(2024, 3, 10), "Romania", "Online"),
                new Transaction(2, new BigDecimal("75.00"), LocalDate.of(2024, 3, 11), "Germania", "Store"),
                new Transaction(3, new BigDecimal("220.00"), LocalDate.of(2024, 3, 12), "Romania", "Online"),
                new Transaction(4, new BigDecimal("220.00"), LocalDate.of(2024, 3, 13), "Franta", "Partner"),
                new Transaction(5, new BigDecimal("49.99"), LocalDate.of(2024, 3, 14), "Germania", "Online"),
                new Transaction(6, new BigDecimal("310.00"), LocalDate.of(2024, 3, 15), "Romania", "Store"),
                new Transaction(7, new BigDecimal("18.75"), LocalDate.of(2024, 3, 16), "Franta", "Online")
        );

        Snapshot snapshot = transactions.stream()
                .collect(CustomCollectors.toSnapshot(3));

        System.out.println("Total general:");
        System.out.println(snapshot.getTotalAmount());

        System.out.println("\nTop 3 tranzactii:");
        snapshot.getTopTransactions()
                .forEach(System.out::println);

        System.out.println("\nNumar tranzactii pe tara:");
        snapshot.getCountByCountry().entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .forEach(entry ->
                        System.out.println(entry.getKey() + ": " + entry.getValue()));

        System.out.println("\nNumar tranzactii pe canal:");
        snapshot.getCountByChannel().entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .forEach(entry ->
                        System.out.println(entry.getKey() + ": " + entry.getValue()));

        System.out.println("\nTotal suma pe tara:");
        snapshot.getTotalByCountry().entrySet().stream()
                .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
                .forEach(entry ->
                        System.out.println(entry.getKey() + ": " + entry.getValue()));
    }
}