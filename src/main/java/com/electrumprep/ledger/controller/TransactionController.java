package com.electrumprep.ledger.controller;

import com.electrumprep.ledger.model.Transaction;
import com.electrumprep.ledger.service.TransactionSwitch;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {  // <--- NAME MATCHES FILE NOW

    private final TransactionSwitch transactionSwitch;

    public TransactionController(TransactionSwitch transactionSwitch) {
        this.transactionSwitch = transactionSwitch;
    }

    @PostMapping
    public Transaction processTransaction(@RequestBody Transaction transaction) {
        return transactionSwitch.processAndRoute(transaction);
    }
}