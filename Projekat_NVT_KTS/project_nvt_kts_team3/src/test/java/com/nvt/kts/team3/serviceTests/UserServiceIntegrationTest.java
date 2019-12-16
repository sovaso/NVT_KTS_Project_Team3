package com.nvt.kts.team3.serviceTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.nvt.kts.team3.model.RegularUser;
import com.nvt.kts.team3.model.User;
import com.nvt.kts.team3.service.UserService;
import com.nvt.kts.team3.service.impl.UserServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class UserServiceIntegrationTest {
	@Autowired
	private UserServiceImpl userService;
	
	@Test
	@Transactional
	public void editProfileUsernameDoesNotExist() {
		User user = (User) new RegularUser();
		user.setUsername("someUsername");
		String result = userService.editProfile(user);
		assertEquals("User with given username does not exist.", result);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void editProfileSuccessfull() {
		User user = (User) new RegularUser();
		user.setUsername("user1");
		user.setName("NewName");
		user.setSurname("NewSurname");
		assertNull(userService.editProfile(user));
	
	}
}
