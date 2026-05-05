package com.pao.laboratory09.exercise3;

class ProcessorThread implements Runnable {
    private final CoadaTranzactii coada;
    private volatile boolean activ = true;
    private int totalProcesate = 0;

    public ProcessorThread(CoadaTranzactii coada) {
        this.coada = coada;
    }

    @Override
    public void run() {
        while (activ || true) {
            try {
                Tranzactie tranzactie = coada.extrage(this);
                if (tranzactie == null) {
                    break;
                }

                Thread.sleep(80);
                totalProcesate++;

                System.out.printf("[Processor] Factura #%d - %.2f RON | %s%n",
                        tranzactie.getId(), tranzactie.getSuma(), tranzactie.getData());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    public boolean isActiv() {
        return activ;
    }

    public void opreste() {
        activ = false;
    }

    public int getTotalProcesate() {
        return totalProcesate;
    }
}
