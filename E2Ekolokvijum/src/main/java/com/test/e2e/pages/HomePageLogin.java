package com.test.e2e.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePageLogin {

	private WebDriver driver;
	
	@FindBy(xpath="//*[@id=\"header\"]/div[2]/div/div/nav/div[1]/a")
	WebElement loginLink;
	
	public HomePageLogin(WebDriver driver) {
		this.driver = driver;
	}
	
	public WebElement getLoginLink() {
		return loginLink;
	}
	
	public void setLoginLink(WebElement loginLink) {
		this.loginLink = loginLink;
	}
	
	public void ensureHomePageVisible() {
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(loginLink));
	}
	
}
