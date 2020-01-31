package com.test.e2e.tests;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

import com.test.e2e.pages.HomePage;
import com.test.e2e.pages.SendEmail;


public class TestSendEmail {
	
	private WebDriver browser;

	HomePage homePage;
	SendEmail sendEmail;
	
	@Before
	public void setupSelenium() {
		// instantiate browser
		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
		browser = new ChromeDriver();
		// maximize window
		browser.manage().window().maximize();
		// navigate
		browser.navigate().to("http://automationpractice.com/index.php");

		homePage = PageFactory.initElements(browser, HomePage.class);
		sendEmail = PageFactory.initElements(browser, SendEmail.class);
	}
	
	@Test
	public void test_sendEmail() throws InvocationTargetException {
		//sva polja prazna
		homePage.ensureHomePageVisible();
		homePage.getContactLink().click();
		assertEquals("http://automationpractice.com/index.php?controller=contact",
				browser.getCurrentUrl());
		sendEmail.ensureSubmitButtonIsDisplayed();
		sendEmail.getSubmitMessage().click();
		sendEmail.ensureDivIsDisplayed();
		
		String errorMessage1 = sendEmail.getErrorMessage().getText();
		assertEquals("Invalid email address.", errorMessage1);
		
//		//nevalidna email adresa
		sendEmail.setEmail("m");
		sendEmail.getSubmitMessage().click();
		sendEmail.ensureDivIsDisplayed();
		String errorMessage2 = sendEmail.getErrorMessage().getText();
		assertEquals("Invalid email address.", errorMessage2);
		
		//validna email adresa ali prazna poruka
		sendEmail.setEmail("test@kts.com");
		sendEmail.getSubmitMessage().click();
		sendEmail.ensureDivIsDisplayed();
		
		String errorMessage3 = sendEmail.getErrorMessage().getText();
		assertEquals("The message cannot be blank.", errorMessage3);
		
		
		//validna email adresa i popunjena poruka, ali subjet ne sme biti empty
		sendEmail.setEmail("test@kts.com");
		sendEmail.setMessage("message");
		sendEmail.getSubmitMessage().click();
		sendEmail.ensureDivIsDisplayed();
		String errorMessage4 = sendEmail.getErrorMessage().getText();
		
		assertEquals("Please select a subject from the list provided.", errorMessage4);
		
		
		//validna adresa, poruka, subject
		sendEmail.setEmail("test@kts.com");
		sendEmail.setMessage("message");
		sendEmail.setSubject(1);
		sendEmail.getSubmitMessage().click();
		sendEmail.ensureSuccessIsDisplayed();
		String errorMessage5 = sendEmail.getSuccess().getText();
		assertEquals("Your message has been successfully sent to our team.", errorMessage5);
		
		sendEmail.ensureHomeButtonIsDisplayed();
		sendEmail.getHomeButton().click();
		assertEquals("http://automationpractice.com/index.php",
				browser.getCurrentUrl());
		
		
		
	}
	
	@After
	public void closeSelenium() {
		// Shutdown the browser
		browser.quit();
	}
	

}
