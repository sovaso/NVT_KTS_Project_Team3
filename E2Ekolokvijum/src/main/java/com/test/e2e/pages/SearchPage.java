package com.test.e2e.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SearchPage {

	private WebDriver driver;
	
	@FindBy(xpath="/html/body/div/div[2]/div/div[3]/div[2]/p")
	WebElement errorMessage;
	
	@FindBy(xpath="/html/body/div/div[2]/div/div[3]/div[2]/h1/span[2]")
	WebElement successMessage;
	
	@FindBy(xpath = "/html/body/div/div[2]/div/div[3]/div[2]/ul/li/div/div[2]/h5/a")
	List<WebElement> searchResultTitles;
	
	public List<WebElement> getSearchResultTitles(){
		List<WebElement> theSearchResultTitles = searchResultTitles.subList(0, 5);
		for (WebElement w : theSearchResultTitles) {
			System.out.println(w.getText());
		}
		return theSearchResultTitles;
	}
	public SearchPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public WebElement getErrorMessage() {
		return errorMessage;
	}
	
	public WebElement getSuccessMessage() {
		return successMessage;
	}
	
	public void ensureErrorMessageVisible() {
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(errorMessage));	
	}
	
	public void ensureSuccessMessageVisible() {
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(successMessage));
		
	}
}
