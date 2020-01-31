package com.test.e2e.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SortPage {
	
	private WebDriver driver;

	public SortPage(WebDriver driver) {
		super();
		this.driver = driver;
	}
	
	public ArrayList<Double> getSorted() {
		ArrayList<Double> sorted=new ArrayList<>();
		List<WebElement> webElems=driver.findElements(By.xpath("/html/body/div/div[2]/div/div[3]/div[2]/ul/li/div/div[2]/div[1]/span[1]"));
				
		System.out.println(webElems.size());
		for(WebElement e: webElems) {
			sorted.add(Double.parseDouble(e.getText().substring(1)));
		}
		return sorted;
		
	}

}
