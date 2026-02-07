package com.electrumprep.ledger;

import com.electrumprep.ledger.model.Transaction;
import com.electrumprep.ledger.repository.TransactionRepository;
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

@ExtendWith(MockitoExtension.class)
public class TransactionSwitchTest {

    // 1. Create a "Fake" Database (Mock)
    // We don't want to really talk to Supabase during a quick test.
    @Mock
    private TransactionRepository repository;

    // 2. Inject the Fake DB into our Real Switch Service
    @InjectMocks
    private TransactionSwitch transactionSwitch;

    @Test
    void shouldBlockNegativeMoney() {
        // GIVEN: A thief tries to send -500 Rand
        Transaction badTransaction = new Transaction();
        badTransaction.setAmount(new BigDecimal("-500.00"));
        badTransaction.setSenderAccount("INV-001");

        // WHEN & THEN: We expect the app to scream (throw exception)
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionSwitch.processAndRoute(badTransaction);
        });

        // Verify the error message is correct
        assertEquals("FRAUD ALERT: Cannot send negative money!", exception.getMessage());
    }

    @Test
    void shouldRouteInvestecToInvestec() {
        // GIVEN: A user sends money from an Investec account
        Transaction txn = new Transaction();
        txn.setAmount(new BigDecimal("100.00"));
        txn.setSenderAccount("INV-999"); // Starts with INV

        // (Teach the fake DB to just say "OK" when asked to save)
        when(repository.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);

        // WHEN: We process it
        Transaction result = transactionSwitch.processAndRoute(txn);

        // THEN: The status should verify it went to Investec
        assertTrue(result.getStatus().contains("INVESTEC BANK"));
    }

    @Test
    void shouldRouteAbsaToAbsa() {
        // GIVEN: A user sends money from an Absa account
        Transaction txn = new Transaction();
        txn.setAmount(new BigDecimal("50.00"));
        txn.setSenderAccount("ABS-123"); // Starts with ABS

        // (Teach the fake DB to just say "OK")
        when(repository.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);

        // WHEN: We process it
        Transaction result = transactionSwitch.processAndRoute(txn);

        // THEN: The status should verify it went to Absa
        assertTrue(result.getStatus().contains("ABSA BANK"));
    }
}