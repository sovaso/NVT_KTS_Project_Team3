package com.nvt.kts.team3.serviceTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.nvt.kts.team3.model.RegularUser;
import com.nvt.kts.team3.model.User;
import com.nvt.kts.team3.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class UserServiceIntegrationTest {
	@Autowired
	private UserService userService;
	
	@Test
	public void editProfileUsernameDoesNotExist() {
		User user = (User) new RegularUser();
		user.setUsername("someUsername");
		String result = userService.editProfile(user);
		assertEquals("User with given username does not exist.", result);
	}
	
	@Test
	public void editProfileSuccessfull() {
		User user = (User) new RegularUser();
		user.setUsername("user1");
		user.setName("NewName");
		user.setSurname("NewSurname");
		assertNull(userService.editProfile(user));
	
	}
}
