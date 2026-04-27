package com.pao.project.eticketing.model;

public class Orchestra extends ArtistMuzical {
    private String tipOrchestra; // ex: simfonică, de cameră

    public Orchestra(int id, String nume, String descriere, String genMuzical, String tipOrchestra) {
        super(id, nume, descriere, genMuzical);
        this.tipOrchestra = tipOrchestra;
    }

    @Override
    public String getTipParticipant() {
        return "Orchestra";
    }

    public String getTipOrchestra() {
        return tipOrchestra;
    }

    public void setTipOrchestra(String tipOrchestra) {
        this.tipOrchestra = tipOrchestra;
    }

    @Override
    public String toString() {
        return "Orchestra{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                ", genMuzical='" + genMuzical + '\'' +
                ", tipOrchestra='" + tipOrchestra + '\'' +
                '}';
    }
}