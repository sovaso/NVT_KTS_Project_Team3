package com.test.e2e.tests;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

import com.test.e2e.pages.HomePage;
import com.test.e2e.pages.LoginPage;
import com.test.e2e.pages.ShowEventsPage;

public class SearchEventsUnsuccessfull {

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
	public void searchEvents_field_noResult() {
		homePage.ensureHomePageVisible();
		homePage.getLoginButton().click();
		loginPage.setUsername("user2");
		loginPage.setPassword("123");
		loginPage.getButton().click();
		homePage.ensureUserButtonVisible();
		homePage.getEventsLink().click();
		showEventsPage.ensureTableLoaded();
		
		//nepostojeci field unos
		showEventsPage.ensureFieldIsDisplayed();
		showEventsPage.setField("someField");
		showEventsPage.ensureSearchIsDisplayed();
		showEventsPage.getSearch().click();
		showEventsPage.ensureMessageIsDisplayed();
		assertTrue(showEventsPage.getMessage().getText().contains("No appropriate events found."));
		
		
	}
	

	@Test
	public void search_startDate_unsuccessfull() {
		
		homePage.ensureHomePageVisible();
		homePage.getLoginButton().click();
		loginPage.setUsername("user2");
		loginPage.setPassword("123");
		loginPage.getButton().click();
		homePage.ensureUserButtonVisible();
		homePage.getEventsLink().click();
		showEventsPage.ensureTableLoaded();
		
		showEventsPage.ensureStartDateIsVisible();
	
		showEventsPage.getStartDate().sendKeys("09252013");
		showEventsPage.getStartDate().sendKeys(Keys.TAB);
		showEventsPage.getStartDate().sendKeys("0245PM");
		showEventsPage.ensureSearchIsDisplayed();
		showEventsPage.getSearch().click();
		showEventsPage.ensureMessageIsDisplayed();
		assertTrue(showEventsPage.getMessage().getText().contains("No appropriate events found."));
		
	}
	
	@Test
	public void search_period_unsuccessfull() {
		
		homePage.ensureHomePageVisible();
		homePage.getLoginButton().click();
		loginPage.setUsername("user2");
		loginPage.setPassword("123");
		loginPage.getButton().click();
		homePage.ensureUserButtonVisible();
		homePage.getEventsLink().click();
		showEventsPage.ensureTableLoaded();
		
		showEventsPage.ensureStartDateIsVisible();
	
		showEventsPage.getStartDate().sendKeys("01012025");
		showEventsPage.getStartDate().sendKeys(Keys.TAB);
		showEventsPage.getStartDate().sendKeys("0000AM");
		
		showEventsPage.getEndDate().sendKeys("03122021");
		showEventsPage.getEndDate().sendKeys(Keys.TAB);
		showEventsPage.getEndDate().sendKeys("0000AM");
		showEventsPage.ensureTableLoaded();
	
	
		showEventsPage.ensureSearchIsDisplayed();
		showEventsPage.getSearch().click();
		
		showEventsPage.ensureMessageIsDisplayed();
		assertTrue(showEventsPage.getMessage().getText().contains("No appropriate events found."));
		
		
	}
	
	@Test
	public void search_period_field_unsuccessfull() {
		
		homePage.ensureHomePageVisible();
		homePage.getLoginButton().click();
		loginPage.setUsername("user2");
		loginPage.setPassword("123");
		loginPage.getButton().click();
		homePage.ensureUserButtonVisible();
		homePage.getEventsLink().click();
		showEventsPage.ensureTableLoaded();
		
		showEventsPage.setField("someField");
		showEventsPage.ensureStartDateIsVisible();
		showEventsPage.getStartDate().sendKeys("01012025");
		showEventsPage.getStartDate().sendKeys(Keys.TAB);
		showEventsPage.getStartDate().sendKeys("0000AM");
		
		showEventsPage.getEndDate().sendKeys("03122021");
		showEventsPage.getEndDate().sendKeys(Keys.TAB);
		showEventsPage.getEndDate().sendKeys("0000AM");
		showEventsPage.ensureTableLoaded();
	
	
		showEventsPage.ensureSearchIsDisplayed();
		showEventsPage.getSearch().click();
		
		showEventsPage.ensureMessageIsDisplayed();
		assertTrue(showEventsPage.getMessage().getText().contains("No appropriate events found."));
		
		
	}
	
	@Test
	public void search_endDate_unsuccessfull() {
		
		homePage.ensureHomePageVisible();
		homePage.getLoginButton().click();
		loginPage.setUsername("user2");
		loginPage.setPassword("123");
		loginPage.getButton().click();
		homePage.ensureUserButtonVisible();
		homePage.getEventsLink().click();
		showEventsPage.ensureTableLoaded();
		
		showEventsPage.ensureEndDateIsVisible();
	
		showEventsPage.getEndDate().sendKeys("09252013");
		showEventsPage.getEndDate().sendKeys(Keys.TAB);
		showEventsPage.getEndDate().sendKeys("0245PM");
		showEventsPage.ensureSearchIsDisplayed();
		showEventsPage.getSearch().click();
		showEventsPage.ensureMessageIsDisplayed();
		assertTrue(showEventsPage.getMessage().getText().contains("No appropriate events found."));
		
	}
	
	
}
