package com.pao.project.eticketing.model;

import java.time.LocalDateTime;

public class Bilet {
    private String codBilet;
    private Client client;
    private Eveniment eveniment;
    private TipBilet tipBilet;
    private LocalDateTime dataCumparare;

    public Bilet(String codBilet, Client client, Eveniment eveniment, TipBilet tipBilet) {
        this.codBilet = codBilet;
        this.client = client;
        this.eveniment = eveniment;
        this.tipBilet = tipBilet;
        this.dataCumparare = LocalDateTime.now();
    }

    public String getCodBilet() {
        return codBilet;
    }

    public Client getClient() {
        return client;
    }

    public Eveniment getEveniment() {
        return eveniment;
    }

    public TipBilet getTipBilet() {
        return tipBilet;
    }

    public LocalDateTime getDataCumparare() {
        return dataCumparare;
    }

    @Override
    public String toString() {
        return "Bilet{" +
                "codBilet='" + codBilet + '\'' +
                ", client=" + client.getNume() +
                ", eveniment=" + eveniment.getNume() +
                ", tipBilet=" + tipBilet.getNume() +
                ", dataCumparare=" + dataCumparare +
                '}';
    }
}