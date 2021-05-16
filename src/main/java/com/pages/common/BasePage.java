package com.pages.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.apache.commons.lang3.StringUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.winium.WiniumDriver;

import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;

import com.util.MessageLogger;
import com.util.TakeScreenShot;
import com.util.Util;
import com.webdriver.SeleniumFactory;

public class BasePage {

	static final int DEFAULT_TIMEOUT = System.getProperty("bank.selenium.timeout") != null
			? Integer.parseInt(System.getProperty("bank.selenium.timeout"))
			: 0;
	static final int IMPLICIT_WAIT = System.getProperty("bank.selenium.implicitWait") != null
			? Integer.parseInt(System.getProperty("bank.selenium.implicitWait"))
			: 0;

	public WebDriver selenium;
	public WiniumDriver winium;
	public ThreadLocal<ExtentTest> test = SeleniumFactory.test;
	public SoftAssert softAssertion = SeleniumFactory.softAssertion;
	WebElement webelement = null;
	String elemIdentifier = null;
	String elemIdentifierValue = null;

	public BasePage(WebDriver selenium) {

		this.selenium = selenium;
		doLog();
	}

	public BasePage() {

	}

	public BasePage(WiniumDriver winium) {
		this.winium = winium;
	}

	public void doLog() {

	}

	public WebDriverWait seleniumWait(int seconds) {

		return SeleniumFactory.getInstance().webDriverWait(selenium, seconds);
	}

	public void initialize(Object o) {

		PageFactory.initElements(selenium, o);
	}

	public <T extends BasePage> T getPage(Class<T> newPageClass) {
		T page = PageFactory.initElements(selenium, newPageClass);
		page.waitForLoad();
		page.verifyPage();

		return page;
	}

	public void verifyPage() {

	}

	public void waitForLoad() {
		seleniumGetCurrentURL();

	}

	private void seleniumGetCurrentURL() {
		

	}

	public void handleJavaScriptDateFields(WebElement element, String value) {
		if (element.isDisplayed()) {
			element.click();
			element.clear();
		}
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		element.sendKeys(value);
	}

	final public void waitForElementNotPresent(final By by, int timeout) {

		try {
			selenium.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			WebDriverWait wait = new WebDriverWait(selenium, DEFAULT_TIMEOUT);
			wait.ignoring(StaleElementReferenceException.class).ignoring(NoSuchElementException.class)
					.until(ExpectedConditions.invisibilityOfElementLocated(by));
		} catch (Exception e) {
			MessageLogger.WriteExceptionToConsole("Error Waiting on Element not to be Clickable", e.getMessage());

		} finally {

			selenium.manage().timeouts().implicitlyWait(IMPLICIT_WAIT, TimeUnit.SECONDS);
		}
	}

	public void localSleep(long time) {

		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
	}

	public boolean isPresentAndDisplayed(WebElement element) {

		try {
			selenium.getCurrentUrl();
			new WebDriverWait(selenium, 10).until(ExpectedConditions.visibilityOf(element));
			return element.isDisplayed();
		} catch (Exception e) {

			System.out.println("Element displayed condition exception");
			return false;
		}

	}

	public boolean isPresentAndDisplayed(WebElement element, int time) {

		try {
			seleniumGetCurrentURL();
			new WebDriverWait(selenium, time).until(ExpectedConditions.visibilityOf(element));
			return element.isDisplayed();
		} catch (Exception e) {

			System.out.println("Element displayed condition exception");
			return false;
		}

	}

	public void waitForElementEnabled(WebElement element) {

		try {
			seleniumWait(10).until((ExpectedCondition<Boolean>) driver -> element.isEnabled());
		} catch (Exception e) {
			System.out.println("Element Enable Condition Exception");
			localSleep(2000);
		}
	}

	public WebElement elementWaitUntilClickable(WebElement element) {

		waitForElementEnabled(element);
		if (isPresentAndDisplayed(element)) {
			return element;
		} else {
			seleniumGetCurrentURL();
			return element;
		}
	}

	public void performJSClick(WebElement element) {
		seleniumGetCurrentURL();
		JavascriptExecutor executor = (JavascriptExecutor) selenium;
		executor.executeScript("arguments[0].click();", element);
	}

	public String getCurrentFrame() {
		seleniumGetCurrentURL();
		JavascriptExecutor executor = (JavascriptExecutor) selenium;
		return (String) executor.executeScript("return self.name");
	}

	public void switchFrame(String framename) {
		String currentFrame = getCurrentFrame();
		if (!framename.equalsIgnoreCase(currentFrame)) {
			selenium.switchTo().defaultContent();
			selenium.switchTo().frame(framename);
			reportLog("info", "Frame switched from - " + currentFrame + " to - " + framename, false);
		}
	}

	public void Switchwindows(String urlortitle) {
		try {
			System.out.println("Size of windows: " + selenium.getWindowHandles().size());
			for (String handle : selenium.getWindowHandles()) {
				String windowurl = selenium.switchTo().window(handle).getCurrentUrl();
				String windowtitle = selenium.switchTo().window(handle).getTitle();
				System.out.println("url is -" + windowurl);
				System.out.println("window title" + windowtitle);
				if (windowurl.contains(urlortitle) || windowtitle.contains(urlortitle)) {

					System.out.println("URL after Switching" + selenium.getCurrentUrl());
					System.out.println("Title after Switching" + selenium.getTitle());
					break;
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public void waitForAjaxContent(final By by) {
		try {
			WebElement element = this.selenium.findElement(by);

			WebDriverWait wait = new WebDriverWait(selenium, DEFAULT_TIMEOUT);
			wait.ignoring(StaleElementReferenceException.class).ignoring(NoSuchElementException.class)
					.until(ExpectedConditions.visibilityOf(element));

			if (element.isDisplayed()) {
				for (int i = 0; i < DEFAULT_TIMEOUT; i++) {
					if (element.getText().length() > 0) {
						break;
					}
					Thread.sleep(1000);
				}
			}
		} catch (InterruptedException e) {

			MessageLogger.WriteExceptionToConsole("Error waiting for ajax element", e.toString());
		}

		new WebDriverWait(selenium, DEFAULT_TIMEOUT).until(new ExpectedCondition<Boolean>() {

			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) selenium).executeScript("return document.readyState").equals("complete");
			}
		});
	}

	public void moveToElementLocation(WebElement element) {

		((JavascriptExecutor) selenium).executeScript("arguments[0].scrollIntoView(true);", element);
	}

	public void buttonClick(final By by) {
		WebElement element = selenium.findElement(by);
		if (!System.getProperty("bank.browser.name").toLowerCase().equals("chrome")) {
			seleniumWait(DEFAULT_TIMEOUT).until(ExpectedConditions.elementToBeClickable(element));
			element.click();
		} else {

			try {
				boolean locationMatch = false;
				for (int i = 0; i < DEFAULT_TIMEOUT; i++) {
					int y = element.getLocation().y;
					int x = element.getLocation().x;

					if (locationMatch) {

						((JavascriptExecutor) selenium).executeScript("window.scrollTo(0," + y + ")");
						((JavascriptExecutor) selenium).executeScript("window.scrollTo(0," + x + ")");
						element.click();
						break;

					}

					if (y == selenium.findElement(by).getLocation().y && x == selenium.findElement(by).getLocation().x)

					{

						locationMatch = true;
					} else {
						element = selenium.findElement(by);
					}

				}
			} catch (WebDriverException e) {

				if (e.getMessage().contains("not clickable at point")) {
					element = selenium.findElement(by);
					element.click();
				}
			} catch (Exception e) {
				MessageLogger.WriteExceptionToConsole("Error trying to click element.", e.toString());
			}

		}
	}

	/*public void getNetworkLogs(Map<String, String> testData) {
		for (LogEntry entry : selenium.manage().logs().get(LogType.PERFORMANCE).getAll()) {

			JsonPath jp = new JsonPath(entry.toJson().get("message").toString());
			if (jp.get("message.method").toString().equalsIgnoreCase("Network.requestWillBeSent")) {
				if (jp.get("message.params.request.url").toString().contains("https://sstats.bbt.com/")) {
					String waUrl = jp.get("message.params.request.url").toString();
					Util.getQueryParameterForGivenURL(testData, waUrl);
				}
			}
		}
	}*/

	public List<LogEntry> getBrowserConsoleLogs() {
		List<LogEntry> returnLogEntry = new ArrayList<LogEntry>();
		//@SuppressWarnings("deprecation")
		List<LogEntry> entry = selenium.manage().logs().get(LogType.BROWSER).filter(Level.SEVERE);
		for (LogEntry logEntry : entry) {
			returnLogEntry.add(logEntry);
		}
		return returnLogEntry;
	}

	public void reportLog(String status, String description, boolean screenshot) {

		try {
			String callingMethod = Thread.currentThread().getStackTrace()[2].getMethodName();
			if (screenshot) {
				String screenshotPath = null;

				try {
					screenshotPath = TakeScreenShot.capture(selenium, callingMethod);
				} catch (IOException e) {

					e.printStackTrace();
				}

				screenshotPath = "./" + screenshotPath.substring(screenshotPath.indexOf("screenshots"));
				if (StringUtils.isNotBlank(System.getProperty("bank.alm"))) {
					switch (status.toLowerCase()) {
					case "info":
						test.get().log(Status.INFO,
								description + "; Refer the below screenshot" + "<br>" + screenshotPath);
						break;
					case "pass":
						test.get().log(Status.PASS,
								description + "; Refer the below screenshot" + "<br>" + screenshotPath);
						break;
					case "fail":
						test.get().log(Status.FAIL,
								description + "; Refer the below screenshot" + "<br>" + screenshotPath);
						break;
					case "warning":
						test.get().log(Status.WARNING,
								description + "; Refer the below screenshot" + "<br>" + screenshotPath);
						break;
					default:
						test.get().log(Status.INFO,
								description + "; Refer the below screenshot" + "<br>" + screenshotPath);
						break;
					}

				} else {
					switch (status.toLowerCase()) {
					case "info":
						test.get().log(Status.INFO, description,
								MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
						break;
					case "pass":
						test.get().log(Status.PASS, description,
								MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
						break;
					case "fail":
						test.get().log(Status.FAIL, description,
								MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
						break;
					case "warning":
						test.get().log(Status.WARNING, description,
								MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
						break;
					default:
						test.get().log(Status.INFO, description,
								MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
						break;
					}

				}
			} else {

				switch (status.toLowerCase()) {
				case "info":
					test.get().log(Status.INFO, description);
					break;
				case "pass":
					test.get().log(Status.PASS, description);
					break;
				case "fail":
					test.get().log(Status.FAIL, description);
					break;
				case "warning":
					test.get().log(Status.WARNING, description);
					break;
				default:
					test.get().log(Status.INFO, description);
					break;
				}

			}
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

}
