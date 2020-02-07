package com.test.e2e.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ShowEventsPage {

	private WebDriver driver;
	
	@FindBy(id="sortByName")
	WebElement sortByName;
	
	@FindBy(id="sortByDateAcs")
	WebElement sortByDateAcs;
	
	@FindBy(id="sortByDateDesc")
	WebElement sortByDateDesc;
	
	@FindBy(id="search")
	WebElement search;
	
	@FindBy(id="reset")
	WebElement reset;
	
	@FindBy(id="startDate")
	WebElement startDate;
	
	@FindBy(id="endDate")
	WebElement endDate;
	
	List<WebElement> events;
	
	@FindBy(xpath="/html/body/app-root/div/app-dashboard/div/app-events/div/div[6]/table/tbody/tr[1]/td[6]/button")
	WebElement showDetailsButton;
	
	public ShowEventsPage(WebDriver driver) {
		this.driver= driver;
	}
	
	public List<WebElement> getEvents(){
		events = driver.findElements(By.xpath("/html/body/app-root/div/app-dashboard/div/app-events/div/div[6]/table/tbody/tr/td"));
		System.out.println(events.size());
		return events;
	}
	
	public WebElement getStartDate() {
		return startDate;
	}
	
	public WebElement getEndDate() {
		return endDate;
	}
	public WebElement getSearch() {
		return search;
	}
	
	public WebElement getReset() {
		return reset;
	}
	
	public WebElement getSortByName() {
		return sortByName;
	}
	
	public WebElement getSortByDateAcs() {
		return sortByDateAcs;
	}
	
	public WebElement getSortByDateDesc() {
		return sortByDateDesc;
	}
	
	public void ensureTableLoaded() {
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(showDetailsButton));
		
	}
	
	
}
