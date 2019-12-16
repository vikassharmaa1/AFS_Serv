package com.webdriver;

import java.util.HashMap;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

public class SeleniumFactory {

	public HashMap<String, String> dataSetMap = new HashMap<String, String>();
	public static ExtentReports extent;
	public static ThreadLocal<ExtentTest> parentTest = new ThreadLocal<>();
	public static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
	private static String url = null;
	public static SoftAssert softAssertion = new SoftAssert();

	private static Properties mobileProperties;

	private SeleniumFactory() {

	}

	private static SeleniumFactory instance = new SeleniumFactory();

	public static SeleniumFactory getInstance() {

		return instance;
	}

	ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>();

	{

	@Override
	protected WebDriver initialValue() {
		return setDriver();
	}
};

private WebDriver setDriver() {
	
	WebDriver driver = null;
	
	switch (System.getProperty("bank.selenium.host")) {
	case "LOCAL":
		
		driver = getDriverbyBrowser(System.getProperty("bank.browser.name"));
		break;
		
	case "REMOTE":
		
		
		
	case "GRID":
		
	case "MOBILE":
		
	case "PERFECTO":
		
	case "KOBITON":
		
	case "APPIUM":
		
	default:
		
		
	}
}

private WebDriver getDriverbyBrowser(String property) {
	
	return null;
}

}
