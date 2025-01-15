package com.global.ecommerce.Entity.walletEntity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Table(name = "transactions")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private BigDecimal amount;
//    @NotNull
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "wallet_id")
    @JsonBackReference
    private Wallet wallet;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType; //Deposit or withdrawal
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus; //PENDING Or COMPLETED or FAILED
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp timestamp;

}
