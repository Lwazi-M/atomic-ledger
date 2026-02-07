package com.electrumprep.ledger.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reference;
    private BigDecimal amount;
    private String senderAccount;
    private String receiverAccount;
    private String currency;
    private String status;
    private LocalDateTime timestamp;

    // --- NEW FIELD FOR AI ---
    // This creates the 'category' column in your database
    private String category;
}