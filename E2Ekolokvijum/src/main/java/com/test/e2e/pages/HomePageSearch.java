package com.test.e2e.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePageSearch {

	private WebDriver driver;
	
	@FindBy(xpath="//*[@id=\"search_query_top\"]")
	WebElement searchInput;
	
	@FindBy(xpath="/html/body/div[1]/div[1]/header/div[3]/div/div/div[2]/form/button")
	WebElement searchButton;
	
	public HomePageSearch(WebDriver driver) {
		this.driver = driver;
	}
	
	public WebElement getSearchButton() {
		return searchButton;
	}
	
	public WebElement getSearchInput() {
		return searchInput;
	}
	
	public void setSearchInput(String searchInput) {
		WebElement el = getSearchInput();
		el.clear();
		el.sendKeys(searchInput);
	}
	
	public void ensureHomePageVisible() {
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(searchButton));
	}
	
}
