package com.global.ecommerce.repository.walletRepo;

import com.global.ecommerce.Entity.walletEntity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface WalletRepo extends JpaRepository<Wallet,Long> {
    public Optional<Wallet> findByUserId(Long userId);

}
