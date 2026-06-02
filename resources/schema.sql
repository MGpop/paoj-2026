DROP TABLE IF EXISTS bilet;
DROP TABLE IF EXISTS tip_bilet;
DROP TABLE IF EXISTS eveniment;
DROP TABLE IF EXISTS locatie;
DROP TABLE IF EXISTS client;

CREATE TABLE client (
    id INT PRIMARY KEY,
    nume VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    parola VARCHAR(100) NOT NULL
);

CREATE TABLE locatie (
    id INT PRIMARY KEY,
    nume VARCHAR(100) NOT NULL,
    strada VARCHAR(100) NOT NULL,
    numar VARCHAR(20) NOT NULL,
    oras VARCHAR(100) NOT NULL,
    judet VARCHAR(100) NOT NULL,
    capacitate INT NOT NULL
);

CREATE TABLE eveniment (
    id INT PRIMARY KEY,
    nume VARCHAR(100) NOT NULL,
    data_eveniment DATETIME NOT NULL,
    locatie_id INT NOT NULL,
    tip_eveniment VARCHAR(50) NOT NULL,

    -- Concert
    gen_muzical VARCHAR(100),

    -- PiesaTeatru
    autor VARCHAR(100),
    regizor VARCHAR(100),
    durata_minute INT,

    FOREIGN KEY (locatie_id) REFERENCES locatie(id)
);

CREATE TABLE tip_bilet (
    id INT PRIMARY KEY,
    nume VARCHAR(100) NOT NULL,
    pret DOUBLE NOT NULL,
    stoc_disponibil INT NOT NULL,
    eveniment_id INT NOT NULL,

    FOREIGN KEY (eveniment_id) REFERENCES eveniment(id)
);

CREATE TABLE bilet (
    cod_bilet VARCHAR(150) PRIMARY KEY,
    client_id INT NOT NULL,
    tip_bilet_id INT NOT NULL,
    data_cumparare DATETIME NOT NULL,

    FOREIGN KEY (client_id) REFERENCES client(id),
    FOREIGN KEY (tip_bilet_id) REFERENCES tip_bilet(id)
);