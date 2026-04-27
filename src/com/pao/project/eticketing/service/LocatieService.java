package com.pao.project.eticketing.service;

import com.pao.project.eticketing.model.Adresa;
import com.pao.project.eticketing.model.Locatie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocatieService {
    private static LocatieService instance;

    private final List<Locatie> locatii;
    private final Map<Integer, Locatie> locatiiById;

    private LocatieService() {
        this.locatii = new ArrayList<>();
        this.locatiiById = new HashMap<>();
    }

    public static LocatieService getInstance() {
        if (instance == null) {
            instance = new LocatieService();
        }
        return instance;
    }

    public void adaugaLocatie(Locatie locatie) {
        if (locatie == null) {
            return;
        }

        if (!locatiiById.containsKey(locatie.getId())) {
            locatii.add(locatie);
            locatiiById.put(locatie.getId(), locatie);
        }
    }

    public void modificaLocatie(int id, String numeNou, Adresa adresaNoua, Integer capacitateNoua) {
        Locatie locatie = cautaLocatieDupaId(id);

        if (locatie == null) {
            return;
        }

        if (numeNou != null && !numeNou.isEmpty()) {
            locatie.setNume(numeNou);
        }

        if (adresaNoua != null) {
            locatie.setAdresa(adresaNoua);
        }

        if (capacitateNoua != null && capacitateNoua > 0) {
            locatie.setCapacitate(capacitateNoua);
        }
    }

    public void stergeLocatie(int id) {
        Locatie locatie = cautaLocatieDupaId(id);

        if (locatie == null) {
            return;
        }

        locatii.remove(locatie);
        locatiiById.remove(id);
    }

    public Locatie cautaLocatieDupaId(int id) {
        return locatiiById.get(id);
    }

    public Locatie cautaLocatieDupaNume(String nume) {
        if (nume == null || nume.isEmpty()) {
            return null;
        }

        for (Locatie locatie : locatii) {
            if (locatie.getNume().equalsIgnoreCase(nume)) {
                return locatie;
            }
        }

        return null;
    }

    public List<Locatie> listeazaLocatii() {
        return new ArrayList<>(locatii);
    }

    public List<Locatie> listeazaLocatiiDupaOras(String oras) {
        List<Locatie> rezultate = new ArrayList<>();

        if (oras == null || oras.isEmpty()) {
            return rezultate;
        }

        for (Locatie locatie : locatii) {
            if (locatie.getAdresa() != null &&
                    locatie.getAdresa().getOras().equalsIgnoreCase(oras)) {
                rezultate.add(locatie);
            }
        }

        return rezultate;
    }
}