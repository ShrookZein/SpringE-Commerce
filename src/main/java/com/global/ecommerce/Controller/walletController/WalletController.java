package com.global.ecommerce.Controller.walletController;

import com.global.ecommerce.Entity.walletEntity.TransactionStatus;
import com.global.ecommerce.Entity.walletEntity.TransactionType;
import com.global.ecommerce.Entity.walletEntity.Wallet;
import com.global.ecommerce.security.JwtTokenUtils;
import com.global.ecommerce.service.walletService.TransactionService;
import com.global.ecommerce.service.walletService.WalletService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
@Validated
@RestController
@RequestMapping("/api/v1/wallet")
public class WalletController {
    @Autowired
    private WalletService walletService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    JwtTokenUtils jwtTokenUtils;
    @Transactional
    @PostMapping("/deposit/{amount}")
    public ResponseEntity<Map<String,Object>>deposit( @Min(value= 0,message = "amount must be greater than or equal 0")@PathVariable BigDecimal amount,@NonNull HttpServletRequest req){
        Map<String,Object>response=new HashMap<>();
        Long userId= jwtTokenUtils.getUserIdFromToken(req);
        try{
            Wallet wallet=walletService.deposit(userId,amount);
            transactionService.insert(wallet,amount, TransactionType.DEPOSIT, TransactionStatus.COMPLETED);
            response.put("success", true);
            response.put("message", "Wallet deposited successfully");
            Map<String, Object> data = new HashMap<>();
            data.put("wallet", wallet);
            response.put("data", data);
            return ResponseEntity.ok(response);

        }catch (Exception e){
            Wallet wallet=walletService.getWalletByUserId(userId);
            transactionService.insert(wallet,amount, TransactionType.DEPOSIT, TransactionStatus.FAILED);
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.badRequest().body(response);
        }
    }
    @Transactional
    @PostMapping("/withdraw/{amount}")
    public ResponseEntity<Map<String,Object>>withdraw(@NonNull HttpServletRequest req, @Min(value= 0,message = "amount must be greater than or equal 0") @PathVariable BigDecimal amount){
        Map<String,Object>response=new HashMap<>();
        Long userId= jwtTokenUtils.getUserIdFromToken(req);
        try{
            Wallet wallet=walletService.withdraw(userId,amount);
            transactionService.insert(wallet,amount, TransactionType.WITHDRAWAL, TransactionStatus.COMPLETED);
            response.put("success", true);
            response.put("message", "wallet withdraw successfully");
            Map<String, Object> data = new HashMap<>();
            data.put("wallet", wallet);
            response.put("data", data);
            return ResponseEntity.ok(response);

        }catch (Exception e){
            Wallet wallet=walletService.getWalletByUserId(userId);
            transactionService.insert(wallet,amount, TransactionType.WITHDRAWAL, TransactionStatus.FAILED);
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.badRequest().body(response);
        }
    }
    @GetMapping("/balance")
    public ResponseEntity<BigDecimal>getWalletBalance(@NonNull HttpServletRequest req){
        Long userId= jwtTokenUtils.getUserIdFromToken(req);
        return ResponseEntity.ok(walletService.getWalletBalance(userId));
    }
}
