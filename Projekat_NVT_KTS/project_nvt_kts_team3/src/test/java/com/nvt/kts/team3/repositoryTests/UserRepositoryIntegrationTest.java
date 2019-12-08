package com.nvt.kts.team3.repositoryTests;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;


import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.EventType;
import com.nvt.kts.team3.model.User;
import com.nvt.kts.team3.repository.EventRepository;
import com.nvt.kts.team3.repository.UserRepository;



@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserRepositoryIntegrationTest {

	@Autowired
	public UserRepository userRepository;
	
	@Test
	public void findByUsername_nonFound() {
		User user = userRepository.findByUsername("someUsername");
		assertNull(user);
	}
	
	@Test
	public void findByUsername_successfull() {
		User user = userRepository.findByUsername("user1");
		assertEquals("user1", user.getName());
		assertEquals("user1", user.getSurname());
		assertEquals("a@a", user.getEmail());
		assertTrue(user.isEnabled());
	}
	
	@Test
	public void findById_nonFound() {
		User user = userRepository.findById(929L);
		assertNull(user);
	}
	
	@Test
	public void findById_successfull() {
		User user = userRepository.findById(1L);
		assertEquals("user1", user.getUsername());
		assertEquals("user1", user.getName());
		assertEquals("user1", user.getSurname());
		assertEquals("a@a", user.getEmail());
		assertTrue(user.isEnabled());
	}
	
	@Test
	public void findByToken_nonFound() {
		User user = userRepository.findByToken("xxx");
		assertNull(user);
	}
	
	@Test
	public void findByToken_successfull() {
		User user = userRepository.findByToken("aaa");
		assertEquals("user1", user.getUsername());
		assertEquals("user1", user.getName());
		assertEquals("user1", user.getSurname());
		assertEquals("a@a", user.getEmail());
		assertTrue(user.isEnabled());
	}
}
