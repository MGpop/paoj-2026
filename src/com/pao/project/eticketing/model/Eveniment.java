package com.pao.project.eticketing.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Eveniment implements Comparable<Eveniment> {
    protected int id;
    protected String nume;
    protected LocalDateTime data;
    protected Locatie locatie;
    protected List<Participant> participanti;
    protected List<TipBilet> tipuriBilete;

    public Eveniment(int id, String nume, LocalDateTime data, Locatie locatie) {
        this.id = id;
        this.nume = nume;
        this.data = data;
        this.locatie = locatie;
        this.participanti = new ArrayList<>();
        this.tipuriBilete = new ArrayList<>();
    }

    public abstract String getTipEveniment();

    @Override
    public int compareTo(Eveniment other) {
        return this.data.compareTo(other.data);
    }

    public void adaugaParticipant(Participant participant) {
        if (participant != null) {
            participanti.add(participant);
        }
    }

    public void adaugaTipBilet(TipBilet tipBilet) {
        if (tipBilet != null) {
            tipuriBilete.add(tipBilet);
        }
    }

    public double getPretMinimBilet() {
        if (tipuriBilete.isEmpty()) {
            return 0;
        }

        double minim = tipuriBilete.get(0).getPret();

        for (TipBilet tipBilet : tipuriBilete) {
            if (tipBilet.getPret() < minim) {
                minim = tipBilet.getPret();
            }
        }

        return minim;
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

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public Locatie getLocatie() {
        return locatie;
    }

    public void setLocatie(Locatie locatie) {
        this.locatie = locatie;
    }

    public List<Participant> getParticipanti() {
        return participanti;
    }

    public void setParticipanti(List<Participant> participanti) {
        this.participanti = participanti;
    }

    public List<TipBilet> getTipuriBilete() {
        return tipuriBilete;
    }

    public void setTipuriBilete(List<TipBilet> tipuriBilete) {
        this.tipuriBilete = tipuriBilete;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Eveniment)) return false;
        Eveniment eveniment = (Eveniment) o;
        return id == eveniment.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Eveniment{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                ", data=" + data +
                ", locatie=" + locatie +
                ", tip='" + getTipEveniment() + '\'' +
                '}';
    }
}