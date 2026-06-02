package com.pao.project.eticketing.util;

import com.pao.project.eticketing.repository.*;

public final class ApplicationContext {

    public static final ClientRepository CLIENT_REPOSITORY = new ClientRepository();

    public static final LocatieRepository LOCATIE_REPOSITORY = new LocatieRepository();

    public static final EvenimentRepository EVENIMENT_REPOSITORY = new EvenimentRepository();

    public static final TipBiletRepository TIP_BILET_REPOSITORY = new TipBiletRepository();

    public static final BiletRepository BILET_REPOSITORY = new BiletRepository();

    public static final RaportRepository RAPORT_REPOSITORY = new RaportRepository();

    private ApplicationContext() {
    }
}