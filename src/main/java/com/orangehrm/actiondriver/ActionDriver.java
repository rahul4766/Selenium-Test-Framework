package com.orangehrm.actiondriver;

import java.time.Duration;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class ActionDriver {

	private WebDriver driver;
	private WebDriverWait wait;
	public static final Logger logger = BaseClass.logger;

	public ActionDriver(WebDriver driver) {
		this.driver = driver;
		int explicitWait = Integer.parseInt(BaseClass.getProp().getProperty("explicitWait"));
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
		logger.info("WebDriver instance is created");
	}

	// Method to click an element
	public void click(By Locator) {
		String elementDescription = getElementDescription(Locator);
		try {
			waitForElementToBeClickable(Locator);
			driver.findElement(Locator).click();
			ExtentManager.logStep("clicked an element:"+elementDescription);
			logger.info("clicked an element--->"+elementDescription);
		} catch (Exception e) {
			System.out.println("Unable to click Element" + e.getMessage());
			ExtentManager.logFailure(BaseClass.getDriver(),"Unable to click an element:" ,elementDescription + "unable to click" );
			logger.error("Unable to click Element");

		}
	}

	// Method to enter text into an input field

	public void enterText(By Locator, String value) {

		try {
			waitForElementToBeVisible(Locator);
			// driver.findElement(Locator).clear();
			// driver.findElement(Locator).sendKeys(value);
			WebElement element = driver.findElement(Locator);
			element.clear();
			element.sendKeys(value);
			logger.info("Entered text on:"+getElementDescription(Locator) + "--->"+value);
		} catch (Exception e) {
			logger.error("Unable to enter the value" + e.getMessage());
		}

	}

	// Method to get text from an input field
	public String getText(By Locator) {
		try {
			waitForElementToBeVisible(Locator);
			return driver.findElement(Locator).getText();
		} catch (Exception e) {
			logger.error("Unable to get the text" + e.getMessage());
		}
		return "";

	}

	// Method to compare Two Text
	public boolean compareText(By Locator, String expectedText) {
		try {
			waitForElementToBeVisible(Locator);
			String actualText = driver.findElement(Locator).getText();
			if (expectedText.equals(actualText)) {
				logger.info("Texts are matching" + actualText + " equals " + expectedText);
				ExtentManager.logStepWithScreenshot(BaseClass.getDriver(),"Compare Text","Text Verified Successfully! " +actualText+ " equals "+expectedText);
				return true;
			} else {
				logger.error("Texts are not matching" + actualText + " not equals " + expectedText);
				ExtentManager.logFailure(BaseClass.getDriver(),"Text Comparison Failed!","Text Comparison Failed " +actualText+ " not equals "+expectedText);
				return false;
			}
		} catch (Exception e) {
			logger.error("Unable to compare text" + e.getMessage());
		}
		return false;

	}

	// Method to check if an element is Displayed
	public boolean isDisplayed(By Locator) {
		try {
			waitForElementToBeVisible(Locator);
			logger.info("Element is displayed "+getElementDescription(Locator));
			ExtentManager.logStep("Element is displayed: " +getElementDescription(Locator));
			ExtentManager.logStepWithScreenshot(BaseClass.getDriver(),"Element is displayed:","Element is displayed"+getElementDescription(Locator));
			return driver.findElement(Locator).isDisplayed();
		
		} catch (Exception e) {
			logger.error("Element is not displayed" + e.getMessage());
			ExtentManager.logFailure(BaseClass.getDriver(),"Element is not Displayed:" ,"Element is not Displayed"+ getElementDescription(Locator) );
			return false;
		}

	}

	// wait for the page to load
	public void waitForPageLoad(int timeOutInSec) {
		try {
			wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(webDriver -> ((JavascriptExecutor) webDriver)
					.executeScript("return document.readyState").equals("complete"));
			logger.info("Page loaded successfully.");
		} catch (Exception e) {
			logger.error("page did not load within " + timeOutInSec + " seconds. Exception: " + e.getMessage());
		}
	}

	// Scroll to an element
	public void scrollToElement(By Locator) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement element = driver.findElement(Locator);
			js.executeScript("arguments[0].scrollIntoView(true);", element);
		} catch (Exception e) {
			logger.error("Unable to locate element" + e.getMessage());
		}
	}

	// Wait for the Element to be Clickable
	public void waitForElementToBeClickable(By Locator) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(Locator));
		} catch (Exception e) {
			logger.error("Element is not Clickable" + e.getMessage());
		}

	}

	// Wait for Element to be visible
	/*public void waitForElementToBeVisible(By Locator) {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(Locator));
		} catch (Exception e) {
			logger.error("Element is Not Visible" + e.getMessage());
		}
	} */

	public void waitForElementToBeVisible(By Locator) {
	    try {
	        WebElement element = driver.findElement(Locator);
	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
	        wait.until(ExpectedConditions.visibilityOf(element));
	    } catch (Exception e) {
	        logger.error("Element is Not Visible " + e.getMessage());
	    }
	}

	// Method to get the description of an element using By Locator
	public String getElementDescription(By Locator) {
		// Check for null driver or locator to avoid NullPointer Exception
		if (driver == null)
			return "driver is null";
		if (Locator == null)
			return "Locator is null";

		try {
			//find the element using the Locator
			WebElement element = driver.findElement(Locator);
			
			//Get Element Attributes
			String name =  element.getDomAttribute("name");
			String id = element.getDomAttribute("id");
			String text = element.getText();
			String className = element.getDomAttribute("class");
			String placeHolder = element.getDomAttribute("placeHolder");
			
			
//Return the description based on element attributes
			if(isNotEmpty(name)) {
				return "Element with name:" +name;
			}
			else if(isNotEmpty(id)) {
				return "Element with id:" +id;
			}
			else if(isNotEmpty(text)) {
				return "Element with text:" +truncate(text,50);
			}
			else if(isNotEmpty(className)) {
				return "Element with className:" +className;
			}
			else if(isNotEmpty(placeHolder)) {
				return "Element with placeHolder"+placeHolder;
			}
		} catch (Exception e) {
		logger.error("unable to describe the element" +e.getMessage());
		}
		return  "unable to describe the element";
		
		
	}
	
	//utility method to check a string is not NULL or Empty
    private boolean isNotEmpty(String value) {
    	return value!= null && !value.isEmpty();
    	
    }
    
    // Utility Method to truncate long String
    private String truncate(String value, int maxLength) {
    	if(value==null || value.length()<=maxLength) {
    		return value;
    	}
    	return value.substring(0,maxLength)+"...";
    }
}
