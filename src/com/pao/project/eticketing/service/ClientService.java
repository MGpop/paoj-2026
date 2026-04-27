package com.pao.project.eticketing.service;

import com.pao.project.eticketing.exception.ParolaIncorectaException;
import com.pao.project.eticketing.model.Client;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ClientService {
    private static ClientService instance;

    private final Set<Client> clienti;
    private final Map<Integer, Client> clientiById;
    private final Map<String, Client> clientiByEmail;

    private ClientService() {
        this.clienti = new HashSet<>();
        this.clientiById = new HashMap<>();
        this.clientiByEmail = new HashMap<>();
    }

    public static ClientService getInstance() {
        if (instance == null) {
            instance = new ClientService();
        }
        return instance;
    }

    public void adaugaClient(Client client) {
        if (client == null) {
            return;
        }

        if (!clientiById.containsKey(client.getId())
                && !clientiByEmail.containsKey(client.getEmail())) {
            clienti.add(client);
            clientiById.put(client.getId(), client);
            clientiByEmail.put(client.getEmail(), client);
        }
    }

    public void stergeClient(int id) {
        Client client = cautaClientDupaId(id);

        if (client == null) {
            return;
        }

        clienti.remove(client);
        clientiById.remove(id);
        clientiByEmail.remove(client.getEmail());
    }

    public Client cautaClientDupaId(int id) {
        return clientiById.get(id);
    }

    public Client cautaClientDupaNume(String nume) {
        if (nume == null || nume.isEmpty()) {
            return null;
        }

        for (Client client : clienti) {
            if (client.getNume().equalsIgnoreCase(nume)) {
                return client;
            }
        }

        return null;
    }

    public Set<Client> listeazaClienti() {
        return new HashSet<>(clienti);
    }

    public Client autentifica(String email, String parola) throws ParolaIncorectaException {
        Client client = clientiByEmail.get(email);

        if (client == null) {
            throw new ParolaIncorectaException("Emailul sau parola sunt incorecte.");
        }

        if (!client.verificaParola(parola)) {
            throw new ParolaIncorectaException("Emailul sau parola sunt incorecte.");
        }

        return client;
    }
}