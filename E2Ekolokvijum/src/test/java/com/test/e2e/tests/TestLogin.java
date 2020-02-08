package com.test.e2e.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

import com.test.e2e.pages.HomePage;
import com.test.e2e.pages.LoginPage;

public class TestLogin {

	private WebDriver browser;
	
	HomePage homePage;
	LoginPage loginPage;
	
	@Before
	public void setupSelenium() {
		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
		browser = new ChromeDriver();
		browser.manage().window().maximize();
		browser.navigate().to("http://localhost:4200/dashboard");
		homePage = PageFactory.initElements(browser, HomePage.class);
		loginPage = PageFactory.initElements(browser, LoginPage.class);
	}
	
	@Test
	public void login_test() {
		homePage.ensureHomePageVisible();
		homePage.getLoginButton().click();
		
		//prazna polja
		loginPage.ensureButtonVisible();
		loginPage.getButton().click();
		String errorMessage1 = loginPage.getMessage().getText();
		assertTrue(errorMessage1.contains("Please enter username and password."));
		
		//prazan password
		loginPage.setUsername("maki");
		loginPage.getButton().click();
		String errorMessage2 = loginPage.getMessage().getText();
		assertTrue(errorMessage2.contains("Please enter username and password."));
		
		//prazan username
		loginPage.setPassword("maki");
		loginPage.getButton().click();
		String errorMessage3 = loginPage.getMessage().getText();
		assertTrue(errorMessage3.contains("Please enter username and password."));
		
		//neispravan username
		loginPage.setUsername("maki");
		loginPage.setPassword("123");
		loginPage.getButton().click();
		String errorMessage4 = loginPage.getMessage().getText();
		assertTrue(errorMessage4.contains("Wrong password or username."));
		
		//neispravan password
		loginPage.setUsername("user1");
		loginPage.setPassword("nesto");
		loginPage.getButton().click();
		String errorMessage5 = loginPage.getMessage().getText();
		assertTrue(errorMessage5.contains("Wrong password or username."));
		
		//sve ispravno
		loginPage.setUsername("user1");
		loginPage.setPassword("123");
		loginPage.getButton().click();
		homePage.ensureUserButtonVisible();
		
		
	}
	
	@After
	public void closeSelenium() {
		browser.quit();
	}
	
	
	
}
