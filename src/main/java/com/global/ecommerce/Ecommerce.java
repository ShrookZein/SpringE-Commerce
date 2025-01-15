package com.global.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Ecommerce {

	public static void main(String[] args) {
		SpringApplication.run(Ecommerce.class, args);
//		Long x= 1L;
//		UserService userService=new UserService();
//		RoleService roleService=new RoleService();
//		final PasswordEncoder passwordEncoder = null;
//
//			List<RoleModel> userRoles = new ArrayList<>();
//			userRoles.add(roleService.findByName("user"));
//			WalletService walletService = new WalletService();
//
//			User user = new User(null, "zee", "ss@gmail.com", "12345678", userRoles,null, true, true, true, true);
//		user.setPassword(passwordEncoder.encode(user.getPassword()));
//		// Save user first without the wallet
//			user.setWallet(null);
//			User savedUser = userService.save(user);
//			// Create wallet and link it to the saved user
//			Wallet wallet = walletService.createWallet(savedUser);
//			savedUser.setWallet(wallet);
//			userRepository.save(savedUcser);
//			userService.save(user);
//			walletService.createWallet(user);
//
//		TransactionService transactionService = new TransactionService();
//		Transaction transaction = transactionService.insert(new Wallet(null, null, new User(1L), null)
//				, BigDecimal.valueOf(10.0), TransactionType.DEPOSIT, TransactionStatus.COMPLETED);
//		System.out.println(transaction);

	}

}
