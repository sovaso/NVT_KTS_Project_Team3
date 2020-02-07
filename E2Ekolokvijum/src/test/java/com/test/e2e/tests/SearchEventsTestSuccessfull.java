package com.test.e2e.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

import com.test.e2e.pages.HomePage;
import com.test.e2e.pages.LoginPage;
import com.test.e2e.pages.ShowEventsPage;

public class SearchEventsTestSuccessfull {

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
	public void searchEvents_field_successfull() {
		homePage.ensureHomePageVisible();
		homePage.getLoginButton().click();
		loginPage.setUsername("user2");
		loginPage.setPassword("123");
		loginPage.getButton().click();
		homePage.ensureUserButtonVisible();
		homePage.getEventsLink().click();
		showEventsPage.ensureTableLoaded();
		
		showEventsPage.ensureFieldIsDisplayed();
		showEventsPage.setField("event2");
		showEventsPage.ensureSearchIsDisplayed();
		showEventsPage.getSearch().click();
		showEventsPage.ensureTableLoaded();
		showEventsPage.ensureFieldEvent2();
		List<WebElement> events = showEventsPage.getEvents();
		assertEquals(1, events.size());
		
		
	}
	
	
	
	
	
	
	@Test
	public void search_date_successfull() {
		
		homePage.ensureHomePageVisible();
		homePage.getLoginButton().click();
		loginPage.setUsername("user2");
		loginPage.setPassword("123");
		loginPage.getButton().click();
		homePage.ensureUserButtonVisible();
		homePage.getEventsLink().click();
		showEventsPage.ensureTableLoaded();
		
		showEventsPage.ensureStartDateIsVisible();
	
		showEventsPage.getStartDate().sendKeys("01012018");
		
		showEventsPage.getStartDate().sendKeys(Keys.TAB);
		showEventsPage.getStartDate().sendKeys("0000AM");
		showEventsPage.ensureTableLoaded();
	
	
		showEventsPage.ensureSearchIsDisplayed();
		showEventsPage.getSearch().click();
		
		showEventsPage.ensureTableLoaded();
		List<WebElement> events;
		while(true) {
			events = showEventsPage.getEvents();
			if (events.size()==2) {
				break;
			}
		}
				
		assertEquals(2, events.size());
		
		
	}
	
	@Test
	public void search_period_successfull() {
		
		homePage.ensureHomePageVisible();
		homePage.getLoginButton().click();
		loginPage.setUsername("user2");
		loginPage.setPassword("123");
		loginPage.getButton().click();
		homePage.ensureUserButtonVisible();
		homePage.getEventsLink().click();
		showEventsPage.ensureTableLoaded();
		
		showEventsPage.ensureStartDateIsVisible();
	
		showEventsPage.getStartDate().sendKeys("01012021");
		showEventsPage.getStartDate().sendKeys(Keys.TAB);
		showEventsPage.getStartDate().sendKeys("0000AM");
		
		showEventsPage.getEndDate().sendKeys("03122021");
		showEventsPage.getEndDate().sendKeys(Keys.TAB);
		showEventsPage.getEndDate().sendKeys("0000AM");
		showEventsPage.ensureTableLoaded();
	
	
		showEventsPage.ensureSearchIsDisplayed();
		showEventsPage.getSearch().click();
		
		showEventsPage.ensureTableLoaded();
		List<WebElement> events;
		while(true) {
			events = showEventsPage.getEvents();
			if (events.size()==2) {
				break;
			}
		}
				
		assertEquals(2, events.size());
		
		
	}
	
	
	
	
	@Test
	public void search_period_field_successfull() {
		
		homePage.ensureHomePageVisible();
		homePage.getLoginButton().click();
		loginPage.setUsername("user2");
		loginPage.setPassword("123");
		loginPage.getButton().click();
		homePage.ensureUserButtonVisible();
		homePage.getEventsLink().click();
		showEventsPage.ensureTableLoaded();
		
		showEventsPage.setField("sports");
		showEventsPage.ensureStartDateIsVisible();
		showEventsPage.getStartDate().sendKeys("01012021");
		showEventsPage.getStartDate().sendKeys(Keys.TAB);
		showEventsPage.getStartDate().sendKeys("0000AM");
		
		showEventsPage.getEndDate().sendKeys("03122021");
		showEventsPage.getEndDate().sendKeys(Keys.TAB);
		showEventsPage.getEndDate().sendKeys("0000AM");
		showEventsPage.ensureTableLoaded();
	
	
		showEventsPage.ensureSearchIsDisplayed();
		showEventsPage.getSearch().click();
		
		showEventsPage.ensureTableLoaded();
		List<WebElement> events;
		while(true) {
			events = showEventsPage.getEvents();
			if (events.size()==1) {
				break;
			}
		}
				
		assertEquals(1, events.size());
		
		
	}
	
	
	
}
