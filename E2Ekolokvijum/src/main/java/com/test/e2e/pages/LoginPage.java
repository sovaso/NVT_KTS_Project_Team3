package com.test.e2e.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.server.handler.SendKeys;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

	private WebDriver driver;
	
	@FindBy(id="username")
	WebElement username;

	@FindBy(id="password")
	WebElement password;
	
	@FindBy(xpath = "/html/body/ngb-modal-window/div/div/app-login/div/div/div[2]/form/button")
	WebElement button;
	
	@FindBy(xpath="/html/body/ngb-modal-window/div/div/app-login/div/div/div[2]/div/ngb-alert")
	WebElement message;
	
	public LoginPage(WebDriver driver) {
		this.driver= driver;
	}
	
	public WebElement getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		WebElement el = getUsername();
		el.clear();
		el.sendKeys(username);
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
	
	public WebElement getMessage() {
		return message;
	}
	
	public void ensureButtonVisible() {
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(button));
		
	}
	
	
	
}
