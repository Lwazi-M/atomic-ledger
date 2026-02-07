package com.electrumprep.ledger.service;

import com.electrumprep.ledger.model.Transaction;
import com.electrumprep.ledger.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransactionSwitch {

    private final TransactionRepository repository;
    private final CategorizationService aiService;

    public TransactionSwitch(TransactionRepository repository, CategorizationService aiService) {
        this.repository = repository;
        this.aiService = aiService;
    }

    public Transaction processAndRoute(Transaction txn) {
        // 1. Validation Rule
        if (txn.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("FRAUD ALERT: Cannot send negative money!");
        }

        // 2. Default Currency (Fixing the null issue)
        txn.setCurrency("ZAR");

        // 3. Routing Logic
        String targetBank;
        String receiverPoolAccount;

        if (txn.getSenderAccount().startsWith("INV")) {
            targetBank = "INVESTEC BANK";
            receiverPoolAccount = "INV-POOL-888";
        } else if (txn.getSenderAccount().startsWith("ABS")) {
            targetBank = "ABSA BANK";
            receiverPoolAccount = "ABS-MERCHANT-001";
        } else {
            targetBank = "STANDARD BANK";
            receiverPoolAccount = "SB-CLEARING-999";
        }

        // Fix: Set the Receiver Account so it's not null
        txn.setReceiverAccount(receiverPoolAccount);

        // 4. AI Enrichment
        // We ask Gemini to guess the category
        String predictedCategory = aiService.categorize(txn.getReference(), txn.getAmount().toString());
        txn.setCategory(predictedCategory);

        // 5. Finalize
        txn.setStatus("SUCCESS - Sent to " + targetBank);
        txn.setTimestamp(LocalDateTime.now());

        return repository.save(txn);
    }
}