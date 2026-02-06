package com.electrumprep.ledger.service;

import com.electrumprep.ledger.model.Transaction;
import com.electrumprep.ledger.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class TransactionSwitch {

    private final TransactionRepository repository;

    public TransactionSwitch(TransactionRepository repository) {
        this.repository = repository;
    }

    public Transaction processAndRoute(Transaction transaction) {
        // 1. THE BOUNCER (Validation)
        if (transaction.getAmount().doubleValue() < 0) {
            throw new IllegalArgumentException("FRAUD ALERT: Cannot send negative money!");
        }

        // 2. THE ROUTER (The "Switch" Logic)
        String destination = determineBank(transaction.getSenderAccount());

        // Simulating the network call to the other bank
        System.out.println(">>> SWITCHING TRANSACTION TO: " + destination);

        // 3. FINAL PROCESSING
        transaction.setStatus("SUCCESS - Sent to " + destination);
        transaction.setTimestamp(LocalDateTime.now());

        return repository.save(transaction);
    }

    private String determineBank(String account) {
        if (account.startsWith("INV")) return "INVESTEC BANK";
        else if (account.startsWith("FNB")) return "FNB BANK";
        else if (account.startsWith("ABS")) return "ABSA BANK";
        else return "STANDARD BANK (Default)";
    }
}