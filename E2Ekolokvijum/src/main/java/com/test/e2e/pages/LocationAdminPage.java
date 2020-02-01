package com.test.e2e.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LocationAdminPage {
	
	private WebDriver driver;

	@FindBy(xpath = "/html/body/app-root/div/app-dashboard/div/app-menu-bar/div/ul/li[2]/a")
	WebElement locations;

	public LocationAdminPage(WebDriver driver) {
		this.driver = driver;
	}

	public WebDriver getDriver() {
		return driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}


	public void ensureRegisterVisible() {
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(locations));

	}

	public WebElement getLocations() {
		return locations;
	}

	public void setLocations(WebElement locations) {
		this.locations = locations;
	}
	
	

}
