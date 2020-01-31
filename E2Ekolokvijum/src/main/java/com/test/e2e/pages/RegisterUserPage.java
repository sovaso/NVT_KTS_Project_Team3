package com.test.e2e.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RegisterUserPage {

	private WebDriver driver;

	@FindBy(xpath = "/html/body/ngb-modal-window/div/div/app-register-user/div/div/div[2]/form/input[1]")
	WebElement name;

	@FindBy(xpath = "/html/body/ngb-modal-window/div/div/app-register-user/div/div/div[2]/form/input[2]")
	WebElement surname;

	@FindBy(xpath = "/html/body/ngb-modal-window/div/div/app-register-user/div/div/div[2]/form/input[3]")
	WebElement email;

	@FindBy(xpath = "/html/body/ngb-modal-window/div/div/app-register-user/div/div/div[2]/form/input[4]")
	WebElement username;

	@FindBy(xpath = "/html/body/ngb-modal-window/div/div/app-register-user/div/div/div[2]/form/input[5]")
	WebElement password;

	@FindBy(xpath = "/html/body/ngb-modal-window/div/div/app-register-user/div/div/div[2]/form/input[6]")
	WebElement repeatedPassword;

	@FindBy(xpath = "/html/body/ngb-modal-window/div/div/app-register-user/div/div/div[2]/form/button")
	WebElement registerButton;
	
	@FindBy(xpath="/html/body/ngb-modal-window/div/div/app-register-user/div/div/div[2]/div/ngb-alert")
	WebElement message;
	

	public WebElement getMessage() {
		return message;
	}

	public RegisterUserPage(WebDriver driver) {
		this.driver = driver;
	}

	public WebDriver getDriver() {
		return driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	public WebElement getName() {
		return name;
	}

	public void setName(String name) {
		WebElement el = getName();
		el.clear();
		el.sendKeys(name);
	}

	public WebElement getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		WebElement el = getSurname();
		el.clear();
		el.sendKeys(surname);
	}

	public WebElement getEmail() {
		return email;
	}

	public void setEmail(String email) {
		WebElement el = getEmail();
		el.clear();
		el.sendKeys(email);
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

	public WebElement getRepeatedPassword() {
		return repeatedPassword;
	}

	public void setRepeatedPassword(String repeatedPassword) {
		WebElement el = getRepeatedPassword();
		el.clear();
		el.sendKeys(repeatedPassword);
	}

	public WebElement getRegisterButton() {
		return registerButton;
	}

	public void setRegisterButton(WebElement registerButton) {
		this.registerButton = registerButton;
	}

	public void ensureRegisterVisible() {
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(registerButton));

	}
	
	public void ensureMessageVisible() {
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(message));

	}

}
