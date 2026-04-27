package com.pao.project.eticketing.model;

import java.time.LocalDateTime;

public class PiesaTeatru extends Eveniment {
    private String autor;
    private String regizor;
    private int durataMinute;

    public PiesaTeatru(int id, String nume, LocalDateTime data, Locatie locatie,
                       String autor, String regizor, int durataMinute) {
        super(id, nume, data, locatie);
        this.autor = autor;
        this.regizor = regizor;
        this.durataMinute = durataMinute;
    }

    @Override
    public String getTipEveniment() {
        return "Piesa de teatru";
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getRegizor() {
        return regizor;
    }

    public void setRegizor(String regizor) {
        this.regizor = regizor;
    }

    public int getDurataMinute() {
        return durataMinute;
    }

    public void setDurataMinute(int durataMinute) {
        this.durataMinute = durataMinute;
    }

    @Override
    public String toString() {
        return "PiesaTeatru{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                ", data=" + data +
                ", locatie=" + locatie +
                ", autor='" + autor + '\'' +
                ", regizor='" + regizor + '\'' +
                ", durataMinute=" + durataMinute +
                '}';
    }
}