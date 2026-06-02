package com.pao.project.eticketing;

import com.pao.project.eticketing.exception.*;
import com.pao.project.eticketing.model.*;
import com.pao.project.eticketing.service.*;
import com.pao.project.eticketing.repository.*;
import com.pao.project.eticketing.util.*;
import com.pao.project.eticketing.ui.ConsoleMenu;

import java.time.LocalDateTime;
import java.util.Scanner;

public class Main {

    private static AuditService auditService;

    private static ClientRepository clientRepository;
    private static LocatieRepository locatieRepository;
    private static EvenimentRepository evenimentRepository;
    private static TipBiletRepository tipBiletRepository;
    private static BiletRepository biletRepository;
    private static RaportRepository raportRepository;

    public static void main(String[] args) {
        DatabaseInitializer.initialize();

        DataSeeder.seed();

        initializeRepositories();

        auditService.logAction("pornire_aplicatie");

        System.out.println("=== INIȚIALIZAREA BAZEI DE DATE ===");
        System.out.println("Baza de date a fost inițializată cu succes.");

        Scanner scanner = new Scanner(System.in);

        System.out.println();
        System.out.println("1. Rulare automată (Etapa II)");
        System.out.println("2. Meniu interactiv");
        System.out.print("Alege opțiunea: ");

        int optiune = scanner.nextInt();

        switch (optiune) {
            case 1 -> ruleazaDemoAutomat();
            case 2 -> ConsoleMenu.run();
            default -> System.out.println("Opțiune invalidă.");
        }
    }

    private static void ruleazaDemoAutomat() {
        demoAutentificareClient();

        demoCrudClient();
        demoCrudLocatie();
        demoCrudEveniment();
        demoCrudTipBilet();
        demoCrudBilet();

        demoCautareEvenimenteDupaFiltre();
        demoEvenimenteSortate();
        demoCautareLocatiiDupaFiltre();
        demoClientiPentruEveniment();
        demoBiletePentruClient();

        demoTranzactieCumparareBilet();

        demoJoinuri();
    }

    private static void initializeRepositories() {
        auditService = AuditService.getInstance();

        clientRepository = ApplicationContext.CLIENT_REPOSITORY;
        locatieRepository = ApplicationContext.LOCATIE_REPOSITORY;
        evenimentRepository = ApplicationContext.EVENIMENT_REPOSITORY;
        tipBiletRepository = ApplicationContext.TIP_BILET_REPOSITORY;
        biletRepository = ApplicationContext.BILET_REPOSITORY;
        raportRepository = ApplicationContext.RAPORT_REPOSITORY;
    }

    private static void demoAutentificareClient() {
        auditService.logAction("autentifica_client");

        System.out.println("\n=== AUTENTIFICARE CLIENT ===");

        System.out.println(
                clientRepository.autentifica(
                        "ana@test.ro",
                        "ana123"
                )
        );

        System.out.println(
                clientRepository.autentifica(
                        "ana@test.ro",
                        "parolaGresita"
                )
        );
    }

    private static void demoCrudClient() {
        auditService.logAction("crud_client");

        System.out.println("\n=== CRUD CLIENT ===");

        Client client = new Client(100, "Client Test", "client@test.ro", "test123");
        clientRepository.save(client);
        System.out.println("CREATE: " + clientRepository.findById(100));

        client.setNume("Client Test Modificat");
        client.setEmail("client.modificat@test.ro");
        clientRepository.update(client);
        System.out.println("UPDATE: " + clientRepository.findById(100));

        System.out.println("READ ALL: " + clientRepository.findAll());

        clientRepository.delete(100);
        System.out.println("DELETE: " + clientRepository.findById(100));
    }

    private static void demoCrudLocatie() {
        auditService.logAction("crud_locatie");

        System.out.println("\n=== CRUD LOCAȚIE ===");

        Locatie locatie = new Locatie(
                100,
                "Locație Test",
                new Adresa("Strada Test", "10", "București", "București"),
                500
        );

        locatieRepository.save(locatie);
        System.out.println("CREATE: " + locatieRepository.findById(100));

        locatie.setNume("Locație Test Modificată");
        locatie.setCapacitate(800);
        locatieRepository.update(locatie);
        System.out.println("UPDATE: " + locatieRepository.findById(100));

        System.out.println("READ ALL: " + locatieRepository.findAll());

        locatieRepository.delete(100);
        System.out.println("DELETE: " + locatieRepository.findById(100));
    }

    private static void demoCrudEveniment() {
        auditService.logAction("crud_eveniment");

        System.out.println("\n=== CRUD EVENIMENT ===");

        Locatie locatie = locatieRepository.findById(1).orElse(null);

        Eveniment eveniment = new Concert(
                100,
                "Eveniment Test",
                LocalDateTime.of(2026, 8, 1, 20, 0),
                locatie,
                "Jazz"
        );

        evenimentRepository.save(eveniment);
        System.out.println("CREATE: " + evenimentRepository.findById(100));

        eveniment.setNume("Eveniment Test Modificat");
        eveniment.setData(LocalDateTime.of(2026, 8, 2, 21, 0));
        evenimentRepository.update(eveniment);
        System.out.println("UPDATE: " + evenimentRepository.findById(100));

        System.out.println("READ ALL: " + evenimentRepository.findAll());

        evenimentRepository.delete(100);
        System.out.println("DELETE: " + evenimentRepository.findById(100));
    }

    private static void demoCrudTipBilet() {
        auditService.logAction("crud_tip_bilet");

        System.out.println("\n=== CRUD TIP BILET ===");

        TipBilet tipBilet = new TipBilet(100, "Test", 99.99, 30);

        tipBiletRepository.save(tipBilet, 1);
        System.out.println("CREATE: " + tipBiletRepository.findById(100));

        tipBilet.setNume("Test Modificat");
        tipBilet.setPret(149.99);
        tipBilet.setStocDisponibil(40);
        tipBiletRepository.update(tipBilet);
        System.out.println("UPDATE: " + tipBiletRepository.findById(100));

        System.out.println("READ ALL: " + tipBiletRepository.findAll());

        tipBiletRepository.delete(100);
        System.out.println("DELETE: " + tipBiletRepository.findById(100));
    }

    private static void demoCrudBilet() {
        auditService.logAction("crud_bilet");

        System.out.println("\n=== CRUD BILET ===");

        Client client1 = clientRepository.findById(1).orElse(null);
        Client client2 = clientRepository.findById(2).orElse(null);

        Eveniment eveniment1 = evenimentRepository.findById(1).orElse(null);
        Eveniment eveniment2 = evenimentRepository.findById(2).orElse(null);

        TipBilet tipBilet1 = tipBiletRepository.findById(1).orElse(null);
        TipBilet tipBilet2 = tipBiletRepository.findById(3).orElse(null);

        Bilet bilet = new Bilet("B-CRUD-TEST", client1, eveniment1, tipBilet1);

        biletRepository.save(bilet);
        System.out.println("CREATE: " + biletRepository.findById("B-CRUD-TEST"));

        Bilet biletModificat = new Bilet(
                "B-CRUD-TEST",
                client2,
                eveniment2,
                tipBilet2
        );

        biletRepository.update(biletModificat);
        System.out.println("UPDATE: " + biletRepository.findById("B-CRUD-TEST"));

        System.out.println("READ ALL: " + biletRepository.findAll());

        biletRepository.delete("B-CRUD-TEST");
        System.out.println("DELETE: " + biletRepository.findById("B-CRUD-TEST"));
    }

    private static void demoCautareEvenimenteDupaFiltre() {
        auditService.logAction("cauta_evenimente_dupa_filtre");

        System.out.println("\n=== CĂUTARE EVENIMENTE DUPĂ FILTRE ===");

        System.out.println("\nConcerte:");
        evenimentRepository
                .cautaDupaFiltre("Concert", null, null, null)
                .forEach(System.out::println);

        System.out.println("\nEvenimente din locația 1:");
        evenimentRepository
                .cautaDupaFiltre(null, 1, null, null)
                .forEach(System.out::println);

        System.out.println("\nEvenimente cu bilet <= 200:");
        evenimentRepository
                .cautaDupaFiltre(null, null, null, 200.0)
                .forEach(System.out::println);
    }

    private static void demoEvenimenteSortate() {
        auditService.logAction("listeaza_evenimente_sortate");

        System.out.println("\n=== EVENIMENTE SORTATE DUPĂ DATĂ ===");

        evenimentRepository
                .findAllSortateDupaData()
                .forEach(System.out::println);
    }

    private static void demoCautareLocatiiDupaFiltre() {
        auditService.logAction("listeaza_locatii_dupa_filtre");

        System.out.println("\n=== LOCAȚII DUPĂ FILTRE ===");

        System.out.println("\nLocații din București:");
        locatieRepository
                .cautaDupaFiltre("București", null)
                .forEach(System.out::println);

        System.out.println("\nLocații care găzduiesc concerte:");
        locatieRepository
                .cautaDupaFiltre(null, "Concert")
                .forEach(System.out::println);
    }

    private static void demoClientiPentruEveniment() {
        auditService.logAction("listeaza_clienti_pentru_eveniment");

        System.out.println("\n=== CLIENȚI PENTRU EVENIMENT ===");

        raportRepository.afiseazaClientiPentruEveniment(1);
    }

    private static void demoBiletePentruClient() {
        auditService.logAction("listeaza_bilete_client");

        System.out.println("\n=== BILETE PENTRU CLIENT ===");

        raportRepository.afiseazaBiletePentruClient(1);
    }

    private static void demoTranzactieCumparareBilet() {
        auditService.logAction("tranzactie_cumparare_bilet");

        System.out.println("\n=== TRANZACȚIE CUMPĂRARE BILET ===");

        Client client = clientRepository.findById(1).orElse(null);
        Eveniment eveniment = evenimentRepository.findById(1).orElse(null);
        TipBilet tipBilet = tipBiletRepository.findById(1).orElse(null);

        System.out.println("STOC ÎNAINTE: " + tipBiletRepository.findById(1));

        Bilet bilet = new Bilet(
                "B-TRANZACTIE-TEST",
                client,
                eveniment,
                tipBilet
        );

        biletRepository.saveCuTranzactie(bilet);

        System.out.println("BILET CUMPĂRAT: " + biletRepository.findById("B-TRANZACTIE-TEST"));
        System.out.println("STOC DUPĂ: " + tipBiletRepository.findById(1));

        biletRepository.delete("B-TRANZACTIE-TEST");
    }

    private static void demoJoinuri() {
        auditService.logAction("interogari_join");

        System.out.println("\n=== JOIN 1: BILETE CU DETALII ===");
        System.out.println("Afișați detalii despre toate biletele cumpărate și despre cumpărătorii și evenimentele asociate acestora:\n");
        raportRepository.afiseazaBileteCuDetalii();

        System.out.println("\n=== JOIN 2: EVENIMENTE CU LOCAȚII ===");
        System.out.println("Afișați toate evenimentele împreună cu orașul și capacitatea locației:\n");
        raportRepository.afiseazaEvenimenteCuLocatii();

        System.out.println("\n=== JOIN 3: VENITURI PE EVENIMENTE ===");
        System.out.println("Calculați ce venit a fost obținut în urma vânzării biletelor pentru fiecare eveniment:\n");
        raportRepository.afiseazaVenituriPeEvenimente();
    }
}