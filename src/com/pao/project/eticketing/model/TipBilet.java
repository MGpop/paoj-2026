package com.pao.project.eticketing.model;

public class TipBilet {
    private int id;
    private String nume;
    private double pret;
    private int stocDisponibil;

    public TipBilet(int id, String nume, double pret, int stocDisponibil) {
        this.id = id;
        this.nume = nume;
        this.pret = pret;
        this.stocDisponibil = stocDisponibil;
    }

    public boolean areStoc(int cantitate) {
        return cantitate > 0 && stocDisponibil >= cantitate;
    }

    public void scadeStoc(int cantitate) {
        if (cantitate > 0 && stocDisponibil >= cantitate) {
            stocDisponibil -= cantitate;
        }
    }

    public void adaugaStoc(int cantitate) {
        if (cantitate > 0) {
            stocDisponibil += cantitate;
        }
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

    public double getPret() {
        return pret;
    }

    public void setPret(double pret) {
        this.pret = pret;
    }

    public int getStocDisponibil() {
        return stocDisponibil;
    }

    public void setStocDisponibil(int stocDisponibil) {
        this.stocDisponibil = stocDisponibil;
    }

    @Override
    public String toString() {
        return "TipBilet{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                ", pret=" + pret +
                ", stocDisponibil=" + stocDisponibil +
                '}';
    }
}