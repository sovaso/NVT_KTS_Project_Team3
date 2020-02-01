package com.test.e2e.tests;

import org.junit.Before;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

import com.test.e2e.pages.HomePage;
import com.test.e2e.pages.RegisterUserPage;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

public class RegisterUserTest {
	private WebDriver browser;

	HomePage homePage;
	RegisterUserPage registerPage;
	
	Class.forName("com.mysql.jdbc.Driver");
	Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/dbteam3SeleniumTest", "root", "root");
	
	
	
	
	@Before
	public void setupSelenium() {
		// instantiate browser
		
		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
		browser = new ChromeDriver();
		// maximize window
		browser.manage().window().maximize();
		// navigate
		browser.navigate().to("http://localhost:4200/dashboard");

		homePage = PageFactory.initElements(browser, HomePage.class);
		registerPage = PageFactory.initElements(browser, RegisterUserPage.class);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void test_register() throws InvocationTargetException {
		//sva polja prazna
		homePage.ensureRegisterButtonVisible();
		homePage.getRegisterButton().click();
		assertEquals("http://localhost:4200/dashboard",browser.getCurrentUrl());
		
		registerPage.setNameEmpty();
		registerPage.setSurnameEmpty();
		registerPage.setEmailEmpty();
		registerPage.setUsernameEmpty();
		registerPage.setPasswordEmpty();
		registerPage.setRepeatedPasswordEmpty();
		registerPage.ensureRegisterVisible();
		registerPage.getRegisterButton().click();
		registerPage.ensureMessageVisible();
		String message=registerPage.getMessage().getText();
		System.out.println(message);
		assertTrue(message.contains("Please fill all fields."));
		
		registerPage.setName("user");
		registerPage.setSurname("user");
		registerPage.setEmail("a@a.com");
		registerPage.setPassword("user");
		registerPage.setUsername("user1");
		registerPage.setRepeatedPassword("user");
		registerPage.ensureRegisterVisible();
		registerPage.getRegisterButton().click();
		registerPage.ensureMessageVisible();
		message=registerPage.getMessage().getText();
		assertTrue(message.contains("Username already exist."));
		
		registerPage.setName("user");
		registerPage.setSurname("user");
		registerPage.setEmail("a");
		registerPage.setPassword("user");
		registerPage.setUsername("user1");
		registerPage.setRepeatedPassword("user");
		registerPage.ensureRegisterVisible();
		registerPage.getRegisterButton().click();
		registerPage.ensureMessageVisible();
		message=registerPage.getMessage().getText();
		assertTrue(message.contains("Invalid email."));
		
		
		registerPage.setName("user");
		registerPage.setSurname("user");
		registerPage.setEmail("leona.nedeljkovic@gmail.com");
		registerPage.setPassword("user");
		registerPage.setUsername("userjkjo");
		registerPage.setRepeatedPassword("user5");
		registerPage.ensureRegisterVisible();
		registerPage.getRegisterButton().click();
		registerPage.ensureMessageVisible();
		message=registerPage.getMessage().getText();
		assertTrue(message.contains("Passwords must match."));
		
		
		registerPage.setName("user");
		registerPage.setSurname("user");
		registerPage.setEmail("leona.nedeljkovic@gmail.com");
		registerPage.setPassword("user");
		registerPage.setUsername("userCoffe");
		registerPage.setRepeatedPassword("user");
		registerPage.ensureRegisterVisible();
		registerPage.getRegisterButton().click();
		registerPage.ensureMessageVisible();
		message=registerPage.getMessage().getText();
		System.out.println(message);
		Statement stmt = con.createStatement();	
		stmt.executeQuery("delete u from user where u.username='userCoffe';");
		
	}
	
	@After
	public void closeSelenium() {
		// Shutdown the browser
		con.close();
		browser.quit();
		
	}

}
