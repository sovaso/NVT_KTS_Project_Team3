package com.test.e2e.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

import com.test.e2e.pages.HomePageLogin;
import com.test.e2e.pages.HomePageSearch;
import com.test.e2e.pages.LoginPage;
import com.test.e2e.pages.SearchPage;

public class TestSearch {

	private WebDriver browser;
	
	HomePageSearch homePage;
	SearchPage searchPage;
	
	
	@Before
	public void setupSelenium() {
		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
		browser = new ChromeDriver();
		browser.manage().window().maximize();
		browser.navigate().to("http://automationpractice.com/index.php");
		homePage = PageFactory.initElements(browser, HomePageSearch.class);
		searchPage = PageFactory.initElements(browser, SearchPage.class);
	}
	
	
	@Test
	public void test() {
		//no input data
		homePage.ensureHomePageVisible();
		homePage.getSearchButton().click();
		searchPage.ensureErrorMessageVisible();
		String errorMessage1 = searchPage.getErrorMessage().getText();
		assertEquals("Please enter a search keyword", errorMessage1);
		
		//input data invalid
		homePage.setSearchInput("d");
		homePage.getSearchButton().click();
		searchPage.ensureErrorMessageVisible();
		String errorMessage2 = searchPage.getErrorMessage().getText();
		assertEquals("No results were found for your search \"d\"", errorMessage2);
		
		//success
		homePage.setSearchInput("dress");
		homePage.getSearchButton().click();
		searchPage.ensureSuccessMessageVisible();
		String successMessage = searchPage.getSuccessMessage().getText();
		assertEquals("7 results have been found.", successMessage);
		List<WebElement> searchResultTitles = searchPage.getSearchResultTitles();
		searchResultTitles.forEach(dress -> assertTrue(dress.getText().toLowerCase().contains("dress")));
	}
	
	@After
	public void closeSelenium() {
		browser.quit();
	}
}
