package com.pao.laboratory09.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex1.ser";

    public static void main(String[] args) throws Exception {

        // TODO: Implementează conform Readme.md
        //
        // 1. Citește N din stdin, apoi cele N tranzacții (id suma data contSursa contDestinatie tip)
        // 2. Setează câmpul note = "procesat" pe fiecare tranzacție înainte de serializare

        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        List<Tranzactie> tranzactii = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            int id = scanner.nextInt();
            double suma = scanner.nextDouble();
            String data = scanner.next();
            String contSursa = scanner.next();
            String contDestinatie = scanner.next();
            TipTranzactie tip = TipTranzactie.valueOf(scanner.next());

            Tranzactie tranzactie = new Tranzactie(id, suma, data, contSursa, contDestinatie, tip);
            tranzactie.setNote("procesat");
            tranzactii.add(tranzactie);
        }

        // 3. Serializează lista de tranzacții în OUTPUT_FILE cu ObjectOutputStream (try-with-resources)

        File outputFile = new File(OUTPUT_FILE);
        File parent = outputFile.getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(outputFile))) {
            out.writeObject(tranzactii);
        }

        // 4. Deserializează lista din OUTPUT_FILE cu ObjectInputStream (try-with-resources)

        List<Tranzactie> tranzactiiDeserializate;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(outputFile))) {
            tranzactiiDeserializate = (List<Tranzactie>) in.readObject();
        }

        // 5. Procesează comenzile din stdin până la EOF:
        //    - LIST
        //    - FILTER yyyy-MM
        //    - NOTE id

        while (scanner.hasNext()) {
            String comanda = scanner.next();

            if (comanda.equals("LIST")) {
                for (Tranzactie tranzactie : tranzactiiDeserializate) {
                    System.out.println(tranzactie);
                }

            } else if (comanda.equals("FILTER")) {
                String prefix = scanner.next();
                boolean gasit = false;

                for (Tranzactie tranzactie : tranzactiiDeserializate) {
                    if (tranzactie.getData().startsWith(prefix)) {
                        System.out.println(tranzactie);
                        gasit = true;
                    }
                }

                if (!gasit) {
                    System.out.println("Niciun rezultat.");
                }

            } else if (comanda.equals("NOTE")) {
                int id = scanner.nextInt();
                Tranzactie gasita = null;

                for (Tranzactie tranzactie : tranzactiiDeserializate) {
                    if (tranzactie.getId() == id) {
                        gasita = tranzactie;
                        break;
                    }
                }

                if (gasita == null) {
                    System.out.println("NOTE[" + id + "]: not found");
                } else {
                    System.out.println("NOTE[" + id + "]: " + gasita.getNote());
                }
            }
        }

        // Format linie tranzacție:
        //   [id] data tip: suma RON | contSursa -> contDestinatie
        //   Ex: [1] 2024-01-15 CREDIT: 1500.00 RON | RO01SRC1 -> RO01DST1
    }
}