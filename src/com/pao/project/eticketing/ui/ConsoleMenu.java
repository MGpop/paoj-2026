package com.pao.project.eticketing.ui;

import com.pao.project.eticketing.model.*;
import com.pao.project.eticketing.repository.*;
import com.pao.project.eticketing.service.AuditService;
import com.pao.project.eticketing.util.ApplicationContext;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ConsoleMenu {
    private static final Scanner scanner = new Scanner(System.in);

    private static final String ADMIN_EMAIL = "admin@eticketing.ro";
    private static final String ADMIN_PAROLA = "admin123";

    private static final AuditService auditService = AuditService.getInstance();

    private static final ClientRepository clientRepository = ApplicationContext.CLIENT_REPOSITORY;
    private static final LocatieRepository locatieRepository = ApplicationContext.LOCATIE_REPOSITORY;
    private static final EvenimentRepository evenimentRepository = ApplicationContext.EVENIMENT_REPOSITORY;
    private static final TipBiletRepository tipBiletRepository = ApplicationContext.TIP_BILET_REPOSITORY;
    private static final BiletRepository biletRepository = ApplicationContext.BILET_REPOSITORY;
    private static final RaportRepository raportRepository = ApplicationContext.RAPORT_REPOSITORY;

    private ConsoleMenu() {
    }

    public static void run() {
        boolean ruleaza = true;

        while (ruleaza) {
            System.out.println("\n=== MENIU INTERACTIV JDBC ===");
            System.out.println("1. Autentificare admin");
            System.out.println("2. Autentificare client");
            System.out.println("3. Înregistrare client");
            System.out.println("0. Ieșire");
            System.out.print("Alege opțiunea: ");

            int optiune = citesteInt();

            switch (optiune) {
                case 1 -> autentificareAdmin();
                case 2 -> autentificareClient();
                case 3 -> inregistrareClient();
                case 0 -> ruleaza = false;
                default -> System.out.println("Opțiune invalidă.");
            }
        }
    }

    private static void autentificareAdmin() {
        auditService.logAction("autentificare_admin");

        System.out.print("Email admin: ");
        String email = scanner.nextLine();

        System.out.print("Parolă admin: ");
        String parola = scanner.nextLine();

        if (ADMIN_EMAIL.equals(email) && ADMIN_PAROLA.equals(parola)) {
            System.out.println("Autentificare admin reușită.");
            meniuAdmin();
        } else {
            System.out.println("Date admin greșite.");
        }
    }

    private static void autentificareClient() {
        auditService.logAction("autentifica_client");

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Parolă: ");
        String parola = scanner.nextLine();

        Optional<Client> client = clientRepository.autentifica(email, parola);

        if (client.isPresent()) {
            System.out.println("Bine ai venit, " + client.get().getNume() + "!");
            meniuClient(client.get());
        } else {
            System.out.println("Emailul sau parola sunt incorecte.");
        }
    }

    private static void inregistrareClient() {
        auditService.logAction("inregistreaza_client");

        System.out.print("Id client: ");
        int id = citesteInt();

        System.out.print("Nume: ");
        String nume = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Parolă: ");
        String parola = scanner.nextLine();

        Client client = new Client(id, nume, email, parola);
        clientRepository.save(client);

        System.out.println("Client înregistrat.");
    }

    private static void meniuAdmin() {
        boolean logat = true;

        while (logat) {
            System.out.println("\n=== MENIU ADMIN JDBC ===");
            System.out.println("1. Adaugă eveniment");
            System.out.println("2. Modifică eveniment");
            System.out.println("3. Șterge eveniment");
            System.out.println("4. Caută evenimente după filtre");
            System.out.println("5. Listează evenimente sortate după dată");
            System.out.println("6. Adaugă locație");
            System.out.println("7. Modifică locație");
            System.out.println("8. Șterge locație");
            System.out.println("9. Listează locații după filtre");
            System.out.println("10. Listează clienți pentru eveniment");
            System.out.println("11. Listează bilete pentru client");
            System.out.println("12. Adaugă tip bilet");
            System.out.println("13. Modifică tip bilet");
            System.out.println("14. Listează toate biletele");
            System.out.println("15. Rapoarte JOIN");
            System.out.println("0. Logout");
            System.out.print("Alege opțiunea: ");

            int optiune = citesteInt();

            switch (optiune) {
                case 1 -> adaugaEveniment();
                case 2 -> modificaEveniment();
                case 3 -> stergeEveniment();
                case 4 -> cautaEvenimenteDupaFiltre();
                case 5 -> listeazaEvenimenteSortate();
                case 6 -> adaugaLocatie();
                case 7 -> modificaLocatie();
                case 8 -> stergeLocatie();
                case 9 -> listeazaLocatiiDupaFiltre();
                case 10 -> clientiPentruEveniment();
                case 11 -> biletePentruClient();
                case 12 -> adaugaTipBilet();
                case 13 -> modificaTipBilet();
                case 14 -> afiseazaLista(biletRepository.findAll());
                case 15 -> rapoarteJoin();
                case 0 -> logat = false;
                default -> System.out.println("Opțiune invalidă.");
            }
        }
    }

    private static void meniuClient(Client client) {
        boolean logat = true;

        while (logat) {
            System.out.println("\n=== MENIU CLIENT JDBC ===");
            System.out.println("1. Caută evenimente după filtre");
            System.out.println("2. Listează evenimente sortate după dată");
            System.out.println("3. Listează locații după filtre");
            System.out.println("4. Cumpără bilet");
            System.out.println("5. Listează biletele mele");
            System.out.println("0. Logout");
            System.out.print("Alege opțiunea: ");

            int optiune = citesteInt();

            switch (optiune) {
                case 1 -> cautaEvenimenteDupaFiltre();
                case 2 -> listeazaEvenimenteSortate();
                case 3 -> listeazaLocatiiDupaFiltre();
                case 4 -> cumparaBilet(client);
                case 5 -> raportRepository.afiseazaBiletePentruClient(client.getId());
                case 0 -> logat = false;
                default -> System.out.println("Opțiune invalidă.");
            }
        }
    }

    private static void adaugaEveniment() {
        auditService.logAction("adauga_eveniment");

        System.out.print("Id eveniment: ");
        int id = citesteInt();

        System.out.print("Nume eveniment: ");
        String nume = scanner.nextLine();

        System.out.print("Dată eveniment (yyyy-mm-ddThh:mm): ");
        LocalDateTime data = LocalDateTime.parse(scanner.nextLine());

        System.out.print("Id locație: ");
        int locatieId = citesteInt();

        Locatie locatie = locatieRepository.findById(locatieId).orElse(null);

        if (locatie == null) {
            System.out.println("Locația nu există.");
            return;
        }

        System.out.println("Tip eveniment:");
        System.out.println("1. Concert");
        System.out.println("2. Piesă de teatru");
        int tip = citesteInt();

        Eveniment eveniment;

        if (tip == 1) {
            System.out.print("Gen muzical: ");
            String gen = scanner.nextLine();

            eveniment = new Concert(id, nume, data, locatie, gen);
        } else {
            System.out.print("Autor: ");
            String autor = scanner.nextLine();

            System.out.print("Regizor: ");
            String regizor = scanner.nextLine();

            System.out.print("Durată minute: ");
            int durata = citesteInt();

            eveniment = new PiesaTeatru(id, nume, data, locatie, autor, regizor, durata);
        }

        evenimentRepository.save(eveniment);
        System.out.println("Eveniment adăugat.");
    }

    private static void modificaEveniment() {
        auditService.logAction("modifica_eveniment");

        System.out.print("Id eveniment: ");
        int id = citesteInt();

        Eveniment eveniment = evenimentRepository.findById(id).orElse(null);

        if (eveniment == null) {
            System.out.println("Evenimentul nu există.");
            return;
        }

        System.out.print("Nume nou: ");
        eveniment.setNume(scanner.nextLine());

        System.out.print("Dată nouă (yyyy-mm-ddThh:mm): ");
        eveniment.setData(LocalDateTime.parse(scanner.nextLine()));

        evenimentRepository.update(eveniment);
        System.out.println("Eveniment modificat.");
    }

    private static void stergeEveniment() {
        auditService.logAction("sterge_eveniment");

        System.out.print("Id eveniment: ");
        int id = citesteInt();

        evenimentRepository.delete(id);
        System.out.println("Eveniment șters.");
    }

    private static void cautaEvenimenteDupaFiltre() {
        auditService.logAction("cauta_evenimente_dupa_filtre");

        System.out.print("Tip eveniment sau gol: ");
        String tip = citesteStringSauNull();

        System.out.print("Id locație sau 0: ");
        int locatieIdCitit = citesteInt();
        Integer locatieId = locatieIdCitit == 0 ? null : locatieIdCitit;

        System.out.print("Data yyyy-mm-dd sau gol: ");
        String data = citesteStringSauNull();

        System.out.print("Preț maxim sau 0: ");
        double pretCitit = citesteDouble();
        Double pretMaxim = pretCitit == 0 ? null : pretCitit;

        List<Eveniment> evenimente = evenimentRepository.cautaDupaFiltre(
                tip,
                locatieId,
                data,
                pretMaxim
        );

        afiseazaLista(evenimente);
    }

    private static void listeazaEvenimenteSortate() {
        auditService.logAction("listeaza_evenimente_sortate");
        afiseazaLista(evenimentRepository.findAllSortateDupaData());
    }

    private static void adaugaLocatie() {
        auditService.logAction("adauga_locatie");

        System.out.print("Id locație: ");
        int id = citesteInt();

        System.out.print("Nume locație: ");
        String nume = scanner.nextLine();

        Adresa adresa = citesteAdresa();

        System.out.print("Capacitate: ");
        int capacitate = citesteInt();

        Locatie locatie = new Locatie(id, nume, adresa, capacitate);
        locatieRepository.save(locatie);

        System.out.println("Locație adăugată.");
    }

    private static void modificaLocatie() {
        auditService.logAction("modifica_locatie");

        System.out.print("Id locație: ");
        int id = citesteInt();

        Locatie locatie = locatieRepository.findById(id).orElse(null);

        if (locatie == null) {
            System.out.println("Locația nu există.");
            return;
        }

        System.out.print("Nume nou: ");
        locatie.setNume(scanner.nextLine());

        System.out.print("Capacitate nouă: ");
        locatie.setCapacitate(citesteInt());

        locatieRepository.update(locatie);
        System.out.println("Locație modificată.");
    }

    private static void stergeLocatie() {
        auditService.logAction("sterge_locatie");

        System.out.print("Id locație: ");
        int id = citesteInt();

        locatieRepository.delete(id);
        System.out.println("Locație ștearsă.");
    }

    private static void listeazaLocatiiDupaFiltre() {
        auditService.logAction("listeaza_locatii_dupa_filtre");

        System.out.print("Oraș sau gol: ");
        String oras = citesteStringSauNull();

        System.out.print("Tip eveniment sau gol: ");
        String tipEveniment = citesteStringSauNull();

        afiseazaLista(locatieRepository.cautaDupaFiltre(oras, tipEveniment));
    }

    private static void clientiPentruEveniment() {
        auditService.logAction("listeaza_clienti_pentru_eveniment");

        System.out.print("Id eveniment: ");
        int id = citesteInt();

        raportRepository.afiseazaClientiPentruEveniment(id);
    }

    private static void biletePentruClient() {
        auditService.logAction("listeaza_bilete_client");

        System.out.print("Id client: ");
        int id = citesteInt();

        raportRepository.afiseazaBiletePentruClient(id);
    }

    private static void adaugaTipBilet() {
        auditService.logAction("adauga_tip_bilet");

        System.out.print("Id tip bilet: ");
        int id = citesteInt();

        System.out.print("Nume tip bilet: ");
        String nume = scanner.nextLine();

        System.out.print("Preț: ");
        double pret = citesteDouble();

        System.out.print("Stoc: ");
        int stoc = citesteInt();

        System.out.print("Id eveniment: ");
        int evenimentId = citesteInt();

        TipBilet tipBilet = new TipBilet(id, nume, pret, stoc);
        tipBiletRepository.save(tipBilet, evenimentId);

        System.out.println("Tip bilet adăugat.");
    }

    private static void modificaTipBilet() {
        auditService.logAction("modifica_tip_bilet");

        System.out.print("Id tip bilet: ");
        int id = citesteInt();

        TipBilet tipBilet = tipBiletRepository.findById(id).orElse(null);

        if (tipBilet == null) {
            System.out.println("Tipul de bilet nu există.");
            return;
        }

        System.out.print("Nume nou: ");
        tipBilet.setNume(scanner.nextLine());

        System.out.print("Preț nou: ");
        tipBilet.setPret(citesteDouble());

        System.out.print("Stoc nou: ");
        tipBilet.setStocDisponibil(citesteInt());

        tipBiletRepository.update(tipBilet);
        System.out.println("Tip bilet modificat.");
    }

    private static void cumparaBilet(Client client) {
        auditService.logAction("cumpara_bilet");

        System.out.print("Id eveniment: ");
        int evenimentId = citesteInt();

        Eveniment eveniment = evenimentRepository.findById(evenimentId).orElse(null);

        if (eveniment == null) {
            System.out.println("Evenimentul nu există.");
            return;
        }

        List<TipBilet> tipuri = tipBiletRepository.findByEvenimentId(evenimentId);

        if (tipuri.isEmpty()) {
            System.out.println("Nu există tipuri de bilete pentru acest eveniment.");
            return;
        }

        System.out.println("Tipuri bilete disponibile:");
        afiseazaLista(tipuri);

        System.out.print("Id tip bilet: ");
        int tipBiletId = citesteInt();

        TipBilet tipBilet = tipBiletRepository.findById(tipBiletId).orElse(null);

        if (tipBilet == null) {
            System.out.println("Tipul de bilet nu există.");
            return;
        }

        System.out.print("Cantitate: ");
        int cantitate = citesteInt();

        for (int i = 0; i < cantitate; i++) {
            String cod = "B-MENIU-" + client.getId() + "-" + evenimentId + "-" + System.nanoTime();

            Bilet bilet = new Bilet(cod, client, eveniment, tipBilet);
            biletRepository.saveCuTranzactie(bilet);

            System.out.println("Bilet cumpărat: " + cod);
        }
    }

    private static void rapoarteJoin() {
        auditService.logAction("interogari_join");

        System.out.println("\n--- Bilete cu detalii ---");
        raportRepository.afiseazaBileteCuDetalii();

        System.out.println("\n--- Evenimente cu locații ---");
        raportRepository.afiseazaEvenimenteCuLocatii();

        System.out.println("\n--- Venituri pe evenimente ---");
        raportRepository.afiseazaVenituriPeEvenimente();
    }

    private static Adresa citesteAdresa() {
        System.out.print("Strada: ");
        String strada = scanner.nextLine();

        System.out.print("Număr: ");
        String numar = scanner.nextLine();

        System.out.print("Oraș: ");
        String oras = scanner.nextLine();

        System.out.print("Județ: ");
        String judet = scanner.nextLine();

        return new Adresa(strada, numar, oras, judet);
    }

    private static String citesteStringSauNull() {
        String text = scanner.nextLine();
        return text.isEmpty() ? null : text;
    }

    private static int citesteInt() {
        int valoare = scanner.nextInt();
        scanner.nextLine();
        return valoare;
    }

    private static double citesteDouble() {
        double valoare = scanner.nextDouble();
        scanner.nextLine();
        return valoare;
    }

    private static void afiseazaLista(Iterable<?> lista) {
        boolean gol = true;

        for (Object obiect : lista) {
            System.out.println(obiect);
            gol = false;
        }

        if (gol) {
            System.out.println("Nu există rezultate.");
        }
    }
}