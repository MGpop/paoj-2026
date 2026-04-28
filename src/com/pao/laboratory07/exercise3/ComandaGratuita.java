package com.pao.laboratory07.exercise3;

public final class ComandaGratuita extends Comanda {

    public ComandaGratuita(String nume, String client) {
        super(nume, client);
    }

    @Override
    public double pretFinal() {
        return 0.0;
    }

    @Override
    public String descriere() {
        return "GIFT: " + nume + ", gratuit [" + stare + "] - client: " + client;
    }

    @Override
    public String descriereScurta() {
        return "GIFT: " + nume + ", gratuit - client: " + client;
    }

    @Override
    public String tip() {
        return "GIFT";
    }
}