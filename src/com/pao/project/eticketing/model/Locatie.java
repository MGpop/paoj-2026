package com.pao.project.eticketing.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Locatie {
    private int id;
    private String nume;
    private Adresa adresa;
    private int capacitate;
    private Set<String> tipuriEvenimentePermise;

    public Locatie(int id, String nume, Adresa adresa, int capacitate) {
        this.id = id;
        this.nume = nume;
        this.adresa = adresa;
        this.capacitate = capacitate;
        this.tipuriEvenimentePermise = new HashSet<>();
    }

    public void adaugaTipEvenimentPermis(String tip) {
        if (tip != null && !tip.isEmpty()) {
            tipuriEvenimentePermise.add(tip);
        }
    }

    public boolean estePermis(String tipEveniment) {
        return tipuriEvenimentePermise.contains(tipEveniment);
    }

    public int getId() {
        return id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public Adresa getAdresa() {
        return adresa;
    }

    public void setAdresa(Adresa adresa) {
        this.adresa = adresa;
    }

    public int getCapacitate() {
        return capacitate;
    }

    public void setCapacitate(int capacitate) {
        this.capacitate = capacitate;
    }

    public Set<String> getTipuriEvenimentePermise() {
        return tipuriEvenimentePermise;
    }

    public void setTipuriEvenimentePermise(Set<String> tipuriEvenimentePermise) {
        this.tipuriEvenimentePermise = tipuriEvenimentePermise;
    }

    @Override
    public String toString() {
        return "Locatie{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                ", adresa=" + adresa +
                ", capacitate=" + capacitate +
                ", tipuriPermise=" + tipuriEvenimentePermise +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Locatie)) return false;
        Locatie locatie = (Locatie) o;
        return id == locatie.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}