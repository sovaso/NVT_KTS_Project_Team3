package com.test.e2e.tests;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

import com.test.e2e.pages.HomePage;
import com.test.e2e.pages.SendEmail;
import com.test.e2e.pages.SortPage;

public class TestSortItems {
	
	private WebDriver browser;

	SortPage sortPage;
	
	@Before
	public void setupSelenium() {
		// instantiate browser
		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
		browser = new ChromeDriver();
		// maximize window
		browser.manage().window().maximize();
		// navigate
		browser.navigate().to("http://automationpractice.com/index.php?controller=search&search_query=dress&submit_search=&orderby=price&orderway=asc");

		sortPage = PageFactory.initElements(browser, SortPage.class);
	}
	
	@Test
	public void test_sendEmail() throws InvocationTargetException {
		ArrayList<Double> elems=sortPage.getSorted();
		assertEquals(16.51,elems.get(0).doubleValue(),0);
		assertEquals(16.40,elems.get(1).doubleValue(),0);
		assertEquals(26.00,elems.get(2).doubleValue(),0);
		
		browser.navigate().to("http://automationpractice.com/index.php?controller=search&search_query=dress&submit_search=&orderby=price&orderway=desc");
		ArrayList<Double> elems2=sortPage.getSorted();
		assertEquals(50.99,elems2.get(0).doubleValue(),0);
		assertEquals(28.98,elems2.get(1).doubleValue(),0);
		assertEquals(30.50,elems2.get(2).doubleValue(),0);
		
		
		
		
		
	}
	
	@After
	public void closeSelenium() {
		// Shutdown the browser
		browser.quit();
	}
	

}
