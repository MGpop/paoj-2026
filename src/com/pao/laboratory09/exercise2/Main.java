package com.pao.laboratory09.exercise2;

import com.pao.laboratory09.exercise1.TipTranzactie;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex2.bin";
    private static final int RECORD_SIZE = 32;

    enum Status {
        PENDING,
        PROCESSED,
        REJECTED
    }

    public static void main(String[] args) throws Exception {

        // TODO: Implementează conform Readme.md
        //
        // 1. Citește N din stdin, apoi cele N tranzacții (id suma data tip)

        Locale.setDefault(Locale.US);

        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();

        File outputDirectory = new File("output");
        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
        }

        // 2. Scrie toate înregistrările în OUTPUT_FILE cu DataOutputStream
        //    format binar, RECORD_SIZE = 32 bytes/înregistrare

        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(OUTPUT_FILE))) {
            for (int i = 0; i < n; i++) {
                int id = scanner.nextInt();
                double suma = scanner.nextDouble();
                String data = scanner.next();
                TipTranzactie tip = TipTranzactie.valueOf(scanner.next());

                writeRecord(out, id, suma, data, tip, Status.PENDING);
            }
        }

        // 3. Procesează comenzile din stdin până la EOF cu RandomAccessFile:
        //    - READ idx
        //    - UPDATE idx ST
        //    - PRINT_ALL

        try (RandomAccessFile raf = new RandomAccessFile(OUTPUT_FILE, "rw")) {
            while (scanner.hasNext()) {
                String command = scanner.next();

                if (command.equals("READ")) {
                    int idx = scanner.nextInt();

                    // READ idx → seek(idx * RECORD_SIZE), citește și afișează înregistrarea
                    printRecord(raf, idx);

                } else if (command.equals("UPDATE")) {
                    int idx = scanner.nextInt();
                    Status status = Status.valueOf(scanner.next());

                    // UPDATE idx ST → seek(idx * RECORD_SIZE + 23), scrie noul status
                    raf.seek((long) idx * RECORD_SIZE + 23);
                    raf.write(status.ordinal());

                    System.out.println("Updated [" + idx + "]: " + status);

                } else if (command.equals("PRINT_ALL")) {

                    // PRINT_ALL → citește și afișează toate înregistrările
                    for (int i = 0; i < n; i++) {
                        printRecord(raf, i);
                    }
                }
            }
        }

        // Format linie output:
        //   [idx] id=<id> data=<data> tip=<CREDIT|DEBIT> suma=<suma:.2f> RON status=<STATUS>
    }

    private static void writeRecord(DataOutputStream out, int id, double suma, String data, TipTranzactie tip, Status status) throws Exception {
        byte[] record = new byte[RECORD_SIZE];
        ByteBuffer buffer = ByteBuffer.wrap(record).order(ByteOrder.LITTLE_ENDIAN);

        // bytes 0-3: id int, little-endian
        buffer.putInt(id);

        // bytes 4-11: suma double, little-endian
        buffer.putDouble(suma);

        // bytes 12-21: data, 10 caractere ASCII, paddată cu spații la dreapta
        byte[] dataBytes = data.getBytes(StandardCharsets.US_ASCII);
        for (int i = 0; i < 10; i++) {
            if (i < dataBytes.length) {
                record[12 + i] = dataBytes[i];
            } else {
                record[12 + i] = ' ';
            }
        }

        // byte 22: tip, 0=CREDIT, 1=DEBIT
        record[22] = (byte) tip.ordinal();

        // byte 23: status, 0=PENDING, 1=PROCESSED, 2=REJECTED
        record[23] = (byte) status.ordinal();

        // bytes 24-31: padding cu zerouri
        out.write(record);
    }

    private static void printRecord(RandomAccessFile raf, int idx) throws Exception {
        raf.seek((long) idx * RECORD_SIZE);

        byte[] record = new byte[RECORD_SIZE];
        raf.readFully(record);

        ByteBuffer buffer = ByteBuffer.wrap(record).order(ByteOrder.LITTLE_ENDIAN);
        int id = buffer.getInt();
        double suma = buffer.getDouble();

        String data = new String(record, 12, 10, StandardCharsets.US_ASCII).trim();
        TipTranzactie tip = TipTranzactie.values()[record[22]];
        Status status = Status.values()[record[23]];

        System.out.printf("[%d] id=%d data=%s tip=%s suma=%.2f RON status=%s%n", idx, id, data, tip, suma, status);
    }
}
