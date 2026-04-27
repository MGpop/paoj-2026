package com.pao.project.eticketing.model;

import java.util.Objects;

public abstract class Participant {
    protected int id;
    protected String nume;
    protected String descriere;

    public Participant(int id, String nume, String descriere) {
        this.id = id;
        this.nume = nume;
        this.descriere = descriere;
    }

    public abstract String getTipParticipant();

    public int getId() {
        return id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    @Override
    public String toString() {
        return "Participant{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                ", tip='" + getTipParticipant() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Participant)) return false;
        Participant that = (Participant) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}