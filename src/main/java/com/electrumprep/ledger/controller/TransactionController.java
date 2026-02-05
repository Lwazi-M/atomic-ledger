package com.electrumprep.ledger.controller;

import com.electrumprep.ledger.model.Transaction;
import com.electrumprep.ledger.repository.TransactionRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transactions") // The address of our bank
public class TransactionController {

    private final TransactionRepository repository;

    // Dependency Injection: Spring gives us the repository automatically
    public TransactionController(TransactionRepository repository) {
        this.repository = repository;
    }

    // GET /api/transactions -> Show me all payments
    @GetMapping
    public List<Transaction> getAllTransactions() {
        return repository.findAll();
    }

    // POST /api/transactions -> Process a new payment
    @PostMapping
    public Transaction processTransaction(@RequestBody Transaction transaction) {
        // 1. Add the timestamp (Audit trail)
        transaction.setTimestamp(LocalDateTime.now());

        // 2. Set default status
        transaction.setStatus("SUCCESS");

        // 3. Save to database
        return repository.save(transaction);
    }
}