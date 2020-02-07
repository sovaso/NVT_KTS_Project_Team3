package com.test.e2e.tests;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

import com.test.e2e.pages.HomePage;
import com.test.e2e.pages.LoginPage;
import com.test.e2e.pages.ShowEventsPage;
public class TestShowEvents {

	private WebDriver browser;
	
	HomePage homePage;
	ShowEventsPage showEventsPage;
	LoginPage loginPage;
	
	
	@Before
	public void setupSelenium() {
		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
		browser = new ChromeDriver();
		browser.manage().window().maximize();
		browser.navigate().to("http://localhost:4200/dashboard");
		homePage = PageFactory.initElements(browser, HomePage.class);
		showEventsPage = PageFactory.initElements(browser, ShowEventsPage.class);
		loginPage = PageFactory.initElements(browser, LoginPage.class);
	}
	
	
	@Test
	public void showEvents_test() {
		homePage.ensureHomePageVisible();
		homePage.getLoginButton().click();
		loginPage.setUsername("user2");
		loginPage.setPassword("123");
		loginPage.getButton().click();
		homePage.ensureUserButtonVisible();
		homePage.getEventsLink().click();
		showEventsPage.ensureTableLoaded();
		List<WebElement> events1 = showEventsPage.getEvents();
		assertEquals(9, events1.size());
	}
	
	
	@Test
	public void sortEventsDesc_test() {
		homePage.getEventsLink().click();
		showEventsPage.ensureTableLoaded();
		showEventsPage.ensureSortByDateDescLoaded();
		showEventsPage.getSortByDateDesc().click();
		showEventsPage.ensureTableLoaded();
		List<WebElement> events;
		while(true) {
			events = showEventsPage.getEvents();
			if (events.size() == 13) {
				break;
			}
		}
		showEventsPage.ensureTableFilledDesc();
		
		assertEquals("3", events.get(0).getText());
		assertEquals("1", events.get(1).getText());
		assertEquals("1", events.get(2).getText());
		assertEquals("2", events.get(3).getText());
		assertEquals("5", events.get(4).getText());
		assertEquals("4", events.get(5).getText());
		assertEquals("7", events.get(6).getText());
		assertEquals("8", events.get(7).getText());
		assertEquals("6", events.get(8).getText());
		assertEquals("4", events.get(9).getText());
		assertEquals("9", events.get(10).getText());
		assertEquals("8", events.get(11).getText());
		assertEquals("7", events.get(12).getText());
		
	}

	@Test
	public void sortEventsAcs_test() {
		homePage.getEventsLink().click();
		showEventsPage.ensureTableLoaded();
		showEventsPage.getSortByDateAcs().click();
		showEventsPage.ensureTableLoaded();
		List<WebElement> events = null;
		
		while(true) {
			events = showEventsPage.getEvents();
			if (events.size()==13) {
				break;
			}
		}
	
		assertEquals("8", events.get(0).getText());
		assertEquals("7", events.get(1).getText());
		assertEquals("9", events.get(2).getText());
		assertEquals("4", events.get(3).getText());
		assertEquals("6", events.get(4).getText());
		assertEquals("8", events.get(5).getText());
		assertEquals("7", events.get(6).getText());
		assertEquals("4", events.get(7).getText());
		assertEquals("5", events.get(8).getText());
		assertEquals("2", events.get(9).getText());
		assertEquals("1", events.get(10).getText());
		assertEquals("1", events.get(11).getText());
		assertEquals("3", events.get(12).getText());
	}
	
	
			
	@After
	public void closeSelenium() {
		browser.quit();
	}
}
