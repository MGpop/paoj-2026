package com.pao.laboratory05.angajati;

import java.util.Scanner;

/**
 * Exercise 3 — Angajați
 *
 * Cerințele complete se află în:
 *   src/com/pao/laboratory05/Readme.md  →  secțiunea "Exercise 3 — Angajați"
 *
 * Creează fișierele de la zero în acest pachet, apoi rulează Main.java
 * pentru a verifica output-ul așteptat din Readme.
 */
public class Main {
    public static void main(String[] args) {
        //System.out.println("Cerințele se află în Readme.md — secțiunea Exercise 3.");

        Scanner scanner = new Scanner(System.in);
        AngajatService service = AngajatService.getInstance();

        while (true) {
            System.out.println("\n===== Gestionare Angajați =====");
            System.out.println("1. Adaugă angajat");
            System.out.println("2. Listare după salariu");
            System.out.println("3. Caută după departament");
            System.out.println("0. Ieșire");
            System.out.print("Opțiune: ");
            // citește opțiunea și execută acțiunea

            String opt = scanner.nextLine().trim();

            switch (opt) {
                case "1":
                    System.out.print("Nume: ");
                    String nume = scanner.nextLine();
                    System.out.print("Departament (nume): ");
                    String numeDept = scanner.nextLine();
                    System.out.print("Departament (locatie): ");
                    String locatie = scanner.nextLine();
                    System.out.print("Salariu: ");
                    double salariu = Double.parseDouble(scanner.nextLine());
                    Departament dept = new Departament(numeDept, locatie);
                    Angajat angajat = new Angajat(nume, dept, salariu);
                    service.addAngajat(angajat);
                    break;

                case "2":
                    service.listBySalary();
                    break;

                case "3":
                    System.out.print("Departament: ");
                    String search = scanner.nextLine();
                    service.findByDepartment(search);
                    break;

                case "0":
                    System.out.println("La revedere!");
                    return;

                default:
                    System.out.println("Opțiune invalidă.");
            }
        }
    }
}
