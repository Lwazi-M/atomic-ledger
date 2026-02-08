package com.electrumprep.ledger.controller;

import com.electrumprep.ledger.model.Transaction;
import com.electrumprep.ledger.repository.TransactionRepository;
import com.electrumprep.ledger.service.TransactionSwitch;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*") // Allows the frontend to talk to the backend easily
public class TransactionController {

    private final TransactionSwitch transactionSwitch;
    private final TransactionRepository repository; // <--- New Dependency

    // Updated Constructor to include the Repository
    public TransactionController(TransactionSwitch transactionSwitch, TransactionRepository repository) {
        this.transactionSwitch = transactionSwitch;
        this.repository = repository;
    }

    // 1. POST: Create a new payment (You already had this)
    @PostMapping
    public Transaction processTransaction(@RequestBody Transaction transaction) {
        return transactionSwitch.processAndRoute(transaction);
    }

    // 2. GET: Read payment history (This is NEW!)
    @GetMapping
    public List<Transaction> getAllTransactions() {
        // Fetch all transactions and sort them by timestamp (newest first)
        return repository.findAll(Sort.by(Sort.Direction.DESC, "timestamp"));
    }
}