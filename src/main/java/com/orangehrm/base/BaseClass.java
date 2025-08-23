package com.orangehrm.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.asserts.SoftAssert;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.LoggerManager;

public class BaseClass {

	protected static Properties prop;
	// protected static WebDriver driver;
	// private static ActionDriver actionDriver;

	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();
	public static final Logger logger = LoggerManager.getLogger(BaseClass.class);
	protected ThreadLocal<SoftAssert> softAssert = ThreadLocal.withInitial(SoftAssert::new);
	private boolean isHeadless = false;

	// Getter method for soft assert
	public SoftAssert getSoftAssert() {
		return softAssert.get();
	}

	@BeforeSuite
	public void loadConfig() throws IOException {
		// Load the configuration file
		prop = new Properties();
		FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "/src/main/resources/config.properties");
		prop.load(fis);
		logger.info("config.properties file loaded");

		// Start the Extent Report
		// ExtentManager.getReporter(); --This has been implemented in TestListener
	}

	@BeforeMethod
	public synchronized void setup() {
		launchBrowser();
		configureBrowser();
		staticWait(2);
		logger.info("WebDriverInitialized and Browser Maximized");
		logger.trace("This is a Trace message");
		logger.error("This is a Error message");
		logger.debug("This is a Debug message");
		logger.fatal("This is a Fatal message");
		logger.warn("This is a warn message");

		// Initialize the actionDriver only once
		/*
		 * if(actionDriver == null) { actionDriver = new ActionDriver(driver);
		 * logger.info("ActionDriver instance is created"); }
		 */

		// Initialize ActionDriver for the current Thread
		actionDriver.set(new ActionDriver(getDriver()));
		logger.info("ActionDriver initialized for thread:" + Thread.currentThread().getId());
	}

	private synchronized void launchBrowser() {
		// Initialize the WebDriver based on browser defined in config.properties file
		String browser = prop.getProperty("browser");
		boolean headlessConfig = Boolean.parseBoolean(prop.getProperty("headless", "false")); // read from config

		if (browser.equalsIgnoreCase("chrome")) {

			if (headlessConfig) {
				// Create ChromeOptions
				ChromeOptions options = new ChromeOptions();
				options.addArguments("--headless"); // Run Chrome in headless mode
				options.addArguments("--disable-gpu"); // Disable GPU for headless mode
				options.addArguments("--window-size=1920,1080"); // Set window size

				options.addArguments("--disable-notifications"); // Disable browser notifications
				options.addArguments("--no-sandbox"); // Required for some CI environments like Jenkins
				options.addArguments("--disable-dev-shm-usage"); // Resolve issues in resource-limited environments
				isHeadless = true;
				driver.set(new ChromeDriver(options));
			} else {
				isHeadless = false;
				driver.set(new ChromeDriver());
			}

			// driver = new ChromeDriver();
			// driver.set(new ChromeDriver()); // New Changes as per Thread
			// getDriver().manage().window().setSize(new Dimension(1920, 1080));

			ExtentManager.registerDriver(getDriver());
			logger.info("ChromeDriver Instance is created");
		} else if (browser.equalsIgnoreCase("Firefox")) {
			if (headlessConfig) {
				// Create FirefoxOptions
				FirefoxOptions options = new FirefoxOptions();
				options.addArguments("--headless"); // Run Firefox in headless mode
				options.addArguments("--disable-gpu"); // Disable GPU for headless mode
				options.addArguments("--width=1920"); // Set browser width
				options.addArguments("--height=1080"); // Set browser height
				options.addArguments("--disable-notifications"); // Disable browser notifications
				options.addArguments("--no-sandbox"); // Required for some CI/CD environment
				options.addArguments("--disable-dev-shm-usage"); // Resolve issues in resources
				isHeadless = true;
				driver.set(new FirefoxDriver(options));
			} else {
				isHeadless = false;
				driver.set(new FirefoxDriver());
			}
			// driver = new FirefoxDriver();
			// driver.set(new FirefoxDriver()); // New Changes as per Thread
			ExtentManager.registerDriver(getDriver());
			logger.info("FirefoxDriver Instance is created");
		} else if (browser.equalsIgnoreCase("edge")) {
			if (headlessConfig) {

				EdgeOptions options = new EdgeOptions();
				options.addArguments("--headless"); // Run Chrome in headless mode
				options.addArguments("--disable-gpu"); // Disable GPU for headless mode
				options.addArguments("--start-maximized");

				options.addArguments("--window-size=2560,1440"); // Set Window Size
				options.addArguments("--disable-notifications"); // Disable browser notifications
				options.addArguments("--no-sandbox"); // Required for some CI/CD environment
				options.addArguments("--disable-dev-shm-usage"); // Resolve issues in resources
				isHeadless = true;
				driver.set(new EdgeDriver(options));
			} else {
				isHeadless = false;
				driver.set(new EdgeDriver());
			}
			// driver = new EdgeDriver();
			// driver.set(new EdgeDriver()); // New Changes as per Thread
			ExtentManager.registerDriver(getDriver());
			logger.info("EdgeDriver Instance is created");
		} else {
			throw new IllegalArgumentException("Browser Not Supported:" + browser);
		}

	}

	/*
	 * Initialize the WebDriver based on browser defined in config.properties file
	 */
	private void configureBrowser() {
		// Implicit Wait
		int ImplicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(ImplicitWait));

		// maximize the browser
		// getDriver().manage().window().maximize();

		if (!isHeadless) {
			getDriver().manage().window().maximize();
		}

		// Navigate to URL
		try {
			getDriver().get(prop.getProperty("url"));
			waitForPageLoad();

		} catch (Exception e) {
			System.out.println("Failed to navigate to url" + e.getMessage());
		}

	}

	@AfterMethod
	public synchronized void tearDown() {
		if (getDriver() != null) {
			try {
				getDriver().quit();
			} catch (Exception e) {
				System.out.println("Failed to quit the driver" + e.getMessage());
			}
		}
		logger.info("WebDriver instance is closed");
		driver.remove();
		actionDriver.remove();
		// driver = null;
		// actionDriver = null;
		// ExtentManager.endTest(); --This has been implemented in TestListener

	}

	/*
	 * //Driver getter method public WebDriver getDriver() { return driver; }
	 * 
	 */

	// Getter method for prop
	public static Properties getProp() {
		return prop;
	}

	// Driver setter method
	public void setDriver(ThreadLocal<WebDriver> driver) {
		this.driver = driver;
	}

	// Getter Method for WebDriver
	public static WebDriver getDriver() {
		if (driver.get() == null) {
			System.out.println("WebDriver is not initialized");
			throw new IllegalStateException("WebDriver is not initialized");
		}
		return driver.get();
	}

	// Getter Method for ActionDriver
	public static ActionDriver getActionDriver() {
		if (actionDriver.get() == null) {
			System.out.println("ActionDriver is not initialized");
			throw new IllegalStateException("ActionDriver is not initialized");
		}
		return actionDriver.get();
	}

	public void staticWait(int seconds) {
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
	}

	public void waitForPageLoad() {
		new WebDriverWait(getDriver(), Duration.ofSeconds(30))
				.until((ExpectedCondition<Boolean>) wd -> ((JavascriptExecutor) wd)
						.executeScript("return document.readyState").equals("complete"));
	}
}