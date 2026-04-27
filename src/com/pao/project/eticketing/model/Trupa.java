package com.pao.project.eticketing.model;

public class Trupa extends ArtistMuzical {
    private int numarMembri;

    public Trupa(int id, String nume, String descriere, String genMuzical, int numarMembri) {
        super(id, nume, descriere, genMuzical);
        this.numarMembri = numarMembri;
    }

    @Override
    public String getTipParticipant() {
        return "Trupa";
    }

    public int getNumarMembri() {
        return numarMembri;
    }

    public void setNumarMembri(int numarMembri) {
        this.numarMembri = numarMembri;
    }

    @Override
    public String toString() {
        return "Trupa{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                ", genMuzical='" + genMuzical + '\'' +
                ", membri=" + numarMembri +
                '}';
    }
}