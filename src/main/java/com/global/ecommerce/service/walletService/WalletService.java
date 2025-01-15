package com.global.ecommerce.service.walletService;

import com.global.ecommerce.Entity.User;
import com.global.ecommerce.Entity.walletEntity.TransactionStatus;
import com.global.ecommerce.Entity.walletEntity.TransactionType;
import com.global.ecommerce.Entity.walletEntity.Wallet;
import com.global.ecommerce.repository.walletRepo.WalletRepo;
import com.global.ecommerce.service.authService.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Log4j2
public class WalletService {
    @Autowired
    private WalletRepo walletRepo;
    @Autowired
    private UserService userService;

    @Transactional
    public Wallet createWallet(User user) {
        log.info("Creating wallet for user: {}", user);
        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setBalance(BigDecimal.valueOf(0.0));
        Wallet savedWallet = walletRepo.save(wallet);
        log.info("Wallet created: {}", savedWallet);
        return savedWallet;
    }

    public Wallet insertWallet() {
        log.info("Inserting a new wallet with zero balance.");
        Wallet wallet = new Wallet();
        wallet.setBalance(BigDecimal.valueOf(0.0));
        Wallet savedWallet = walletRepo.save(wallet);
        log.info("Wallet inserted: {}", savedWallet);
        return savedWallet;
    }

    public Wallet updateWallet(Wallet wallet) {
        log.info("Updating wallet: {}", wallet);
        Wallet updatedWallet = walletRepo.save(wallet);
        log.info("Wallet updated: {}", updatedWallet);
        return updatedWallet;
    }

    public Wallet getWalletById(Long walletId) {
        log.info("Fetching wallet by ID: {}", walletId);
        Wallet wallet = walletRepo.findById(walletId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet not found"));
        log.info("Wallet fetched: {}", wallet);
        return wallet;
    }

    public Wallet getWalletByUserId(Long userId) {
        log.info("Fetching wallet by user ID: {}", userId);
        Wallet wallet = walletRepo.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet not found"));
        log.info("Wallet fetched: {}", wallet);
        return wallet;
    }

    public BigDecimal getWalletBalance(Long userId) {
        log.info("Fetching wallet balance for user ID: {}", userId);
        Wallet wallet = getWalletByUserId(userId);
        BigDecimal balance = wallet.getBalance();
        log.info("Wallet balance for user ID {}: {}", userId, balance);
        return balance;
    }

    public Wallet deposit(Long userId, BigDecimal amount) {
        log.info("Depositing amount {} to wallet of user ID: {}", amount, userId);
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            log.error("Deposit amount must be greater than zero. Provided amount: {}", amount);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Deposit amount must be greater than zero");
        }
        Wallet wallet = getWalletByUserId(userId);
        wallet.setBalance(wallet.getBalance().add(amount));
        Wallet updatedWallet = walletRepo.save(wallet);
        log.info("Deposit successful. Updated wallet: {}", updatedWallet);
        return updatedWallet;
    }

    public Wallet withdraw(Long userId, BigDecimal amount) {
        log.info("Withdrawing amount {} from wallet of user ID: {}", amount, userId);
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            log.error("Withdrawal amount must be greater than zero. Provided amount: {}", amount);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Withdraw amount must be greater than zero");
        }
        Wallet wallet = getWalletByUserId(userId);
        if (amount.compareTo(wallet.getBalance()) > 0) {
            log.error("Insufficient balance in wallet for user ID: {}. Attempted withdrawal: {}, Available balance: {}", userId, amount, wallet.getBalance());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient balance in the wallet");
        }
        wallet.setBalance(wallet.getBalance().subtract(amount));
        Wallet updatedWallet = walletRepo.save(wallet);
        log.info("Withdrawal successful. Updated wallet: {}", updatedWallet);
        return updatedWallet;
    }
}






//package com.global.ecommerce.service.walletService;
//
//import com.global.ecommerce.Entity.User;
//import com.global.ecommerce.Entity.walletEntity.Wallet;
//import com.global.ecommerce.repository.walletRepo.WalletRepo;
//import com.global.ecommerce.service.authService.UserService;
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.math.BigDecimal;
//
//@Service
//@AllArgsConstructor
//@NoArgsConstructor
//public class WalletService {
//    @Autowired
//    private WalletRepo walletRepo;
//    @Autowired
//    private UserService userService;
//    @Transactional
//    public Wallet createWallet(User user){
//        Wallet wallet=new Wallet();
//        wallet.setUser(user);
//        wallet.setBalance(BigDecimal.valueOf(0.0));
//       return walletRepo.save(wallet);
//    }
//    public Wallet insertWallet(){
//        Wallet wallet=new Wallet();
//        wallet.setBalance(BigDecimal.valueOf(0.0));
//        return walletRepo.save(wallet);
//    }
//    public Wallet updateWallet(Wallet wallet){
//        return walletRepo.save(wallet);
//    }
//    public Wallet getWalletById(Long walletId){
//        return walletRepo.findById(walletId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Wallet not found"));
//    }
//    public Wallet getWalletByUserId(Long userId){
//        return walletRepo.findByUserId(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Wallet not found"));
//    }
//    public BigDecimal getWalletBalance(Long userId){
//        Wallet wallet=getWalletByUserId(userId);
//        if (wallet == null) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet not found for the user");
//        }
//        return wallet.getBalance() ;
//    }
//    public Wallet deposit(Long userId,BigDecimal amount){
//        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Deposit amount must be greater than zero");
//        }
//        Wallet wallet=getWalletByUserId(userId);
//        if (wallet == null) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet not found for the user");
//        }
//        wallet.setBalance(wallet.getBalance().add(amount));
//        return walletRepo.save(wallet);
//    }
//    public Wallet withdraw(Long userId,BigDecimal amount){
//        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "withdraw amount must be greater than zero");
//        }
//        Wallet wallet=getWalletByUserId(userId);
//        if (wallet == null) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet not found for the user");
//        }
//        if(amount.compareTo(wallet.getBalance())>0){
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Insufficient balance in the wallet");
//        }
//        wallet.setBalance(wallet.getBalance().subtract(amount));
//        return walletRepo.save(wallet);
//    }
//
//}
