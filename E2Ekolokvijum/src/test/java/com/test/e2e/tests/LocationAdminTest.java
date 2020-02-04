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
	
	@Test
	@Transactional
	@Rollback(true)
	public void test_locationAdmin() throws InvocationTargetException {
		//go to location list, ensure list is not empty
		homePage.ensureHomePageVisible();
		homePage.getLoginButton().click();
		WebElement username=  browser.findElement(By.xpath("/html/body/ngb-modal-window/div/div/app-login/div/div/div[2]/form/input[1]"));
		WebElement password=  browser.findElement(By.xpath("/html/body/ngb-modal-window/div/div/app-login/div/div/div[2]/form/input[2]"));
		username.clear();
		username.sendKeys("user3");
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
		
		
		//open reports
		WebElement showReport=(new WebDriverWait(browser, 10)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/app-root/div/app-dashboard/div/app-locations/div/div/table/tbody/tr[1]/ngb-tab/td[3]/button")));
		assertTrue(showReport.isDisplayed());
		showReport.click();
		(new WebDriverWait(browser, 10))
		  .until(ExpectedConditions.textToBePresentInElementLocated(
				  By.xpath("/html/body/ngb-modal-window/div/div/app-location-report/div/h4[1]"),"A) Daily"));
		
		WebElement x=(new WebDriverWait(browser, 10)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/ngb-modal-window/div/div/app-location-report/div/button")));
		assertTrue(x.isDisplayed());
		x.click();
		
		//add new location emptyFields
		WebElement createNew=(new WebDriverWait(browser, 10)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/app-root/div/app-dashboard/div/app-locations/div/ngb-tab/button")));
		assertTrue(createNew.isDisplayed());
		createNew.click();
		locationAdminPage.getCreateLocationButton().click();
		locationAdminPage.ensureMessageVisible();
		String message=locationAdminPage.getMessage().getText();
		assertTrue(message.contains("Location name,address and description cannot be blank!"));
		locationAdminPage.ensureCloseButtonVisible();
		locationAdminPage.getCloseMessage().click();
		
		//locaion with this name and address already exists
		locationAdminPage.setNewName("Name1");
		locationAdminPage.setNewAddress("Address1");
		locationAdminPage.setNewDescription("Description1");
		locationAdminPage.getCreateLocationButton().click();
		locationAdminPage.ensureMessageVisible();
		message=locationAdminPage.getMessage().getText();
		assertTrue(message.contains("Location with this name and address already exists!"));
		locationAdminPage.ensureCloseButtonVisible();
		locationAdminPage.getCloseMessage().click();

		
		
		//must have at least one location zone
		locationAdminPage.setNewName("Name50");
		locationAdminPage.setNewAddress("Address1");
		locationAdminPage.setNewDescription("Description1");
		locationAdminPage.getCreateLocationButton().click();
		locationAdminPage.ensureMessageVisible();
		message=locationAdminPage.getMessage().getText();
		assertTrue(message.contains("Must have at least one location zone!"));
		locationAdminPage.ensureCloseButtonVisible();
		locationAdminPage.getCloseMessage().click();
		
		//must have at least one location zone
		locationAdminPage.setNewName("Name50");
		locationAdminPage.setNewAddress("Address1");
		locationAdminPage.setNewDescription("Description1");
		locationAdminPage.getCreateLocationButton().click();
		locationAdminPage.ensureMessageVisible();
		message=locationAdminPage.getMessage().getText();
		assertTrue(message.contains("Must have at least one location zone!"));
		locationAdminPage.ensureCloseButtonVisible();
		locationAdminPage.getCloseMessage().click();
		
		//must have at least one location zone
		locationAdminPage.setNewName("Name50");
		locationAdminPage.setNewAddress("Address1");
		locationAdminPage.setNewDescription("Description1");
		locationAdminPage.ensureAddLZVisible();
		locationAdminPage.getAddNewLocationZoneButton().click();
		locationAdminPage.ensureAddFinalLZButtonVisible();
		locationAdminPage.getAddLZFinalButton().click();
		message=locationAdminPage.getMessage().getText();
		assertTrue(message.contains("Name of location zone is required!"));
		locationAdminPage.ensureCloseButtonVisible();
		locationAdminPage.getCloseMessage().click();
		

        //Row and colomn number must be integers greater than 0!
		locationAdminPage.setNewName("Name50");
		locationAdminPage.setNewAddress("Address1");
		locationAdminPage.setNewDescription("Description1");
		locationAdminPage.ensureAddLZVisible();
		locationAdminPage.getAddNewLocationZoneButton().click();
		locationAdminPage.setLocationZoneName("LZ1");
		locationAdminPage.ensureAddFinalLZButtonVisible();
		locationAdminPage.getAddLZFinalButton().click();
		message=locationAdminPage.getMessage().getText();
		assertTrue(message.contains("Row and colomn number must be integers greater than 0!"));
		locationAdminPage.ensureCloseButtonVisible();
		locationAdminPage.getCloseMessage().click();
		
		//success
		locationAdminPage.setNewName("Name8");
		locationAdminPage.setNewAddress("Address1");
		locationAdminPage.setNewDescription("Description1");
		locationAdminPage.ensureAddLZVisible();
		locationAdminPage.setLocationZoneName("LZ1");
		locationAdminPage.setLocationZoneRow("10");
		locationAdminPage.setLocationZoneCol("10");
		locationAdminPage.ensureAddFinalLZButtonVisible();
		locationAdminPage.getAddLZFinalButton().click();
		locationAdminPage.getCreateLocationButton().click();
		locationAdminPage.ensureMessageFinalVisible();
		String message2=locationAdminPage.getMessageFinal().getText();
		System.out.println("Message2 "+message2);
		assertTrue(message2.contains("Location successfully created."));
		locationAdminPage.ensureCloseSuccessVisible();
		locationAdminPage.getCloseSuccess().click();
		
		
		//edit location not allowed
		locationAdminPage.ensureEditNotAllowed();
		locationAdminPage.getEditButtonLocationFirst().click();
		locationAdminPage.ensureChangeLocationZoneVisible();
		locationAdminPage.setNameNotChangebleEdit("name2");
		locationAdminPage.getChangeLocationZone().click();
		locationAdminPage.ensureMessageVisible();
		String messageEdit=locationAdminPage.getMessage().getText();
		System.out.println(messageEdit);
		assertTrue(messageEdit.contains("Location zone not changeable."));
		locationAdminPage.ensureCloseButtonVisible();
		locationAdminPage.getCloseMessage().click();
		locationAdminPage.ensureXEditModalVisible();
		locationAdminPage.getxEditModal().click();
		
		//edit location allowed
		locationAdminPage.ensureEditAllowed();
		locationAdminPage.getEditButtonLocationSecond().click();
		locationAdminPage.ensureChangeLocationZoneVisible();
		locationAdminPage.setNameNotChangebleEdit("name200");
		locationAdminPage.getChangeLocationZone().click();
		locationAdminPage.ensureMessageVisible();
		messageEdit=locationAdminPage.getMessage().getText();
		System.out.println(messageEdit);
		assertTrue(messageEdit.contains("Location zone successfully updated."));
		locationAdminPage.ensureCloseButtonVisible();
		locationAdminPage.getCloseMessage().click();
		locationAdminPage.ensureXEditModalVisible();
		locationAdminPage.getxEditModal().click();
		
		
		
		//delete location not allowed
		locationAdminPage.ensureDLNAVisible();
		locationAdminPage.getDeleteLocationNotAllowed().click();
		locationAdminPage.ensureMessageFinalVisible();
		message=locationAdminPage.getMessageFinal().getText();
		System.out.println("Not visible "+message);
		assertTrue(message.contains("Location not changeable."));
		locationAdminPage.ensureCloseSuccessVisible();
		locationAdminPage.getCloseSuccess().click();
		
		//delete location allowed
		locationAdminPage.ensureDLAVisible();
		locationAdminPage.getDeleteLocationAllowed().click();
		locationAdminPage.ensureMessageFinalVisible();
		message=locationAdminPage.getMessageFinal().getText();
		System.out.println("visible "+message);
		assertTrue(message.contains("Location successfully deleted."));
		locationAdminPage.ensureCloseSuccessVisible();
		locationAdminPage.getCloseSuccess().click();

	}
	
	
	@After
	public void closeSelenium() {
		// Shutdown the browser
		browser.quit();
	}
	

}
