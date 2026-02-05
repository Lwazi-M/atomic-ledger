package com.electrumprep.ledger.controller;

import com.electrumprep.ledger.model.Transaction;
import com.electrumprep.ledger.repository.TransactionRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionRepository repository;

    public TransactionController(TransactionRepository repository) {
        this.repository = repository;
    }

    // GET /api/transactions -> Show me the history
    @GetMapping
    public List<Transaction> getAllTransactions() {
        return repository.findAll();
    }

    // POST /api/transactions -> Process a new payment
    @PostMapping
    public Transaction processTransaction(@RequestBody Transaction transaction) {

        // -----------------------------------------------------------
        // 1. THE MANUAL BOUNCER (Validation Logic)
        // -----------------------------------------------------------

        // Rule 1: No negative money allowed
        // We convert to double just for the check to keep it simple
        if (transaction.getAmount().doubleValue() < 0) {
            throw new IllegalArgumentException("FRAUD ALERT: Cannot send negative money!");
        }

        // Rule 2: Only ZAR is allowed (Optional, but good practice)
        if (!"ZAR".equals(transaction.getCurrency())) {
            // We can uncomment this later if we want to enforce ZAR only
            // throw new IllegalArgumentException("Only ZAR is supported.");
        }

        // -----------------------------------------------------------
        // 2. PROCESSING
        // -----------------------------------------------------------

        // Stamp the time
        transaction.setTimestamp(LocalDateTime.now());

        // Set the status
        transaction.setStatus("SUCCESS");

        // Save to database
        return repository.save(transaction);
    }
}