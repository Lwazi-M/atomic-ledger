package com.electrumprep.ledger.repository;

import com.electrumprep.ledger.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // This is empty because Spring Boot writes the SQL for us automatically!
}