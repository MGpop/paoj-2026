package com.pao.project.eticketing.model;

import java.time.LocalDateTime;

public class Concert extends Eveniment {
    private String genMuzical;

    public Concert(int id, String nume, LocalDateTime data, Locatie locatie, String genMuzical) {
        super(id, nume, data, locatie);
        this.genMuzical = genMuzical;
    }

    @Override
    public String getTipEveniment() {
        return "Concert";
    }

    public String getGenMuzical() {
        return genMuzical;
    }

    public void setGenMuzical(String genMuzical) {
        this.genMuzical = genMuzical;
    }

    @Override
    public String toString() {
        return "Concert{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                ", data=" + data +
                ", locatie=" + locatie +
                ", genMuzical='" + genMuzical + '\'' +
                '}';
    }
}