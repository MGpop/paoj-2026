package com.pao.project.eticketing.service;

import com.pao.project.eticketing.exception.StocBileteInsuficientException;
import com.pao.project.eticketing.model.Bilet;
import com.pao.project.eticketing.model.Client;
import com.pao.project.eticketing.model.Eveniment;
import com.pao.project.eticketing.model.TipBilet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BiletService {
    private static BiletService instance;

    private final List<Bilet> bilete;
    private final Map<String, Bilet> bileteByCod;
    private final Map<Integer, List<Bilet>> biletePeClient;
    private final Map<Integer, List<Client>> clientiPeEveniment;

    private BiletService() {
        this.bilete = new ArrayList<>();
        this.bileteByCod = new HashMap<>();
        this.biletePeClient = new HashMap<>();
        this.clientiPeEveniment = new HashMap<>();
    }

    public static BiletService getInstance() {
        if (instance == null) {
            instance = new BiletService();
        }
        return instance;
    }

    public List<Bilet> cumparaBilet(Client client, Eveniment eveniment, TipBilet tipBilet, int cantitate)
            throws StocBileteInsuficientException {

        if (client == null || eveniment == null || tipBilet == null || cantitate <= 0) {
            return new ArrayList<>();
        }

        if (!tipBilet.areStoc(cantitate)) {
            throw new StocBileteInsuficientException(
                    "Nu există suficiente bilete disponibile pentru tipul " + tipBilet.getNume() + "."
            );
        }

        List<Bilet> bileteCumparate = new ArrayList<>();

        for (int i = 0; i < cantitate; i++) {
            String codBilet = genereazaCodBilet(client, eveniment, tipBilet, i);
            Bilet bilet = new Bilet(codBilet, client, eveniment, tipBilet);

            bilete.add(bilet);
            bileteByCod.put(codBilet, bilet);

            biletePeClient
                    .computeIfAbsent(client.getId(), id -> new ArrayList<>())
                    .add(bilet);

            clientiPeEveniment
                    .computeIfAbsent(eveniment.getId(), id -> new ArrayList<>());

            if (!clientiPeEveniment.get(eveniment.getId()).contains(client)) {
                clientiPeEveniment.get(eveniment.getId()).add(client);
            }

            bileteCumparate.add(bilet);
        }

        tipBilet.scadeStoc(cantitate);

        return bileteCumparate;
    }

    public void anuleazaBilet(String codBilet) {
        Bilet bilet = cautaBiletDupaCod(codBilet);

        if (bilet == null) {
            return;
        }

        bilete.remove(bilet);
        bileteByCod.remove(codBilet);

        int clientId = bilet.getClient().getId();
        int evenimentId = bilet.getEveniment().getId();

        List<Bilet> bileteleClientului = biletePeClient.get(clientId);
        if (bileteleClientului != null) {
            bileteleClientului.remove(bilet);
        }

        bilet.getTipBilet().adaugaStoc(1);

        actualizeazaClientiPeEveniment(evenimentId);
    }

    public Bilet cautaBiletDupaCod(String codBilet) {
        if (codBilet == null || codBilet.isEmpty()) {
            return null;
        }

        return bileteByCod.get(codBilet);
    }

    public List<Bilet> listeazaBilete() {
        return new ArrayList<>(bilete);
    }

    public List<Bilet> listeazaBileteClient(int clientId) {
        List<Bilet> rezultat = biletePeClient.get(clientId);

        if (rezultat == null) {
            return new ArrayList<>();
        }

        return new ArrayList<>(rezultat);
    }

    public List<Client> listeazaClientiPentruEveniment(int evenimentId) {
        List<Client> rezultat = clientiPeEveniment.get(evenimentId);

        if (rezultat == null) {
            return new ArrayList<>();
        }

        return new ArrayList<>(rezultat);
    }

    private String genereazaCodBilet(Client client, Eveniment eveniment, TipBilet tipBilet, int index) {
        return "B-" +
                client.getId() + "-" +
                eveniment.getId() + "-" +
                tipBilet.getId() + "-" +
                System.nanoTime() + "-" +
                index;
    }

    private void actualizeazaClientiPeEveniment(int evenimentId) {
        List<Client> clienti = new ArrayList<>();

        for (Bilet bilet : bilete) {
            if (bilet.getEveniment().getId() == evenimentId) {
                Client client = bilet.getClient();

                if (!clienti.contains(client)) {
                    clienti.add(client);
                }
            }
        }

        if (clienti.isEmpty()) {
            clientiPeEveniment.remove(evenimentId);
        } else {
            clientiPeEveniment.put(evenimentId, clienti);
        }
    }
}