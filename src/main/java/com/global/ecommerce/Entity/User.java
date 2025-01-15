package com.global.ecommerce.Entity;

//import jakarta.persistence.*;
import com.global.ecommerce.Entity.shopEntity.Cart;
import com.global.ecommerce.Entity.walletEntity.Wallet;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
//@Builder
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "use_id")
    private Long id;
    @NotBlank(message = "Name of user is mandatory")
    private String fullName;
    @Email
    @NotBlank(message = "Email of user is mandatory")
    private String email;
    @NotBlank(message = "Password of user is mandatory")
    private String password;
    @NotNull
//    @ManyToMany(cascade=CascadeType.DETACH)
    @ManyToMany(fetch = FetchType.EAGER)
//    @JsonIgnore
    @JoinTable(name = "sec_user_roles",joinColumns = @JoinColumn(name = "user_id",referencedColumnName = "use_id"),inverseJoinColumns = @JoinColumn(name = "role_id"))
    @OrderColumn(name = "id")
    private List<RoleModel>roles;

//    @NotNull
    //Relation with wallet
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id",nullable = true)
    @ToString.Exclude
    private Wallet wallet;
//    @OneToOne()
//    @ToString.Exclude
//    private Cart cart;

    private boolean isEnabled;

    private boolean isCredentialsNonExpired;

    private boolean isAccountNonLocked;

    private boolean isAccountNonExpired;

    public User(Long userId) {
        super();
        this.id=userId;
    }
}
