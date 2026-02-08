package com.electrumprep.ledger.repository;

// 📦 IMPORTS
// We are grabbing "Spring Data" tools from the shelf.
// "JpaRepository" is a magical tool that writes SQL code for us.
import com.electrumprep.ledger.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// -----------------------------------------------------------------------------------
// 📚 THE REPOSITORY (The Librarian)
// This interface is the ONLY way our Java code talks to the SQL Database.
// Think of it as a "Librarian" who knows exactly where every book is stored.
// -----------------------------------------------------------------------------------

@Repository // 🏷️ STICKER: Tells Spring Boot: "This is a Database Manager."
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // 🪄 MAGIC HAPPENS HERE!
    // Notice how this interface is EMPTY? That is intentional!

    // By extending "JpaRepository<Transaction, Long>", Spring Boot automatically
    // writes ALL the SQL code for us in the background.

    // It gives us methods like:
    // - .save(transaction)      -> INSERT INTO transactions...
    // - .findAll()              -> SELECT * FROM transactions...
    // - .findById(1)            -> SELECT * FROM transactions WHERE id = 1...
    // - .delete(transaction)    -> DELETE FROM transactions...

    // We don't have to write a single line of SQL manually! 🤯
}