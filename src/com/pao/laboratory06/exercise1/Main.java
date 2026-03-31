package com.pao.laboratory06.exercise1;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Vezi Readme.md pentru cerințe
        Scanner scanner = new Scanner(System.in);
        String optiune = scanner.next();
        int numarAngajati = scanner.nextInt();
        Angajat[] angajati = new Angajat[numarAngajati];
        for (int i = 0; i < numarAngajati; i++) {
            angajati[i] = Angajat.citeste(scanner);
        }
        // cerinte: sorteaza in functie de optiune
        Comparator<Angajat> comparator = switch (optiune) {
            case "by_name" -> new SortareDupaNume();
            case "by_salary" -> new SortareDupaSalariu();
            case "by_salary_desc" -> new SortareDupaSalariuDescrescator();
            default -> (a1, a2) -> 0;
        };
        Arrays.sort(angajati, comparator);
        for (Angajat angajat : angajati) {
            System.out.println(angajat);
        }
    }
}
