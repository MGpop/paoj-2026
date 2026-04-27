package com.pao.project.eticketing;

import com.pao.project.eticketing.exception.*;
import com.pao.project.eticketing.model.*;
import com.pao.project.eticketing.service.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    private static final String ADMIN_EMAIL = "admin@eticketing.ro";
    private static final String ADMIN_PAROLA = "admin123";

    private static final EvenimentService evenimentService = EvenimentService.getInstance();
    private static final LocatieService locatieService = LocatieService.getInstance();
    private static final ClientService clientService = ClientService.getInstance();
    private static final BiletService biletService = BiletService.getInstance();

    public static void main(String[] args) {
        incarcaDateInitiale();

        boolean ruleaza = true;

        while (ruleaza) {
            System.out.println("\n~~~ E-TICKETING ~~~");
            System.out.println("1. Autentificare admin");
            System.out.println("2. Autentificare client");
            System.out.println("3. Înregistrare client nou");
            System.out.println("0. Ieșire");
            System.out.print("Alege opțiunea: ");

            int optiune = citesteInt();

            switch (optiune) {
                case 1:
                    autentificareAdmin();
                    break;
                case 2:
                    autentificareClient();
                    break;
                case 3:
                    inregistrareClient();
                    break;
                case 0:
                    ruleaza = false;
                    break;
                default:
                    System.out.println("Opțiune invalidă.");
            }
        }

        System.out.println("Aplicația s-a închis.");
    }

    private static void autentificareAdmin() {
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
        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Parolă: ");
        String parola = scanner.nextLine();

        try {
            Client client = clientService.autentifica(email, parola);
            System.out.println("Bine ai venit, " + client.getNume() + "!");
            meniuClient(client);
        } catch (ParolaIncorectaException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void inregistrareClient() {
        System.out.print("Id client: ");
        int id = citesteInt();

        System.out.print("Nume: ");
        String nume = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Parolă: ");
        String parola = scanner.nextLine();

        Client client = new Client(id, nume, email, parola);
        clientService.adaugaClient(client);

        System.out.println("Client înregistrat.");
    }

    private static void meniuAdmin() {
        boolean logat = true;

        while (logat) {
            System.out.println("\n~~~ MENIU ADMIN ~~~");
            System.out.println("1. Adaugă eveniment");
            System.out.println("2. Modifică eveniment");
            System.out.println("3. Șterge eveniment");
            System.out.println("4. Caută eveniment după id");
            System.out.println("5. Caută eveniment după nume");
            System.out.println("6. Listează toate evenimentele");
            System.out.println("7. Caută evenimente după filtre");
            System.out.println("8. Listează evenimente sortate după dată");
            System.out.println("9. Adaugă locație");
            System.out.println("10. Modifică locație");
            System.out.println("11. Șterge locație");
            System.out.println("12. Caută locație după id");
            System.out.println("13. Caută locație după nume");
            System.out.println("14. Listează toate locațiile");
            System.out.println("15. Listează locații după oraș");
            System.out.println("16. Listează toți clienții");
            System.out.println("17. Listează toate biletele");
            System.out.println("18. Listează biletele unui client");
            System.out.println("19. Listează clienții pentru un eveniment");
            System.out.println("0. Logout");
            System.out.print("Alege opțiunea: ");

            int optiune = citesteInt();

            switch (optiune) {
                case 1:
                    adaugaEvenimentDinConsola();
                    break;
                case 2:
                    modificaEvenimentDinConsola();
                    break;
                case 3:
                    stergeEvenimentDinConsola();
                    break;
                case 4:
                    cautaEvenimentDupaIdDinConsola();
                    break;
                case 5:
                    cautaEvenimentDupaNumeDinConsola();
                    break;
                case 6:
                    afiseazaLista(evenimentService.listeazaEvenimente());
                    break;
                case 7:
                    cautaEvenimenteDupaFiltreDinConsola();
                    break;
                case 8:
                    afiseazaLista(evenimentService.listeazaEvenimenteSortateDupaData());
                    break;
                case 9:
                    adaugaLocatieDinConsola();
                    break;
                case 10:
                    modificaLocatieDinConsola();
                    break;
                case 11:
                    stergeLocatieDinConsola();
                    break;
                case 12:
                    cautaLocatieDupaIdDinConsola();
                    break;
                case 13:
                    cautaLocatieDupaNumeDinConsola();
                    break;
                case 14:
                    afiseazaLista(locatieService.listeazaLocatii());
                    break;
                case 15:
                    listeazaLocatiiDupaOrasDinConsola();
                    break;
                case 16:
                    afiseazaLista(clientService.listeazaClienti());
                    break;
                case 17:
                    afiseazaLista(biletService.listeazaBilete());
                    break;
                case 18:
                    listeazaBileteClientDinConsola();
                    break;
                case 19:
                    listeazaClientiPentruEvenimentDinConsola();
                    break;
                case 0:
                    logat = false;
                    break;
                default:
                    System.out.println("Opțiune invalidă.");
            }
        }
    }

    private static void meniuClient(Client client) {
        boolean logat = true;

        while (logat) {
            System.out.println("\n~~~ MENIU CLIENT ~~~");
            System.out.println("1. Caută evenimente după filtre");
            System.out.println("2. Listează evenimente sortate după dată");
            System.out.println("3. Listează locații după oraș");
            System.out.println("4. Cumpără bilete");
            System.out.println("5. Listează biletele mele");
            System.out.println("0. Logout");
            System.out.print("Alege opțiunea: ");

            int optiune = citesteInt();

            switch (optiune) {
                case 1:
                    cautaEvenimenteDupaFiltreDinConsola();
                    break;
                case 2:
                    afiseazaLista(evenimentService.listeazaEvenimenteSortateDupaData());
                    break;
                case 3:
                    listeazaLocatiiDupaOrasDinConsola();
                    break;
                case 4:
                    cumparaBileteDinConsola(client);
                    break;
                case 5:
                    afiseazaLista(biletService.listeazaBileteClient(client.getId()));
                    break;
                case 0:
                    logat = false;
                    break;
                default:
                    System.out.println("Opțiune invalidă.");
            }
        }
    }

    private static void adaugaEvenimentDinConsola() {
        System.out.print("Id eveniment: ");
        int id = citesteInt();

        System.out.print("Nume eveniment: ");
        String nume = scanner.nextLine();

        System.out.print("Dată eveniment (format yyyy-mm-ddThh:mm): ");
        LocalDateTime data = LocalDateTime.parse(scanner.nextLine());

        System.out.print("Id locație: ");
        int idLocatie = citesteInt();

        Locatie locatie = locatieService.cautaLocatieDupaId(idLocatie);
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

        adaugaTipuriBileteLaEveniment(eveniment);
        evenimentService.adaugaEveniment(eveniment);

        System.out.println("Eveniment adăugat.");
    }

    private static void adaugaTipuriBileteLaEveniment(Eveniment eveniment) {
        System.out.print("Câte tipuri de bilete vrei să adaugi? ");
        int nr = citesteInt();

        for (int i = 0; i < nr; i++) {
            System.out.print("Id tip bilet: ");
            int id = citesteInt();

            System.out.print("Nume tip bilet: ");
            String nume = scanner.nextLine();

            System.out.print("Preț: ");
            double pret = citesteDouble();

            System.out.print("Stoc disponibil: ");
            int stoc = citesteInt();

            eveniment.adaugaTipBilet(new TipBilet(id, nume, pret, stoc));
        }
    }

    private static void modificaEvenimentDinConsola() {
        try {
            System.out.print("Id eveniment: ");
            int id = citesteInt();

            System.out.print("Nume nou: ");
            String numeNou = scanner.nextLine();

            System.out.print("Dată nouă (format yyyy-mm-ddThh:mm): ");
            LocalDateTime dataNoua = LocalDateTime.parse(scanner.nextLine());

            evenimentService.modificaEveniment(id, numeNou, dataNoua);
            System.out.println("Eveniment modificat.");
        } catch (NiciunEvenimentGasitException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void stergeEvenimentDinConsola() {
        try {
            System.out.print("Id eveniment: ");
            int id = citesteInt();

            evenimentService.stergeEveniment(id);
            System.out.println("Eveniment șters.");
        } catch (NiciunEvenimentGasitException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void cautaEvenimentDupaIdDinConsola() {
        try {
            System.out.print("Id eveniment: ");
            int id = citesteInt();

            System.out.println(evenimentService.cautaEvenimentDupaId(id));
        } catch (NiciunEvenimentGasitException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void cautaEvenimentDupaNumeDinConsola() {
        try {
            System.out.print("Nume eveniment: ");
            String nume = scanner.nextLine();

            System.out.println(evenimentService.cautaEvenimentDupaNume(nume));
        } catch (NiciunEvenimentGasitException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void cautaEvenimenteDupaFiltreDinConsola() {
        try {
            System.out.print("Tip eveniment sau null: ");
            String tip = citesteStringSauNull();

            System.out.print("Nume locație sau null: ");
            String locatie = citesteStringSauNull();

            System.out.print("Data yyyy-mm-ddThh:mm sau null: ");
            String dataText = scanner.nextLine();
            LocalDateTime data = dataText.isEmpty() ? null : LocalDateTime.parse(dataText);

            System.out.print("Preț maxim sau null: ");
            String pretText = scanner.nextLine();
            Double pret = pretText.isEmpty() ? null : Double.parseDouble(pretText);

            System.out.print("Nume participant sau null: ");
            String participant = citesteStringSauNull();

            List<Eveniment> rezultate = evenimentService.cautaEvenimenteDupaFiltre(
                    tip, locatie, data, pret, participant
            );

            afiseazaLista(rezultate);
        } catch (NiciunEvenimentGasitException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void adaugaLocatieDinConsola() {
        System.out.print("Id locație: ");
        int id = citesteInt();

        System.out.print("Nume locație: ");
        String nume = scanner.nextLine();

        Adresa adresa = citesteAdresa();

        System.out.print("Capacitate: ");
        int capacitate = citesteInt();

        Locatie locatie = new Locatie(id, nume, adresa, capacitate);

        System.out.print("Câte tipuri de evenimente permite locația? ");
        int nr = citesteInt();

        for (int i = 0; i < nr; i++) {
            System.out.print("Tip permis: ");
            locatie.adaugaTipEvenimentPermis(scanner.nextLine());
        }

        locatieService.adaugaLocatie(locatie);
        System.out.println("Locație adăugată.");
    }

    private static void modificaLocatieDinConsola() {
        System.out.print("Id locație: ");
        int id = citesteInt();

        System.out.print("Nume nou: ");
        String nume = scanner.nextLine();

        System.out.println("Adresă nouă:");
        Adresa adresa = citesteAdresa();

        System.out.print("Capacitate nouă: ");
        int capacitate = citesteInt();

        locatieService.modificaLocatie(id, nume, adresa, capacitate);
        System.out.println("Locație modificată cu succes.");
    }

    private static void stergeLocatieDinConsola() {
        System.out.print("Id locație: ");
        int id = citesteInt();

        locatieService.stergeLocatie(id);
        System.out.println("Locație ștearsă cu succes.");
    }

    private static void cautaLocatieDupaIdDinConsola() {
        System.out.print("Id locație: ");
        int id = citesteInt();

        Locatie locatie = locatieService.cautaLocatieDupaId(id);
        System.out.println(locatie != null ? locatie : "Locația nu există.");
    }

    private static void cautaLocatieDupaNumeDinConsola() {
        System.out.print("Nume locație: ");
        String nume = scanner.nextLine();

        Locatie locatie = locatieService.cautaLocatieDupaNume(nume);
        System.out.println(locatie != null ? locatie : "Locația nu există.");
    }

    private static void listeazaLocatiiDupaOrasDinConsola() {
        System.out.print("Oraș: ");
        String oras = scanner.nextLine();

        afiseazaLista(locatieService.listeazaLocatiiDupaOras(oras));
    }

    private static void listeazaBileteClientDinConsola() {
        System.out.print("Id client: ");
        int id = citesteInt();

        afiseazaLista(biletService.listeazaBileteClient(id));
    }

    private static void listeazaClientiPentruEvenimentDinConsola() {
        System.out.print("Id eveniment: ");
        int id = citesteInt();

        afiseazaLista(biletService.listeazaClientiPentruEveniment(id));
    }

    private static void cumparaBileteDinConsola(Client client) {
        try {
            System.out.print("Id eveniment: ");
            int idEveniment = citesteInt();

            Eveniment eveniment = evenimentService.cautaEvenimentDupaId(idEveniment);

            List<TipBilet> tipuri = eveniment.getTipuriBilete();

            if (tipuri.isEmpty()) {
                System.out.println("Evenimentul nu are tipuri de bilete disponibile.");
                return;
            }

            System.out.println("Tipuri bilete disponibile:");
            for (TipBilet tipBilet : tipuri) {
                System.out.println(tipBilet);
            }

            System.out.print("Id tip bilet: ");
            int idTipBilet = citesteInt();

            TipBilet tipAles = null;
            for (TipBilet tipBilet : tipuri) {
                if (tipBilet.getId() == idTipBilet) {
                    tipAles = tipBilet;
                    break;
                }
            }

            if (tipAles == null) {
                System.out.println("Tipul de bilet nu există pentru acest eveniment.");
                return;
            }

            System.out.print("Cantitate: ");
            int cantitate = citesteInt();

            List<Bilet> bileteCumparate = biletService.cumparaBilet(client, eveniment, tipAles, cantitate);

            System.out.println("Bilete cumpărate:");
            afiseazaLista(bileteCumparate);

        } catch (NiciunEvenimentGasitException | StocBileteInsuficientException e) {
            System.out.println(e.getMessage());
        }
    }

    private static Adresa citesteAdresa() {
        System.out.print("Strada: ");
        String strada = scanner.nextLine();

        System.out.print("Numărul: ");
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

    private static void incarcaDateInitiale() {
        Adresa adresa1 = new Adresa("Bd. Basarabia", "37-39", "București", "București");
        Adresa adresa2 = new Adresa("Str. Franklin", "1-3", "București", "București");
        Adresa adresa3 = new Adresa("Piața Victoriei", "5", "Timișoara", "Timiș");

        Locatie locatie1 = new Locatie(1, "Arena Națională", adresa1, 55000);
        locatie1.adaugaTipEvenimentPermis("Concert");

        Locatie locatie2 = new Locatie(2, "Ateneul Român", adresa2, 800);
        locatie2.adaugaTipEvenimentPermis("Concert");

        Locatie locatie3 = new Locatie(3, "Teatrul Național Timișoara", adresa3, 700);
        locatie3.adaugaTipEvenimentPermis("Piesă de teatru");

        locatieService.adaugaLocatie(locatie1);
        locatieService.adaugaLocatie(locatie2);
        locatieService.adaugaLocatie(locatie3);

        Solist solist = new Solist(1, "Andrei Pop", "Solist român", "Pop", "Voce");
        Trupa trupa = new Trupa(2, "The Echoes", "Trupa rock alternativă", "Rock", 4);
        Orchestra orchestra = new Orchestra(3, "Orchestra București", "Orchestră simfonică", "Clasic", "Simfonică");

        Concert concert1 = new Concert(
                1,
                "Concert The Echoes",
                LocalDateTime.of(2026, 5, 20, 20, 0),
                locatie1,
                "Rock"
        );
        concert1.adaugaParticipant(trupa);
        concert1.adaugaTipBilet(new TipBilet(1, "Standard", 250, 100));
        concert1.adaugaTipBilet(new TipBilet(2, "VIP", 600, 20));

        Concert concert2 = new Concert(
                2,
                "Seara Simfonică",
                LocalDateTime.of(2026, 6, 10, 19, 0),
                locatie2,
                "Clasic"
        );
        concert2.adaugaParticipant(orchestra);
        concert2.adaugaParticipant(solist);
        concert2.adaugaTipBilet(new TipBilet(3, "Balcon", 120, 50));
        concert2.adaugaTipBilet(new TipBilet(4, "Lojă", 300, 10));

        PiesaTeatru piesa = new PiesaTeatru(
                3,
                "O scrisoare pierdută",
                LocalDateTime.of(2026, 5, 30, 18, 30),
                locatie3,
                "I. L. Caragiale",
                "Maria Ionescu",
                120
        );
        piesa.adaugaTipBilet(new TipBilet(5, "Standard", 80, 60));
        piesa.adaugaTipBilet(new TipBilet(6, "Premium", 150, 15));

        evenimentService.adaugaEveniment(concert1);
        evenimentService.adaugaEveniment(concert2);
        evenimentService.adaugaEveniment(piesa);

        Client client1 = new Client(1, "Ana Popescu", "ana@test.ro", "ana123");
        Client client2 = new Client(2, "Mihai Ionescu", "mihai@test.ro", "mihai123");

        clientService.adaugaClient(client1);
        clientService.adaugaClient(client2);

        try {
            biletService.cumparaBilet(client1, concert1, concert1.getTipuriBilete().get(0), 2);
            biletService.cumparaBilet(client2, piesa, piesa.getTipuriBilete().get(0), 1);
        } catch (StocBileteInsuficientException e) {
            System.out.println(e.getMessage());
        }
    }
}