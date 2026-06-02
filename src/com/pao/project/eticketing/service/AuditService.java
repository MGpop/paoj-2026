package com.pao.project.eticketing.service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class AuditService {
    private static AuditService instance;

    private static final String AUDIT_FILE = "audit.csv";

    private AuditService() {
    }

    public static AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }

        return instance;
    }

    public synchronized void logAction(String actionName) {
        try (FileWriter writer = new FileWriter(AUDIT_FILE, true)) {
            writer.write(actionName + "," + LocalDateTime.now() + "\n");
        } catch (IOException e) {
            throw new RuntimeException("Eroare la scrierea în audit.csv.", e);
        }
    }
}