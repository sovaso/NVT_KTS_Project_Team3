package com.test.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SendEmail {
	private WebDriver driver;
	
	Select subject;
	
	@FindBy(xpath="//*[@id=\"email\"]")
	WebElement email;
	
	@FindBy(xpath="//*[@id=\"id_order\"]")
	WebElement other_reference;
	
	@FindBy(xpath="//*[@id=\"message\"]")
	WebElement message;
	
	@FindBy(xpath="//*[@id=\"submitMessage\"]")
	WebElement submitMessage;//proverno da li je ucitano - ako ono jeste onda su i ostali elementi
	
	@FindBy(xpath="//*[@id='center_column']/div/ol/li")
	WebElement errorMessage; //provereno da li je ucitano
	
	@FindBy(xpath="//*[@id=\"center_column\"]/p")
	WebElement success; //provereno da li je ucitano
	
	@FindBy(xpath="//*[@id=\"center_column\"]/ul/li/a")
	WebElement homeButton; //provereno da li je ucitano

	public WebElement getSuccess() {
		return success;
	}

	public WebElement getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(WebElement errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Select getSubject() {
		subject=new Select(driver.findElement(By.xpath("//*[@id=\"id_contact\"]")));
		return subject;
	}

	public void setSubject(int subject) {
		Select el = getSubject();
		el.selectByIndex(subject);
	}

	public WebElement getEmail() {
		return email;
	}

	public void setEmail(String email) {
		WebElement el = getEmail();
		el.clear();
		el.sendKeys(email);
	}

	public WebElement getOther_reference() {
		return other_reference;
	}

	public void setOther_reference(String other_reference) {
		WebElement el = getOther_reference();
		el.clear();
		el.sendKeys(other_reference);
	}

	public WebElement getMessage() {
		return message;
	}

	public void setMessage(String message) {
		WebElement el = getMessage();
		el.clear();
		el.sendKeys(message);
	}
	
	public WebElement getHomeButton() {
		return homeButton;
	}

	public void ensureSubmitButtonIsDisplayed() {
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(submitMessage));
	}


	public SendEmail(WebDriver driver) {
		super();
		this.driver = driver;
	}

	public WebElement getSubmitMessage() {
		return submitMessage;
	}
	
	public void ensureDivIsDisplayed() {
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOf(errorMessage));
	}
	
	public void ensureSuccessIsDisplayed() {
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOf(success));
	}
	
	public void ensureHomeButtonIsDisplayed() {
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(homeButton));
	}

}
