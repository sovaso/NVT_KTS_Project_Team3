package com.test.e2e.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.server.handler.SendKeys;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

	private WebDriver driver;
	
	@FindBy(xpath="/html/body/div/div[2]/div/div[3]/div/div/div[2]/form/div/div[1]/input")
	WebElement email;

	@FindBy(xpath="/html/body/div/div[2]/div/div[3]/div/div/div[2]/form/div/div[2]/span/input")
	WebElement password;
	
	@FindBy(xpath = "/html/body/div/div[2]/div/div[3]/div/div/div[2]/form/div/p[2]/button")
	WebElement button;
	
	@FindBy(xpath="/html/body/div/div[2]/div/div[3]/div/div[1]/ol/li")
	WebElement error;
	
	public LoginPage(WebDriver driver) {
		this.driver= driver;
	}
	
	public WebElement getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		WebElement el = getEmail();
		el.clear();
		el.sendKeys(email);
	}
	
	public WebElement getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		WebElement el = getPassword();
		el.clear();
		el.sendKeys(password);
	}
	
	public WebElement getButton() {
		return button;
	}
	
	public WebElement getError() {
		return error;
	}
	
	public void ensureButtonVisible() {
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(button));
		
	}
	
	
	
}
