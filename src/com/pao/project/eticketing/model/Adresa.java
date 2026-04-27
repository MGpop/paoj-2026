package com.pao.project.eticketing.model;

import java.util.Objects;

public final class Adresa {
    private final String strada;
    private final String numar;
    private final String oras;
    private final String judet;

    public Adresa(String strada, String numar, String oras, String judet) {
        this.strada = strada;
        this.numar = numar;
        this.oras = oras;
        this.judet = judet;
    }

    public String getStrada() {
        return strada;
    }

    public String getNumar() {
        return numar;
    }

    public String getOras() {
        return oras;
    }

    public String getJudet() {
        return judet;
    }

    @Override
    public String toString() {
        return strada + " " + numar + ", " + oras + ", " + judet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Adresa)) return false;
        Adresa adresa = (Adresa) o;
        return Objects.equals(strada, adresa.strada) &&
                Objects.equals(numar, adresa.numar) &&
                Objects.equals(oras, adresa.oras) &&
                Objects.equals(judet, adresa.judet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(strada, numar, oras, judet);
    }
}