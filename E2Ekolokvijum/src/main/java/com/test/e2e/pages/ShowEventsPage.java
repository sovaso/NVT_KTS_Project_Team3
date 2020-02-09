package com.test.e2e.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.beust.jcommander.internal.Nullable;
import com.google.common.base.Predicate;

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
	
	@FindBy(xpath="/html/body/app-root/div/app-dashboard/div/app-events/div/input[1]")
	WebElement field;
	
	@FindBy(xpath="/html/body/app-root/div/app-dashboard/div/app-events/div/div[6]/ngb-alert")
	WebElement message;

	
	List<WebElement> events;
	
	@FindBy(xpath = "/html/body/app-root/div/app-dashboard/div/app-events/div/div[6]/table/tbody/tr[1]/td[1]")
	WebElement firstId;

	@FindBy(xpath="/html/body/app-root/div/app-dashboard/div/app-events/div/div[6]/table/tbody/tr[1]/td[5]/button")
	WebElement showDetailsButton;
	
	public ShowEventsPage(WebDriver driver) {
		this.driver= driver;
	}
	
	public List<WebElement> getEvents(){
		//events = driver.findElements(By.xpath("/html/body/app-root/div/app-dashboard/div/app-events/div/div[6]/table/tbody/tr/td[1]"));
		events = driver.findElements(By.xpath("/html/body/app-root/div/app-dashboard/div/app-events/div/div[6]/table/tbody/tr/td[1]"));
		System.out.println(events.size());
		return events;
	}
	
	public WebElement getMessage() {
		return message;
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
	
	public WebElement getField() {
		return field;
	}
	
	public void setField(String field) {
		WebElement el = getField();
		el.clear();
		el.sendKeys(field);
	}
	
	public void ensureTableLoaded() {
		(new WebDriverWait(driver, 30)).ignoring(StaleElementReferenceException.class).until(ExpectedConditions.visibilityOf(showDetailsButton));
		
	}
	
	public void ensureSortByDateDescLoaded() {
		(new WebDriverWait(driver, 30)).ignoring(StaleElementReferenceException.class).until(ExpectedConditions.elementToBeClickable(sortByDateDesc));
		
	}
	
	public void ensureTableFilledDesc() {
		(new WebDriverWait(driver, 30)).ignoring(StaleElementReferenceException.class).until(ExpectedConditions.textToBePresentInElement(firstId, "3"));
	}
	
	public void ensureMessageIsDisplayed() {
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOf(message));
	}
	
	public void ensureStartDateIsVisible() {
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOf(startDate));
	}
	
	public void ensureEndDateIsVisible() {
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOf(endDate));
	}
	
	public void ensureFieldIsDisplayed() {
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOf(field));
	}
	
	public void ensureSearchIsDisplayed() {
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(search));
	}
	
	public void ensureFieldEvent2() {
		(new WebDriverWait(driver, 30)).ignoring(StaleElementReferenceException.class).until(ExpectedConditions.textToBePresentInElement(firstId, "3"));
	}
	
	
	
	
}
