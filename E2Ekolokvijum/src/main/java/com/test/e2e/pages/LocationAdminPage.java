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
	
	@FindBy(xpath="/html/body/ngb-modal-window/div/div/app-location-create/div/div[1]/input[1]")
	WebElement newName;
	
	@FindBy(xpath="/html/body/ngb-modal-window/div/div/app-location-create/div/div[1]/input[3]")
	WebElement newDescription;
	
	@FindBy(xpath="/html/body/ngb-modal-window/div/div/app-location-create/div/div[1]/input[2]")
	WebElement newAddress;
	
	@FindBy(xpath="/html/body/ngb-modal-window/div/div/app-location-create/div/div[1]/button[3]")
	WebElement createLocationButton;
	
	@FindBy(xpath="/html/body/ngb-modal-window/div/div/app-location-create/div/div[1]/button[1]")
	WebElement addNewLocationZoneButton;
	
	
	@FindBy(xpath="/html/body/ngb-modal-window[2]/div/div/app-alert-box/div/div/div[2]/form")
	WebElement message;
	
	@FindBy(xpath="/html/body/ngb-modal-window/div/div/app-alert-box/div/div/div[2]/form")
	WebElement messageFinal;
	
	@FindBy(xpath="/html/body/ngb-modal-window[2]/div/div/app-alert-box/div/div/div[2]/form/button")
	WebElement closeMessage;
	
	
	@FindBy(xpath="/html/body/ngb-modal-window/div/div/app-location-create/div/div[1]/table/tr[1]/td/input")
	WebElement locationZoneName;
	
	@FindBy(xpath="/html/body/ngb-modal-window/div/div/app-location-create/div/div[1]/table/tr[3]/td/input")
	WebElement locationZoneCol;
	
	@FindBy(xpath="/html/body/ngb-modal-window/div/div/app-location-create/div/div[1]/table/tr[2]/td/input")
	WebElement locationZoneRow;
	
	@FindBy(xpath="/html/body/ngb-modal-window/div/div/app-location-create/div/div[1]/table/tr[6]/button")
	WebElement addLZFinalButton;
	
	@FindBy(xpath="/html/body/ngb-modal-window/div/div/app-alert-box/div/div/div[2]/form/button")
	WebElement closeSuccess;
	
	//8
	@FindBy(xpath="/html/body/app-root/div/app-dashboard/div/app-locations/div/div/table/tbody/tr[8]/ngb-tab/td[1]/button")
	WebElement deleteLocationAllowed;
	
	@FindBy(xpath="/html/body/app-root/div/app-dashboard/div/app-locations/div/div/table/tbody/tr[1]/ngb-tab/td[1]/button")
	WebElement deleteLocationNotAllowed;
	

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
	

	public WebElement getNewName() {
		return newName;
	}

	public void setNewName(String newName) {
		WebElement e=getNewName();
		e.clear();
		e.sendKeys(newName);
	}

	public WebElement getNewDescription() {
		return newDescription;
	}

	public void setNewDescription(String newDescription) {
		WebElement e=getNewDescription();
		e.clear();
		e.sendKeys(newDescription);
	}

	public WebElement getNewAddress() {
		return newAddress;
	}

	public void setNewAddress(String newAddress) {
		WebElement e=getNewAddress();
		e.clear();
		e.sendKeys(newAddress);
	}

	public WebElement getLocations() {
		return locations;
	}

	public void setLocations(WebElement locations) {
		this.locations = locations;
	}

	public WebElement getCreateLocationButton() {
		return createLocationButton;
	}

	public WebElement getAddNewLocationZoneButton() {
		return addNewLocationZoneButton;
	}
	
	public WebElement getMessage() {
		return message;
	}

	public void ensureMessageVisible() {
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(message));

	}
	
	public void ensureMessageFinalVisible() {
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(messageFinal));

	}
	
	public WebElement getMessageFinal() {
		return messageFinal;
	}

	public void ensureCloseButtonVisible() {
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(closeMessage));

	}
	
	public void ensureAddFinalLZButtonVisible() {
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(addLZFinalButton));

	}
	
	public void ensureAddLZVisible() {
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(addNewLocationZoneButton));

	}
	
	public void ensureCloseSuccessVisible() {
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(closeSuccess));

	}
	
	public void ensureDLAVisible() {
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(deleteLocationAllowed));

	}
	
	public void ensureDLNAVisible() {
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(deleteLocationNotAllowed));

	}
	
	
	public WebElement getDeleteLocationAllowed() {
		return deleteLocationAllowed;
	}

	public WebElement getDeleteLocationNotAllowed() {
		return deleteLocationNotAllowed;
	}

	public WebElement getCloseSuccess() {
		return closeSuccess;
	}

	public WebElement getLocationZoneName() {
		return locationZoneName;
	}

	public void setLocationZoneName(String locationZoneName) {
		WebElement el=getLocationZoneName();
		el.clear();
		el.sendKeys(locationZoneName);
	}

	public WebElement getLocationZoneCol() {
		return locationZoneCol;
	}

	public void setLocationZoneCol(String locationZoneCol) {
		WebElement el=getLocationZoneCol();
		el.clear();
		el.sendKeys(locationZoneCol);
	}

	public WebElement getLocationZoneRow() {
		return locationZoneRow;
	}

	public void setLocationZoneRow(String locationZoneRow) {
		WebElement el=getLocationZoneRow();
		el.clear();
		el.sendKeys(locationZoneRow);
	}

	public WebElement getCloseMessage() {
		return closeMessage;
	}

	public WebElement getAddLZFinalButton() {
		return addLZFinalButton;
	}
	
	
	

}
