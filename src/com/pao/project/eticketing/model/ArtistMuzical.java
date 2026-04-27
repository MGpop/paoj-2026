package com.pao.project.eticketing.model;

public abstract class ArtistMuzical extends Participant {
    protected String genMuzical;

    public ArtistMuzical(int id, String nume, String descriere, String genMuzical) {
        super(id, nume, descriere);
        this.genMuzical = genMuzical;
    }

    public String getGenMuzical() {
        return genMuzical;
    }

    public void setGenMuzical(String genMuzical) {
        this.genMuzical = genMuzical;
    }

    @Override
    public String toString() {
        return "ArtistMuzical{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                ", genMuzical='" + genMuzical + '\'' +
                '}';
    }
}