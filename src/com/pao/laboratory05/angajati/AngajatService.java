package com.pao.laboratory05.angajati;

import java.util.Arrays;

public class AngajatService {

    private Angajat[] angajati;

    private AngajatService() {
        angajati = new Angajat[0];
    }

    private static class Holder {
        private static final AngajatService INSTANCE = new AngajatService();
    }

    public static AngajatService getInstance() {
        return Holder.INSTANCE;
    }

    public void addAngajat(Angajat a) {
        Angajat[] newArr = new Angajat[angajati.length + 1];
        System.arraycopy(angajati, 0, newArr, 0, angajati.length);
        newArr[angajati.length] = a;
        angajati = newArr;
        System.out.println("Angajat adăugat: " + a.getNume());
    }

    public void printAll() {
        for (Angajat a : angajati) {
            System.out.println(a);
        }
    }

    public void listBySalary() {
        Angajat[] copy = angajati.clone();
        Arrays.sort(copy);
        for (Angajat a : copy) {
            System.out.println(a);
        }
    }

    public void findByDepartment(String numeDept) {
        boolean found = false;
        for (Angajat a : angajati) {
            if (a.getDepartament().nume().equalsIgnoreCase(numeDept)) {
                System.out.println(a);
                found = true;
            }
        }
        if (!found) {
            System.out.println("Niciun angajat în departamentul: " + numeDept);
        }
    }
}
