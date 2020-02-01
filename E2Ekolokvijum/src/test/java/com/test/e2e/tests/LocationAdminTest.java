package com.test.e2e.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.test.e2e.pages.HomePage;
import com.test.e2e.pages.LocationAdminPage;
import com.test.e2e.pages.RegisterUserPage;

public class LocationAdminTest {
	private WebDriver browser;

	HomePage homePage;
	LocationAdminPage locationAdminPage;
	
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
		locationAdminPage = PageFactory.initElements(browser, LocationAdminPage.class);
	}
	
	@SuppressWarnings("deprecation")
	@Test
	@Transactional
	@Rollback(true)
	public void test_locationAdmin() throws InvocationTargetException {
		homePage.ensureHomePageVisible();
		homePage.getLoginButton().click();
		WebElement username=  browser.findElement(By.xpath("/html/body/ngb-modal-window/div/div/app-login/div/div/div[2]/form/input[1]"));
		WebElement password=  browser.findElement(By.xpath("/html/body/ngb-modal-window/div/div/app-login/div/div/div[2]/form/input[2]"));
		username.clear();
		username.sendKeys("user2");
		password.clear();
		password.sendKeys("123");
		WebElement button=(new WebDriverWait(browser, 10)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/ngb-modal-window/div/div/app-login/div/div/div[2]/form/button")));
		assertTrue(button.isDisplayed());
		button.click();
		
		locationAdminPage.ensureRegisterVisible();
		locationAdminPage.getLocations().click();
		
		(new WebDriverWait(browser, 10))
		  .until(ExpectedConditions.numberOfElementsToBeMoreThan(
				  By.cssSelector("tr"), 0));
		
		WebElement showReport=(new WebDriverWait(browser, 10)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/app-root/div/app-dashboard/div/app-locations/div/div/table/tbody/tr[1]/ngb-tab/td[3]/button")));
		assertTrue(showReport.isDisplayed());
		showReport.click();
		(new WebDriverWait(browser, 10))
		  .until(ExpectedConditions.textToBePresentInElementLocated(
				  By.xpath("/html/body/ngb-modal-window/div/div/app-location-report/div/h4[1]"),"A) Daily"));
		
	}
	
	@After
	public void closeSelenium() {
		// Shutdown the browser
		browser.quit();
	}
	

}
