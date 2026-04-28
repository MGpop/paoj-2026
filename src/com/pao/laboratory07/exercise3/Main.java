package com.pao.laboratory07.exercise3;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = Integer.parseInt(sc.nextLine().trim());
        List<Comanda> comenzi = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String line = sc.nextLine().trim();
            String[] tokens = line.split(" ");

            switch (tokens[0]) {
                case "STANDARD" -> {
                    String nume = tokens[1];
                    double pret = Double.parseDouble(tokens[2]);
                    String client = tokens[3];
                    comenzi.add(new ComandaStandard(nume, pret, client));
                }

                case "DISCOUNTED" -> {
                    String nume = tokens[1];
                    double pret = Double.parseDouble(tokens[2]);
                    int discount = Integer.parseInt(tokens[3]);
                    String client = tokens[4];
                    comenzi.add(new ComandaRedusa(nume, pret, discount, client));
                }

                case "GIFT" -> {
                    String nume = tokens[1];
                    String client = tokens[2];
                    comenzi.add(new ComandaGratuita(nume, client));
                }
            }
        }

        for (Comanda comanda : comenzi) {
            System.out.println(comanda.descriere());
        }

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();

            if (line.equals("QUIT")) {
                break;
            }

            if (line.equals("STATS")) {
                afiseazaStats(comenzi);
            } else if (line.startsWith("FILTER")) {
                double threshold = Double.parseDouble(line.split(" ")[1]);
                afiseazaFilter(comenzi, threshold);
            } else if (line.equals("SORT")) {
                afiseazaSort(comenzi);
            } else if (line.equals("SPECIAL")) {
                afiseazaSpecial(comenzi);
            }
        }
    }

    private static void afiseazaStats(List<Comanda> comenzi) {
        System.out.println();
        System.out.println("--- STATS ---");

        Map<String, Double> medii = comenzi.stream()
                .collect(Collectors.groupingBy(
                        Comanda::tip,
                        Collectors.averagingDouble(Comanda::pretFinal)
                ));

        if (medii.containsKey("STANDARD")) {
            System.out.printf(Locale.US, "STANDARD: medie = %.2f lei\n", medii.get("STANDARD"));
        }

        if (medii.containsKey("DISCOUNTED")) {
            System.out.printf(Locale.US, "DISCOUNTED: medie = %.2f lei\n", medii.get("DISCOUNTED"));
        }

        if (medii.containsKey("GIFT")) {
            System.out.printf(Locale.US, "GIFT: medie = %.2f lei\n", medii.get("GIFT"));
        }
    }

    private static void afiseazaFilter(List<Comanda> comenzi, double threshold) {
        System.out.println();
        System.out.printf(Locale.US, "--- FILTER (>= %.2f) ---\n", threshold);

        comenzi.stream()
                .filter(c -> c.pretFinal() >= threshold)
                .forEach(c -> System.out.println(c.descriereScurta()));
    }

    private static void afiseazaSort(List<Comanda> comenzi) {
        System.out.println();
        System.out.println("--- SORT (by client, then by pret) ---");

        comenzi.stream()
                .sorted(Comparator.comparing(Comanda::getClient)
                        .thenComparing(Comanda::pretFinal))
                .forEach(c -> System.out.println(c.descriereScurta()));
    }

    private static void afiseazaSpecial(List<Comanda> comenzi) {
        System.out.println();
        System.out.println("--- SPECIAL (discount > 15%) ---");

        comenzi.stream()
                .filter(c -> c instanceof ComandaRedusa)
                .map(c -> (ComandaRedusa) c)
                .filter(c -> c.getDiscountProcent() > 15)
                .forEach(c -> System.out.println(c.descriereSpeciala()));
    }
}