package com.global.ecommerce;

import com.global.ecommerce.Entity.User;
import com.global.ecommerce.service.authService.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EcommerceTest {
	@Autowired
	private UserService userService;

	@Test
	void findUserByIdNotFoundTest() {
		Boolean x=userService.userExistsById(10000L);
		assertEquals(false,x);
//		assert equals(x);
	}
	@Test
	void findUserByIdFoundTest() {
		User user=userService.findById(14L);
		assertEquals("Shrookkkk@gmail.com",user.getEmail());
//		assert equals(x);
	}

}
