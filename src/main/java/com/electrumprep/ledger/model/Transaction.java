package com.electrumprep.ledger.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * =================================================================================
 * THE TRANSACTION MODEL
 * =================================================================================
 * This class represents a single movement of money.
 * It is mapped to a database table called 'transactions'.
 * <p>
 * ANNOTATIONS EXPLAINED:
 * 1. @Entity: Tells Spring "Save this class to the Database".
 * 2. @Data: (Lombok) Automatically writes getters, setters, and toString() for us.
 * 3. @NoArgsConstructor: Generates a blank constructor (needed by the database).
 * 4. @AllArgsConstructor: Generates a constructor with all fields (easier for us to use).
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions") // This creates a table named 'transactions' in SQL
public class Transaction {

    @Id // This is the Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment (1, 2, 3...)
    private Long id;

    // We use String for IDs because they might contain dashes (e.g., "TXN-99882")
    private String reference;

    // CRITICAL: Always use BigDecimal for money. Never use Double.
    private BigDecimal amount;

    // Currency code (e.g., "ZAR", "USD")
    private String currency;

    // Who sent the money?
    private String senderAccount;

    // Who received the money?
    private String receiverAccount;

    // What is the status? (PENDING, SUCCESS, FAILED)
    private String status;

    // When did this happen?
    private LocalDateTime timestamp;
}