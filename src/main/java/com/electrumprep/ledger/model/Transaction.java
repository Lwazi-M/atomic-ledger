package com.electrumprep.ledger.model;

// 📦 IMPORTS
// We are grabbing tools from the Java library shelf.
// "persistence" tools help us talk to the database (PostgreSQL).
// "math" helps us handle money (BigDecimal) without rounding errors.
// "time" helps us record exactly WHEN something happened.
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

// -----------------------------------------------------------------------------------
// 📄 THE MODEL (The Blueprint)
// Think of this class as a "Blank Receipt".
// It defines exactly what information we need to save for every single payment.
// -----------------------------------------------------------------------------------

@Entity // 🏷️ STICKER: Tells Java: "This is a Database Table Row."
@Data   // 🪄 MAGIC: Lombok automatically writes the "Getters" and "Setters" for us.
// (So we don't have to write public String getReference() { return reference; } ...)
@Table(name = "transactions") // 🗄️ DRAWER: Tells the database: "Store these in the 'transactions' folder."
public class Transaction {

    // 🔑 THE UNIQUE ID
    // Every receipt needs a unique number so we don't mix them up.
    // @Id = This is the Primary Key.
    // @GeneratedValue = "Database, please invent a new number (1, 2, 3...) for me automatically."
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 📝 THE DATA FIELDS (The Lines on the Receipt)

    // "Reference": What did they buy? (e.g., "Uber Ride", "Netflix")
    private String reference;

    // "Amount": How much money? We use BigDecimal for money because it's precise.
    // (Double/Float can make math mistakes like 1.00 - 0.90 = 0.0999999)
    private BigDecimal amount;

    // "Sender": Who is paying? (e.g., "INV-2026")
    private String senderAccount;

    // "Receiver": Who is getting paid? (e.g., "Absa Bank")
    private String receiverAccount;

    // "Currency": ZAR, USD, EUR...
    private String currency;

    // "Status": Did it work? (SUCCESS, FAILED, PENDING)
    private String status;

    // "Timestamp": Exactly when did this happen? (2026-02-08 14:30:00)
    private LocalDateTime timestamp;

    // 🤖 AI FIELD (The Smart Label)
    // This is the new box we added for Gemini.
    // The AI will fill this in with "Food", "Transport", etc.
    // In the database, this creates a new column called 'category'.
    private String category;
}