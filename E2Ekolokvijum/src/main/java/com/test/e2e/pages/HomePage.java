package com.test.e2e.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {
	
	private WebDriver driver;
	
	@FindBy(xpath="//*[@id=\"contact-link\"]/a")
	WebElement contactLink;
	
	public HomePage(WebDriver driver) {
		this.driver = driver;
	}


	public WebElement getContactLink() {
		return contactLink;
	}

	public void setContactLink(WebElement contactLink) {
		this.contactLink = contactLink;
	}

	//Zato i uzimas ovaj element contactLink-da bi proverila da je stranica vidljiva
	public void ensureHomePageVisible() {
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(contactLink));
		
	}

	

	
	

}
