package com.pao.laboratory09.exercise3;

import java.time.LocalDate;

class Tranzactie {
    private final int id;
    private final double suma;
    private final LocalDate data;

    public Tranzactie(int id, double suma, LocalDate data) {
        this.id = id;
        this.suma = suma;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public double getSuma() {
        return suma;
    }

    public LocalDate getData() {
        return data;
    }

    @Override
    public String toString() {
        return String.format("Tranzacție #%d %.2f RON", id, suma);
    }
}
