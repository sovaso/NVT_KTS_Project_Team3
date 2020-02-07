package com.test.e2e.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {
	
	private WebDriver driver;
	
	@FindBy(id="loginButton")
	WebElement loginButton;
	
	@FindBy(id="registerButton")
	WebElement registerButton;
	
	@FindBy(id="eventsLink")
	WebElement eventsLink;
	
	@FindBy(xpath = "/html/body/app-root/div/app-dashboard/div/app-header/div/div[3]/div/a")
	WebElement userButton;
	
	
	public HomePage(WebDriver driver) {
		this.driver = driver;
	}


	public WebElement getLoginButton() {
		return loginButton;
	}

	public void setLoginButton(WebElement loginButton) {
		this.loginButton = loginButton;
	}
	
	public WebElement getRegisterButton() {
		return registerButton;
	}

	public void setRegisterButton(WebElement registerButton) {
		this.registerButton = registerButton;
	}
	
	public WebElement getEventsLink() {
		return eventsLink;
	}

	public WebElement getUserButton() {
		return userButton;
	}


	//Zato i uzimas ovaj element contactLink-da bi proverila da je stranica vidljiva
	public void ensureHomePageVisible() {
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(loginButton));
		
	}
	
	public void ensureRegisterButtonVisible() {
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(registerButton));
		
	}
	
	public void ensureUserButtonVisible() {
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(userButton));
		
	}
	
	
}
