package com.global.ecommerce.Entity.walletEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.global.ecommerce.Entity.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "wallets")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Min(value = 0,message = "balance must be greater than or equal 0")
    private BigDecimal balance;
//    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    private User user;
//    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, orphanRemoval = false)
    @OneToMany(mappedBy = "wallet",cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonManagedReference
//    @JsonIgnore
    private List<Transaction> transactions=new ArrayList<>();
    public void  addTransaction(Transaction transaction){
        if (transactions == null) {

            throw new IllegalArgumentException("Transaction cannot be null");
        }
        transaction.setWallet(this);
        transactions.add(transaction);

    }
}
