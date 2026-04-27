package com.pao.project.eticketing.service;

import com.pao.project.eticketing.exception.NiciunEvenimentGasitException;
import com.pao.project.eticketing.model.Eveniment;
import com.pao.project.eticketing.model.Participant;
import com.pao.project.eticketing.model.TipBilet;

import java.time.LocalDateTime;
import java.util.*;

public class EvenimentService {
    private static EvenimentService instance;

    private final List<Eveniment> evenimente;
    private final Map<Integer, Eveniment> evenimenteById;

    private EvenimentService() {
        this.evenimente = new ArrayList<>();
        this.evenimenteById = new HashMap<>();
    }

    public static EvenimentService getInstance() {
        if (instance == null) {
            instance = new EvenimentService();
        }
        return instance;
    }

    public void adaugaEveniment(Eveniment eveniment) {
        if (eveniment == null) {
            return;
        }

        if (!evenimenteById.containsKey(eveniment.getId())) {
            evenimente.add(eveniment);
            evenimenteById.put(eveniment.getId(), eveniment);
        }
    }

    public void modificaEveniment(int id, String numeNou, LocalDateTime dataNoua) throws NiciunEvenimentGasitException {
        Eveniment eveniment = cautaEvenimentDupaId(id);

        if (numeNou != null && !numeNou.isEmpty()) {
            eveniment.setNume(numeNou);
        }

        if (dataNoua != null) {
            eveniment.setData(dataNoua);
        }
    }

    public void stergeEveniment(int id) throws NiciunEvenimentGasitException {
        Eveniment eveniment = cautaEvenimentDupaId(id);

        evenimente.remove(eveniment);
        evenimenteById.remove(id);
    }

    public Eveniment cautaEvenimentDupaId(int id) throws NiciunEvenimentGasitException {
        Eveniment eveniment = evenimenteById.get(id);

        if (eveniment == null) {
            throw new NiciunEvenimentGasitException("Nu există niciun eveniment cu id-ul " + id + ".");
        }

        return eveniment;
    }

    public Eveniment cautaEvenimentDupaNume(String nume) throws NiciunEvenimentGasitException {
        for (Eveniment eveniment : evenimente) {
            if (eveniment.getNume().equalsIgnoreCase(nume)) {
                return eveniment;
            }
        }

        throw new NiciunEvenimentGasitException("Nu există niciun eveniment cu numele " + nume + ".");
    }

    public List<Eveniment> listeazaEvenimente() {
        return new ArrayList<>(evenimente);
    }

    public List<Eveniment> cautaEvenimenteDupaFiltre(
            String tipEveniment,
            String numeLocatie,
            LocalDateTime data,
            Double pretMaxim,
            String numeParticipant
    ) throws NiciunEvenimentGasitException {

        List<Eveniment> rezultate = new ArrayList<>();

        for (Eveniment eveniment : evenimente) {
            boolean potrivit = true;

            if (tipEveniment != null && !tipEveniment.isEmpty()) {
                potrivit = eveniment.getTipEveniment().equalsIgnoreCase(tipEveniment);
            }

            if (potrivit && numeLocatie != null && !numeLocatie.isEmpty()) {
                potrivit = eveniment.getLocatie() != null &&
                        eveniment.getLocatie().getNume().equalsIgnoreCase(numeLocatie);
            }

            if (potrivit && data != null) {
                potrivit = eveniment.getData().toLocalDate().equals(data.toLocalDate());
            }

            if (potrivit && pretMaxim != null) {
                potrivit = areBiletSubPret(eveniment, pretMaxim);
            }

            if (potrivit && numeParticipant != null && !numeParticipant.isEmpty()) {
                potrivit = areParticipant(eveniment, numeParticipant);
            }

            if (potrivit) {
                rezultate.add(eveniment);
            }
        }

        if (rezultate.isEmpty()) {
            throw new NiciunEvenimentGasitException("Nu a fost găsit niciun eveniment pentru filtrele date.");
        }

        return rezultate;
    }

    public List<Eveniment> listeazaEvenimenteSortateDupaData() {
        List<Eveniment> sortate = new ArrayList<>(evenimente);
        Collections.sort(sortate);
        return sortate;
    }

    private boolean areBiletSubPret(Eveniment eveniment, double pretMaxim) {
        for (TipBilet tipBilet : eveniment.getTipuriBilete()) {
            if (tipBilet.getPret() <= pretMaxim) {
                return true;
            }
        }

        return false;
    }

    private boolean areParticipant(Eveniment eveniment, String numeParticipant) {
        for (Participant participant : eveniment.getParticipanti()) {
            if (participant.getNume().equalsIgnoreCase(numeParticipant)) {
                return true;
            }
        }

        return false;
    }
}