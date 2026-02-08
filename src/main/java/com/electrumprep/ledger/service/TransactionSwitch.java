package com.electrumprep.ledger.service;

// 📦 IMPORTS
// We need the "Transaction" blueprint to understand what a payment looks like.
// We need the "Repository" to save the final result to the database.
// We need the "Service" sticker to tell Spring Boot this is a worker class.
// We need "BigDecimal" for precise money math (no penny errors!).
import com.electrumprep.ledger.model.Transaction;
import com.electrumprep.ledger.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// -----------------------------------------------------------------------------------
// 🚦 THE TRANSACTION SWITCH (The Traffic Cop)
// This is the "Brain" of the entire operation.
// It decides:
// 1. Is this money fake? (Validation)
// 2. Where should it go? (Routing)
// 3. What is this for? (AI Categorization)
// -----------------------------------------------------------------------------------

@Service // 🏷️ STICKER: Tells Spring Boot: "This is the Manager. It handles the logic."
public class TransactionSwitch {

    // 🛠️ THE STAFF
    // The Manager (Switch) can't do everything alone. It hires help:
    // 1. The "Librarian" (Repository) to store the records.
    // 2. The "Translator" (AI Service) to understand the messy text.
    private final TransactionRepository repository;
    private final CategorizationService aiService;

    // 🏗️ CONSTRUCTOR (Hiring Process)
    // Spring Boot automatically gives us these tools when the app starts.
    public TransactionSwitch(TransactionRepository repository, CategorizationService aiService) {
        this.repository = repository;
        this.aiService = aiService;
    }

    // ===================================================================================
    // ⚙️ THE MAIN LOGIC METHOD
    // This function runs every single time someone clicks "Process Payment".
    // ===================================================================================
    public Transaction processAndRoute(Transaction txn) {

        // 🛑 1. THE BOUNCER (Validation)
        // Before we do anything, we check if the request is legal.
        // Rule: You cannot send negative money (e.g., -R500). That's stealing!
        if (txn.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            // If they try, we throw them out immediately with an error.
            throw new IllegalArgumentException("FRAUD ALERT: Cannot send negative money!");
        }

        // 🔧 2. AUTO-FIXING DATA
        // If the user forgot to say which currency, we assume it's South African Rands.
        // This prevents the "NullPointerException" crash we saw earlier.
        txn.setCurrency("ZAR");

        // 🔀 3. THE TRAFFIC COP (Routing Logic)
        // We look at the "License Plate" (Account Number) to decide which road to take.
        String targetBank;
        String receiverPoolAccount;

        // "StartsWith" checks the first 3 letters of the account.
        if (txn.getSenderAccount().startsWith("INV")) {
            // Case A: It's an Investec Client -> Send to Investec Pool
            targetBank = "INVESTEC BANK";
            receiverPoolAccount = "INV-POOL-888";
        } else if (txn.getSenderAccount().startsWith("ABS")) {
            // Case B: It's an Absa Client -> Send to Absa Merchant
            targetBank = "ABSA BANK";
            receiverPoolAccount = "ABS-MERCHANT-001";
        } else {
            // Case C: Everyone else -> Send to Standard Bank Clearing
            targetBank = "STANDARD BANK";
            receiverPoolAccount = "SB-CLEARING-999";
        }

        // We stamp the final destination onto the transaction ticket.
        txn.setReceiverAccount(receiverPoolAccount);

        // 🤖 4. THE SMART CONSULTANT (AI Enrichment)
        // We pause for a split second to call our AI Friend (Google Gemini).
        // We ask: "Hey, what category is 'Uber * 8721'?"
        // The AI replies: "Transport".
        String predictedCategory = aiService.categorize(txn.getReference(), txn.getAmount().toString());

        // We write that answer onto the ticket.
        txn.setCategory(predictedCategory);

        // ✅ 5. STAMP OF APPROVAL (Finalize)
        // We declare the transaction successful and record the destination.
        txn.setStatus("SUCCESS - Sent to " + targetBank);

        // We record the exact millisecond this happened.
        txn.setTimestamp(LocalDateTime.now());

        // 📂 6. FILE IT AWAY (Save to Database)
        // Finally, we hand the completed ticket to the Librarian to put in the permanent file.
        // This sends the SQL "INSERT" command to the database.
        return repository.save(txn);
    }
}