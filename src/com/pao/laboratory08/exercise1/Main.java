package com.pao.laboratory08.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    // Calea către fișierul cu date — relativă la rădăcina proiectului
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";

    public static void main(String[] args) throws Exception {
        List<Student> studenti = citesteStudenti();

        Scanner scanner = new Scanner(System.in);
        String comanda = scanner.nextLine();

        String[] parts = comanda.split(" ", 2);
        String tipComanda = parts[0];

        switch (tipComanda) {
            case "PRINT":
                for (Student student : studenti) {
                    System.out.println(student);
                }
                break;

            case "SHALLOW":
                executaClonare(studenti, parts[1], false);
                break;

            case "DEEP":
                executaClonare(studenti, parts[1], true);
                break;
        }
    }

    private static List<Student> citesteStudenti() throws IOException {
        List<Student> studenti = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(FILE_PATH));
        String linie;

        while ((linie = br.readLine()) != null) {
            if (linie.trim().isEmpty()) {
                continue;
            }

            String[] parts = linie.split(",");

            if (parts.length < 4) {
                continue;
            }

            String nume = parts[0].trim();
            int varsta = Integer.parseInt(parts[1].trim());
            String oras = parts[2].trim();
            String strada = parts[3].trim();

            Adresa adresa = new Adresa(oras, strada);
            Student student = new Student(nume, varsta, adresa);

            studenti.add(student);
        }

        br.close();
        return studenti;
    }

    private static void executaClonare(List<Student> studenti, String nume, boolean deep)
            throws CloneNotSupportedException {

        Student original = gasesteStudent(studenti, nume);

        Student clona;
        if (deep) {
            clona = original.deepClone();
        } else {
            clona = original.shallowClone();
        }

        clona.getAdresa().setOras("MODIFICAT");

        System.out.println("Original: " + original);
        System.out.println("Clona: " + clona);
    }

    private static Student gasesteStudent(List<Student> studenti, String nume) {
        for (Student student : studenti) {
            if (student.getNume().equals(nume)) {
                return student;
            }
        }

        return null;
    }
}
