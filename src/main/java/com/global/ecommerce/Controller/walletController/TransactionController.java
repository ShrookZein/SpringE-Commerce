package com.global.ecommerce.Controller.walletController;

import com.global.ecommerce.Entity.walletEntity.Transaction;
import com.global.ecommerce.service.walletService.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@AllArgsConstructor
public class TransactionController {
    @Autowired
    TransactionService transactionService;
    @GetMapping("/wallet/{walletId}")
    public ResponseEntity<List<Transaction>>findWalletTransactions(@PathVariable Long walletId){

        List<Transaction>transactions=transactionService.findAllTransactionsByWalletId(walletId);
//        System.out.println(transactions.get(1).getTransactionType().toString());
        return ResponseEntity.ok(transactions);
    }
}
