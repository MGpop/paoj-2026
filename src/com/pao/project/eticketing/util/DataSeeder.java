package com.pao.project.eticketing.util;

import com.pao.project.eticketing.model.*;
import com.pao.project.eticketing.repository.*;

import java.time.LocalDateTime;

public class DataSeeder {
    private DataSeeder() {
    }

    public static void seed() {
        ClientRepository clientRepository = new ClientRepository();
        LocatieRepository locatieRepository = new LocatieRepository();
        EvenimentRepository evenimentRepository = new EvenimentRepository();
        TipBiletRepository tipBiletRepository = new TipBiletRepository();
        BiletRepository biletRepository = new BiletRepository();

        Client client1 = new Client(1, "Ana Popescu", "ana@test.ro", "ana123");
        Client client2 = new Client(2, "Mihai Ionescu", "mihai@test.ro", "mihai123");
        Client client3 = new Client(3, "Elena Marin", "elena@test.ro", "elena123");
        Client client4 = new Client(4, "Andrei Pavel", "andrei@test.ro", "andrei123");
        Client client5 = new Client(5, "Ioana Dumitru", "ioana@test.ro", "ioana123");

        clientRepository.save(client1);
        clientRepository.save(client2);
        clientRepository.save(client3);
        clientRepository.save(client4);
        clientRepository.save(client5);

        Locatie locatie1 = new Locatie(
                1,
                "Arena Națională",
                new Adresa("Bd. Basarabia", "37-39", "București", "București"),
                55000
        );

        Locatie locatie2 = new Locatie(
                2,
                "Ateneul Român",
                new Adresa("Str. Franklin", "1-3", "București", "București"),
                800
        );

        Locatie locatie3 = new Locatie(
                3,
                "Teatrul Național Timișoara",
                new Adresa("Piața Victoriei", "5", "Timișoara", "Timiș"),
                700
        );

        Locatie locatie4 = new Locatie(
                4,
                "Sala Palatului",
                new Adresa("Str. Ion Câmpineanu", "28", "București", "București"),
                4000
        );

        Locatie locatie5 = new Locatie(
                5,
                "Casa de Cultură Cluj",
                new Adresa("Str. Eroilor", "10", "Cluj-Napoca", "Cluj"),
                1200
        );

        locatieRepository.save(locatie1);
        locatieRepository.save(locatie2);
        locatieRepository.save(locatie3);
        locatieRepository.save(locatie4);
        locatieRepository.save(locatie5);

        Eveniment eveniment1 = new Concert(
                1,
                "Concert The Echoes",
                LocalDateTime.of(2026, 6, 20, 20, 0),
                locatie1,
                "Rock"
        );

        Eveniment eveniment2 = new Concert(
                2,
                "Seara Simfonică",
                LocalDateTime.of(2026, 6, 25, 19, 0),
                locatie2,
                "Clasic"
        );

        Eveniment eveniment3 = new PiesaTeatru(
                3,
                "O scrisoare pierdută",
                LocalDateTime.of(2026, 7, 5, 18, 30),
                locatie3,
                "I. L. Caragiale",
                "Maria Ionescu",
                120
        );

        Eveniment eveniment4 = new Concert(
                4,
                "Pop Night",
                LocalDateTime.of(2026, 7, 10, 21, 0),
                locatie4,
                "Pop"
        );

        Eveniment eveniment5 = new PiesaTeatru(
                5,
                "Hamlet",
                LocalDateTime.of(2026, 7, 15, 19, 30),
                locatie5,
                "William Shakespeare",
                "Radu Enache",
                150
        );

        evenimentRepository.save(eveniment1);
        evenimentRepository.save(eveniment2);
        evenimentRepository.save(eveniment3);
        evenimentRepository.save(eveniment4);
        evenimentRepository.save(eveniment5);

        TipBilet tip1 = new TipBilet(1, "Standard", 250, 100);
        TipBilet tip2 = new TipBilet(2, "VIP", 600, 20);
        TipBilet tip3 = new TipBilet(3, "Balcon", 120, 50);
        TipBilet tip4 = new TipBilet(4, "Lojă", 300, 10);
        TipBilet tip5 = new TipBilet(5, "Standard", 80, 60);
        TipBilet tip6 = new TipBilet(6, "Premium", 150, 15);
        TipBilet tip7 = new TipBilet(7, "General Access", 180, 200);
        TipBilet tip8 = new TipBilet(8, "Golden Circle", 450, 30);
        TipBilet tip9 = new TipBilet(9, "Standard", 100, 70);
        TipBilet tip10 = new TipBilet(10, "Premium", 220, 25);

        tipBiletRepository.save(tip1, eveniment1.getId());
        tipBiletRepository.save(tip2, eveniment1.getId());
        tipBiletRepository.save(tip3, eveniment2.getId());
        tipBiletRepository.save(tip4, eveniment2.getId());
        tipBiletRepository.save(tip5, eveniment3.getId());
        tipBiletRepository.save(tip6, eveniment3.getId());
        tipBiletRepository.save(tip7, eveniment4.getId());
        tipBiletRepository.save(tip8, eveniment4.getId());
        tipBiletRepository.save(tip9, eveniment5.getId());
        tipBiletRepository.save(tip10, eveniment5.getId());

        biletRepository.saveCuTranzactie(new Bilet("B-1", client1, eveniment1, tip1));
        biletRepository.saveCuTranzactie(new Bilet("B-2", client1, eveniment2, tip3));
        biletRepository.saveCuTranzactie(new Bilet("B-3", client2, eveniment1, tip2));
        biletRepository.saveCuTranzactie(new Bilet("B-4", client2, eveniment3, tip5));
        biletRepository.saveCuTranzactie(new Bilet("B-5", client3, eveniment4, tip7));
        biletRepository.saveCuTranzactie(new Bilet("B-6", client3, eveniment5, tip9));
        biletRepository.saveCuTranzactie(new Bilet("B-7", client4, eveniment2, tip4));
        biletRepository.saveCuTranzactie(new Bilet("B-8", client4, eveniment4, tip8));
        biletRepository.saveCuTranzactie(new Bilet("B-9", client5, eveniment3, tip6));
        biletRepository.saveCuTranzactie(new Bilet("B-10", client5, eveniment5, tip10));
    }
}