package com.test.e2e.tests;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

import com.test.e2e.pages.HomePage;

public class TestLogin {
/*
	private WebDriver browser;
	
	HomePageLogin homePage;
	LoginPage loginPage;
	
	@Before
	public void setupSelenium() {
		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
		browser = new ChromeDriver();
		browser.manage().window().maximize();
		browser.navigate().to("http://automationpractice.com/index.php");
		homePage = PageFactory.initElements(browser, HomePageLogin.class);
		loginPage = PageFactory.initElements(browser, LoginPage.class);
	}
	
	@Test
	public void login_test() {
		homePage.ensureHomePageVisible();
		homePage.getLoginLink().click();
		assertEquals("http://automationpractice.com/index.php?controller=authentication&back=my-account", browser.getCurrentUrl());
		
		//prazna polja
		loginPage.ensureButtonVisible();
		loginPage.getButton().click();
		String errorMessage1 = loginPage.getError().getText();
		assertEquals("An email address required.", errorMessage1);
		
		
		//prazan password
		loginPage.setEmail("marina.vojnovic1997@gmail.com");
		loginPage.getButton().click();
		String errorMessage2 = loginPage.getError().getText();
		assertEquals("Password is required.", errorMessage2);
		
		
		//neispravna adresa
		loginPage.setEmail("marina");
		loginPage.getButton().click();
		String errorMessage3 = loginPage.getError().getText();
		assertEquals("Invalid email address.", errorMessage3);
		
		//neispravan password
		loginPage.setEmail("test@kts.com");
		loginPage.setPassword("j");
		loginPage.getButton().click();
		String errorMessage4 = loginPage.getError().getText();
		assertEquals("Invalid password.", errorMessage4);
		
		//sve ispravno
		loginPage.setEmail("test@kts.com");
		loginPage.setPassword("johnsmith");
		loginPage.getButton().click();
		assertEquals("http://automationpractice.com/index.php?controller=my-account", browser.getCurrentUrl());
		
	}
	
	@After
	public void closeSelenium() {
		browser.quit();
	}
	*/
}
