package com.electrumprep.ledger.controller;

// 📦 IMPORTS
// These are like "tools" we grab from the shelf to build our code.
// We need tools to talk to the database (Repository) and tools to handle web requests (Spring Web).
import com.electrumprep.ledger.model.Transaction;
import com.electrumprep.ledger.repository.TransactionRepository;
import com.electrumprep.ledger.service.TransactionSwitch;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// -----------------------------------------------------------------------------------
// 🏢 THE CONTROLLER (The Receptionist)
// This class is the "Front Desk" of your application.
// It is the ONLY place that the outside world (Frontend/Website) can talk to.
// -----------------------------------------------------------------------------------

@RestController // Tells Spring Boot: "This class is ready to receive web requests."
@RequestMapping("/api/transactions") // The specific "Desk Address". Any URL starting with this comes here.

// UPDATED SECURITY PASS: Explicitly allowing your Vercel frontend and local testing.
@CrossOrigin(origins = {
        "https://atomic-ledger.vercel.app",
        "http://localhost:3000",
        "http://localhost:8080"
})
public class TransactionController {

    // 🛠️ DEPENDENCIES (The Staff)
    // The Controller (Receptionist) doesn't actually do the heavy lifting.
    // It delegates work to the "Manager" (Switch) and the "Librarian" (Repository).
    private final TransactionSwitch transactionSwitch;
    private final TransactionRepository repository;

    // 🏗️ CONSTRUCTOR (The Hiring Manager)
    // When the app starts, Spring Boot automatically "hires" the Switch and Repository
    // and plugs them into this Controller so they are ready to use.
    public TransactionController(TransactionSwitch transactionSwitch, TransactionRepository repository) {
        this.transactionSwitch = transactionSwitch;
        this.repository = repository;
    }

    // ===================================================================================
    // 📝 1. POST REQUEST: "Creating New Data"
    // ===================================================================================
    // Think of this like a customer handing a "New Order" to the waiter.
    // The Frontend sends a JSON payment -> We process it.
    @PostMapping
    public Transaction processTransaction(@RequestBody Transaction transaction) {
        // "Waiter" (Controller) hands the order to the "Kitchen Manager" (TransactionSwitch).
        // The Switch determines if it's Investec vs Absa, runs the AI, and saves it.
        // We just return the final result to the user.
        return transactionSwitch.processAndRoute(transaction);
    }

    // ===================================================================================
    // 📖 2. GET REQUEST: "Reading History"
    // ===================================================================================
    // Think of this like asking for a "Bank Statement".
    // The Frontend asks for a list -> We fetch it from the records.
    @GetMapping
    public List<Transaction> getAllTransactions() {
        // "Waiter" asks the "Librarian" (Repository) for all records.
        // Sort.by(Sort.Direction.DESC, "timestamp") means:
        // "Give me the list, but put the NEWEST ones at the top."
        return repository.findAll(Sort.by(Sort.Direction.DESC, "timestamp"));
    }
}