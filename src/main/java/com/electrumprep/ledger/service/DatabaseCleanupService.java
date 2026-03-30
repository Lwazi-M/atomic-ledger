package com.electrumprep.ledger.service;

import com.electrumprep.ledger.repository.TransactionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DatabaseCleanupService {

    private final TransactionRepository transactionRepository;

    public DatabaseCleanupService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    // Runs exactly at midnight (00:00:00) every day
    @Scheduled(cron = "0 0 0 * * ?")
    public void wipeTransactionHistory() {
        transactionRepository.deleteAll();
        System.out.println("🧹 SCHEDULED TASK: 24-hour cycle complete. Transaction database wiped clean!");
    }
}