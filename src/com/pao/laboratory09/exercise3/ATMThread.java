package com.pao.laboratory09.exercise3;

import java.time.LocalDate;

class ATMThread extends Thread {
    private static int urmatorulId = 1;

    private final int atmId;
    private final CoadaTranzactii coada;

    public ATMThread(int atmId, CoadaTranzactii coada) {
        this.atmId = atmId;
        this.coada = coada;
    }

    @Override
    public void run() {
        for (int i = 0; i < 4; i++) {
            try {
                Tranzactie tranzactie = new Tranzactie(genereazaId(), 100.0 * atmId + 25.5 * i, LocalDate.now());
                String numeATM = "ATM-" + atmId;

                System.out.println("[" + numeATM + "] trimite: " + tranzactie);
                coada.adauga(tranzactie, numeATM);

                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    private static synchronized int genereazaId() {
        return urmatorulId++;
    }
}
