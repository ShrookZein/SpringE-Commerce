package com.global.ecommerce.config;

import com.global.ecommerce.Entity.RoleModel;
import com.global.ecommerce.Entity.User;
import com.global.ecommerce.Entity.walletEntity.Wallet;
import com.global.ecommerce.security.AuthService;
import com.global.ecommerce.service.authService.RoleService;
import com.global.ecommerce.service.authService.UserService;
import com.global.ecommerce.service.shopService.CartService;
import com.global.ecommerce.service.shopService.OrderService;
import com.global.ecommerce.service.walletService.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StartUpApp implements CommandLineRunner {
    private final UserService userService;

    private final RoleService roleService;

    private final AuthService authService;

    private final WalletService walletService;
    private final CartService cartService;
    private final OrderService orderService;
    Wallet wallet= new Wallet();

    @Override
    public void run(String... args) throws Exception {


        if (roleService.findAll().isEmpty()) {
            roleService.save(new RoleModel(null, "admin"));
            roleService.save(new RoleModel(null, "user"));
        }


        if (userService.findAll().isEmpty()) {

            List<RoleModel> adminRoles = new ArrayList<>();
            adminRoles.add(roleService.findByName("admin"));

            List<RoleModel> userRoles = new ArrayList<>();
            userRoles.add(roleService.findByName("user"));
            User user=userService.save(new User(null,"Norhan2001","Norhan2001@gmail.com", "12345678", adminRoles,null,true,true,true,true));

            userService.save(new User(null , "Norhan2001","AliMohamed12@gmail.com", "12345678",adminRoles,null,true,true,true,true));

            userService.save(new User( null, "Norhan2001","Zein8080@gmail.com", "12345678",userRoles,null,true,true,true,true));
            userService.save(new User( null,"Norhan2001","Gaber5050@gmail.comv", "12345678",userRoles,null,true,true,true,true));
            userService.save(new User( null, "Norhan2001","Zeinnn8070@gmail.com", "12345678",userRoles,null,true,true,true,true));
            userService.save(new User( null, "Norhan2001","Maher774@gmail.com", "12345678",userRoles,null,true,true,true,true));


//            userService.save(new User(null , "Norhan2001","AliMohamed12@gmail.com", "12345678",adminRoles,null,null,true,true,true,true));
//            userService.save(new User( null, "Norhan2001","Zein8080@gmail.com", "12345678",userRoles,null,null,true,true,true,true));
//            userService.save(new User( null,"Norhan2001","Gaber5050@gmail.comv", "12345678",userRoles,null,null,true,true,true,true));
//            userService.save(new User( null, "Norhan2001","Zeinnn8070@gmail.com", "12345678",userRoles,null,null,true,true,true,true));
//            userService.save(new User( null, "Norhan2001","Maher774@gmail.com", "12345678",userRoles,null,null,true,true,true,true));



            wallet= walletService.insertWallet();
            user.setWallet(wallet);
            userService.save(user);
            wallet.setUser(user);
            walletService.updateWallet(wallet);
//            orderService.createOrder(14L);
        }
//        cartService.deleteAllItemsInCart(1L);

//        System.out.println(cartService.deleteItemFromCart(1L,2L).getId().toString());
//        System.out.println("Done!");

    }
}
