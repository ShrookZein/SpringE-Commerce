package com.global.ecommerce.repository.walletRepo;

import com.global.ecommerce.Entity.walletEntity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction,Long> {
    public List<Transaction>findByWalletId(Long walletId);
//    @Query("SELECT t FROM Transaction t WHERE t.wallet.id = :walletId")
//    List<Transaction> findTransactionsByWalletId(@Param("walletId") Long walletId);
}
