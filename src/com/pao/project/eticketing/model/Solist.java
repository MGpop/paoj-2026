package com.pao.project.eticketing.model;

public class Solist extends ArtistMuzical {
    private String instrumentPrincipal;

    public Solist(int id, String nume, String descriere, String genMuzical, String instrumentPrincipal) {
        super(id, nume, descriere, genMuzical);
        this.instrumentPrincipal = instrumentPrincipal;
    }

    @Override
    public String getTipParticipant() {
        return "Solist";
    }

    public String getInstrumentPrincipal() {
        return instrumentPrincipal;
    }

    public void setInstrumentPrincipal(String instrumentPrincipal) {
        this.instrumentPrincipal = instrumentPrincipal;
    }

    @Override
    public String toString() {
        return "Solist{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                ", genMuzical='" + genMuzical + '\'' +
                ", instrument='" + instrumentPrincipal + '\'' +
                '}';
    }
}