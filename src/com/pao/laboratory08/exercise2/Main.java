package com.pao.laboratory08.exercise2;

import com.pao.laboratory08.exercise1.Adresa;
import com.pao.laboratory08.exercise1.Student;

import java.io.*;
import java.util.*;

public class Main {
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";
    private static final String OUTPUT_FILE = "src/com/pao/laboratory08/exercise2/rezultate.txt";

    public static void main(String[] args) throws Exception {
        List<Student> studenti = citesteStudenti();

        Scanner scanner = new Scanner(System.in);
        int prag = scanner.nextInt();

        List<Student> studentiFiltrati = filtreazaStudenti(studenti, prag);

        scrieInFisier(studentiFiltrati);

        System.out.println("Filtru: varsta >= " + prag);
        System.out.println("Rezultate: " + studentiFiltrati.size() + " studenti");
        System.out.println();

        for (Student student : studentiFiltrati) {
            System.out.println(student);
        }

        System.out.println();
        System.out.println("Scris in: rezultate.txt");
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

    private static List<Student> filtreazaStudenti(List<Student> studenti, int prag) {
        List<Student> rezultat = new ArrayList<>();

        for (Student student : studenti) {
            if (student.getVarsta() >= prag) {
                rezultat.add(student);
            }
        }

        return rezultat;
    }

    private static void scrieInFisier(List<Student> studenti) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(OUTPUT_FILE));

        for (Student student : studenti) {
            bw.write(student.toString());
            bw.newLine();
        }

        bw.close();
    }
}