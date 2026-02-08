package com.electrumprep.ledger;

// 📦 IMPORTS
// We are grabbing the "Mockito" tools.
// Mockito is a special library that lets us create "Fake Objects" (Stunt Doubles).
import com.electrumprep.ledger.model.Transaction;
import com.electrumprep.ledger.repository.TransactionRepository;
import com.electrumprep.ledger.service.CategorizationService; // <--- Needed for the new AI stuff
import com.electrumprep.ledger.service.TransactionSwitch;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

// -----------------------------------------------------------------------------------
// 🧪 THE FLIGHT SIMULATOR (Unit Test)
// This file tests the "Brain" (Switch) in isolation.
// We DO NOT connect to the real database or the real Google AI here.
// Why? Because real databases are slow, and Google costs money per click!
// -----------------------------------------------------------------------------------

@ExtendWith(MockitoExtension.class) // 🔌 PLUG: Turns on the "Mockito" Simulator.
public class TransactionSwitchTest {

    // 🎭 THE STUNT DOUBLES (Mocks)
    // We create "Fake" versions of the complicated stuff.
    // They look like the real thing, but they don't actually do anything unless we tell them to.

    @Mock // Fake Database
    private TransactionRepository repository;

    @Mock // Fake AI Service (Added this because your Switch needs it now!)
    private CategorizationService aiService;

    // 🧠 THE REAL SUBJECT
    // This is the actual code we want to test.
    // @InjectMocks tells Mockito: "Create the Switch, and plug in the Fake DB and Fake AI automatically."
    @InjectMocks
    private TransactionSwitch transactionSwitch;

    // ===================================================================================
    // 🧪 TEST 1: The Fraud Check
    // Scenario: Someone tries to steal money by sending a negative amount.
    // ===================================================================================
    @Test
    void shouldBlockNegativeMoney() {
        // 1. GIVEN: A bad transaction (The Setup)
        Transaction badTransaction = new Transaction();
        badTransaction.setAmount(new BigDecimal("-500.00")); // Negative money!
        badTransaction.setSenderAccount("INV-001");

        // 2. WHEN & THEN: We try to process it (The Action & Check)
        // "assertThrows" means: "I EXPECT this code to crash with an error."
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionSwitch.processAndRoute(badTransaction);
        });

        // 3. VERIFY: Did it give the right error message?
        assertEquals("FRAUD ALERT: Cannot send negative money!", exception.getMessage());
    }

    // ===================================================================================
    // 🧪 TEST 2: Routing to Investec
    // Scenario: An account starting with "INV" sends money.
    // ===================================================================================
    @Test
    void shouldRouteInvestecToInvestec() {
        // 1. GIVEN: An Investec client
        Transaction txn = new Transaction();
        txn.setAmount(new BigDecimal("100.00"));
        txn.setSenderAccount("INV-999"); // Starts with INV

        // 📝 THE SCRIPT (Stubbing)
        // Since the Database is fake, it doesn't know how to save.
        // We teach it: "When I ask you to save ANY transaction, just return it back to me."
        when(repository.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);

        // WHEN: We run the switch logic
        Transaction result = transactionSwitch.processAndRoute(txn);

        // THEN: We check the stamp on the envelope
        // It should say "INVESTEC BANK".
        assertTrue(result.getStatus().contains("INVESTEC BANK"));
    }

    // ===================================================================================
    // 🧪 TEST 3: Routing to Absa
    // Scenario: An account starting with "ABS" sends money.
    // ===================================================================================
    @Test
    void shouldRouteAbsaToAbsa() {
        // 1. GIVEN: An Absa client
        Transaction txn = new Transaction();
        txn.setAmount(new BigDecimal("50.00"));
        txn.setSenderAccount("ABS-123"); // Starts with ABS

        // 📝 THE SCRIPT
        // Teach the fake DB to say "Yes".
        when(repository.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);

        // WHEN: We process it
        Transaction result = transactionSwitch.processAndRoute(txn);

        // THEN: We check the status
        assertTrue(result.getStatus().contains("ABSA BANK"));
    }
}