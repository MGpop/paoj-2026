package com.pao.laboratory10.exercise3;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<Tranzactie> tranzactii = List.of(
                new Tranzactie(1, "2026-01-05", TipTranzactie.CREDIT, 1250.00, "CONT_SALARIU"),
                new Tranzactie(2, "2026-01-12", TipTranzactie.DEBIT, 180.50, "CONT_CURENT"),
                new Tranzactie(3, "2026-01-20", TipTranzactie.CREDIT, 320.00, "CONT_ECONOMII"),
                new Tranzactie(4, "2026-02-03", TipTranzactie.DEBIT, 75.99, "CONT_CURENT"),
                new Tranzactie(5, "2026-02-10", TipTranzactie.CREDIT, 2200.00, "CONT_SALARIU"),
                new Tranzactie(6, "2026-02-22", TipTranzactie.DEBIT, 640.00, "CONT_CARD"),
                new Tranzactie(7, "2026-03-01", TipTranzactie.CREDIT, 900.00, "CONT_ECONOMII"),
                new Tranzactie(8, "2026-03-08", TipTranzactie.DEBIT, 150.00, "CONT_CARD"),
                new Tranzactie(9, "2026-03-16", TipTranzactie.CREDIT, 4000.00, "CONT_INVESTITII"),
                new Tranzactie(10, "2026-04-02", TipTranzactie.DEBIT, 300.00, "CONT_CURENT"),
                new Tranzactie(11, "2026-04-11", TipTranzactie.CREDIT, 780.25, "CONT_SALARIU")
        );

        System.out.println("1. Tranzactii CREDIT");
        tranzactii.stream()
                .filter(t -> t.getTip() == TipTranzactie.CREDIT)
                .forEach(System.out::println);

        System.out.println("\n2. Total procesat");
        double totalProcesat = tranzactii.stream()
                .mapToDouble(Tranzactie::getSuma)
                .sum();
        System.out.printf("Total procesat: %.2f RON%n", totalProcesat);

        System.out.println("\n3. Total per luna");
        Map<String, Double> totalPerLuna = tranzactii.stream()
                .collect(Collectors.groupingBy(
                        Tranzactie::getLuna,
                        TreeMap::new,
                        Collectors.summingDouble(Tranzactie::getSuma)
                ));
        totalPerLuna.forEach((luna, total) ->
                System.out.printf("%s: %.2f RON%n", luna, total));

        System.out.println("\n4. Top 3 tranzactii");
        tranzactii.stream()
                .sorted(Comparator.comparingDouble(Tranzactie::getSuma).reversed())
                .limit(3)
                .forEach(System.out::println);

        System.out.println("\n5. Conturi sursa unice");
        List<String> conturiSursa = tranzactii.stream()
                .map(Tranzactie::getContSursa)
                .distinct()
                .collect(Collectors.toList());
        System.out.println("Conturi sursa unice: " + conturiSursa);

        System.out.println("\n6. Suma medie");
        double sumaMedie = tranzactii.stream()
                .mapToDouble(Tranzactie::getSuma)
                .average()
                .orElse(0.0);
        System.out.printf("Suma medie: %.2f RON%n", sumaMedie);

        System.out.println("\n7. Extrase de cont lunare");
        Map<String, List<Tranzactie>> tranzactiiPerLuna = tranzactii.stream()
                .collect(Collectors.groupingBy(
                        Tranzactie::getLuna,
                        TreeMap::new,
                        Collectors.toList()
                ));

        tranzactiiPerLuna.forEach((luna, lista) -> {
            double total = lista.stream()
                    .mapToDouble(Tranzactie::getSuma)
                    .sum();

            System.out.printf("EXTRAS DE CONT - %s: %d tranzactii, total: %.2f RON%n",
                    luna, lista.size(), total);
        });
    }

    enum TipTranzactie {
        CREDIT,
        DEBIT
    }

    static class Tranzactie {
        private final int id;
        private final String data;
        private final TipTranzactie tip;
        private final double suma;
        private final String contSursa;

        public Tranzactie(int id, String data, TipTranzactie tip, double suma, String contSursa) {
            this.id = id;
            this.data = data;
            this.tip = tip;
            this.suma = suma;
            this.contSursa = contSursa;
        }

        public String getLuna() {
            return data.substring(0, 7);
        }

        public TipTranzactie getTip() {
            return tip;
        }

        public double getSuma() {
            return suma;
        }

        public String getContSursa() {
            return contSursa;
        }

        @Override
        public String toString() {
            return String.format("[%d] %s %s %.2f RON, cont sursa: %s",
                    id, data, tip, suma, contSursa);
        }
    }
}
