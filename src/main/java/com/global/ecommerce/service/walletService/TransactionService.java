package com.global.ecommerce.service.walletService;

import com.global.ecommerce.Entity.walletEntity.Transaction;
import com.global.ecommerce.Entity.walletEntity.TransactionStatus;
import com.global.ecommerce.Entity.walletEntity.TransactionType;
import com.global.ecommerce.Entity.walletEntity.Wallet;
import com.global.ecommerce.repository.walletRepo.TransactionRepo;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Log4j2
public class TransactionService {

    @Autowired
    private TransactionRepo transactionRepo;

    public Transaction insert(Wallet wallet, BigDecimal amount, TransactionType transactionType, TransactionStatus transactionStatus) {
        log.info("Inserting a new transaction: Wallet ID={}, Amount={}, TransactionType={}, TransactionStatus={}",
                wallet.getId(), amount, transactionType, transactionStatus);
        Transaction transaction = new Transaction(null, amount, wallet, transactionType, transactionStatus, new Timestamp(System.currentTimeMillis()));
        Transaction savedTransaction = transactionRepo.save(transaction);
        log.info("Transaction inserted successfully with ID={}", savedTransaction.getId());
        return savedTransaction;
    }

    public List<Transaction> findAllTransactionsByWalletId(Long walletId) {
        log.info("Fetching all transactions for Wallet ID={}", walletId);
        List<Transaction> transactions = transactionRepo.findByWalletId(walletId);
        if (transactions.isEmpty()) {
            log.warn("No transactions found for Wallet ID={}", walletId);
        } else {
            log.info("Found {} transactions for Wallet ID={}", transactions.size(), walletId);
        }
        return transactions;
    }
}






//package com.global.ecommerce.service.walletService;
//
//import com.global.ecommerce.Entity.walletEntity.Transaction;
//import com.global.ecommerce.Entity.walletEntity.TransactionStatus;
//import com.global.ecommerce.Entity.walletEntity.TransactionType;
//import com.global.ecommerce.Entity.walletEntity.Wallet;
//import com.global.ecommerce.repository.walletRepo.TransactionRepo;
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.sql.Timestamp;
//import java.util.List;
//
//@Service
//@AllArgsConstructor
//@NoArgsConstructor
//public class TransactionService {
//    @Autowired
//    private TransactionRepo transactionRepo;
//    public Transaction insert(Wallet wallet, BigDecimal amount, TransactionType transactionType, TransactionStatus transactionStatus){
//        Transaction transaction=new Transaction(null,amount,wallet,transactionType,transactionStatus,new Timestamp(System.currentTimeMillis()));
//        return transactionRepo.save(transaction);
//    }
//    public List<Transaction>findAllTransactionsByWalletId(Long walletId){
////        return transactionRepo.findTransactionsByWalletId(walletId);
//        return transactionRepo.findByWalletId(walletId);
//
//    }
//}



