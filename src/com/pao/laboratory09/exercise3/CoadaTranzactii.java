package com.pao.laboratory09.exercise3;

import java.util.LinkedList;
import java.util.Queue;

class CoadaTranzactii {
    private final Queue<Tranzactie> tranzactii = new LinkedList<>();
    private final int capacitate;

    public CoadaTranzactii(int capacitate) {
        this.capacitate = capacitate;
    }

    public synchronized void adauga(Tranzactie tranzactie, String numeATM) throws InterruptedException {
        while (tranzactii.size() == capacitate) {
            System.out.println("[" + numeATM + "] aștept loc...");
            wait();
        }

        tranzactii.add(tranzactie);
        notifyAll();
    }

    public synchronized Tranzactie extrage(ProcessorThread processor) throws InterruptedException {
        while (tranzactii.isEmpty()) {
            if (!processor.isActiv()) {
                return null;
            }
            wait();
        }

        Tranzactie tranzactie = tranzactii.poll();
        notifyAll();
        return tranzactie;
    }

    public synchronized void trezesteToateFirele() {
        notifyAll();
    }
}
