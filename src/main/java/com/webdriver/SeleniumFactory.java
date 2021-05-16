package com.webdriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;

import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;

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

	ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>() {

		@Override
		protected WebDriver initialValue()

		{
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

			mobileProperties = PropertiesLoader.getMobilePropertiesInstance();

			try {
				String hubname = System.getProperty("remote.hub");
				String hubport = "4444";
				url = "http://" + hubname + ":" + hubport + "/wd/hub";
				System.out.println("Remote Web Grid Url is - :" + url);
				driver = getRemoteDriverbyBrowser(System.getProperty("bank.browser.name"), url);
			} catch (MalformedURLException e) {

				e.printStackTrace();
			}
			break;

		case "GRID":

			try {
				String hubname = System.getProperty("remote.hub");
				String hubport = "4444";
				url = "http://" + hubname + ":" + hubport + "/wd/hub";
				System.out.println("Remote Web Grid Url is - :" + url);
				driver = getRemoteDriverbyBrowser(System.getProperty("bank.browser.name"), url);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			break;

		case "MOBILE":

			mobileProperties = PropertiesLoader.getMobilePropertiesInstance();
			if (System.getProperty("bank.mobile.tool.name").equalsIgnoreCase("APPIUM")) {
				driver = AppiumDriverFactory.getAppiumDriver(System.getProperty("bank.mobile.execution.platform"),
						System.getProperty("bank.mobile.device.name"), System.getProperty("bank.mobile.os.version"),
						Boolean.parseBoolean(System.getProperty("bank.mobile.install.application")),
						mobileProperties.getProperty("AppiumURL"));

			} else {

				driver = PerfectoDriverFactory.getPerfectoRemoteWebDriver(
						System.getProperty("bank.mobile.execution.platform"),
						System.getProperty("bank.mobile.device.name"), System.getProperty("bank.mobile.os.version"),
						Boolean.parseBoolean(System.getProperty("bank.mobile.install.application")),
						mobileProperties.getProperty("AppiumURL"));

			}

			break;

		case "PERFECTO":
			mobileProperties = PropertiesLoader.getMobilePropertiesInstance();
			if (System.getProperty("bank.mobile.tool.name").equalsIgnoreCase("APPIUM")) {
				driver = PerfectoDriverFactory.getPerfectoAppiumDriver(
						System.getProperty("bank.mobile.execution.platform"),
						System.getProperty("bank.mobile.device.name"), mobileProperties.getProperty("PerfectoHost"));

			} else {

				driver = PerfectoDriverFactory.getPerfectoRemoteWebDriver(
						System.getProperty("bank.mobile.execution.platform"),
						System.getProperty("bank.mobile.device.name"), mobileProperties.getProperty("PerfectoHost"));
				System.getProperty("bank.browser.name");

			}

		case "KOBITON":

			driver = null;
			break;

		case "APPIUM":

			mobileProperties = PropertiesLoader.getMobilePropertiesInstance();
			DesiredCapabilities capabilities = new DesiredCapabilities("mobileOS", "", Platform.ANY);
			String host = "bbt.perfectomobile.com";
			// capabilities.setCapability(CapabilityType.PROXY, proxy);

			capabilities.setJavascriptEnabled(true);
			String platform = "iOS";

			capabilities.setCapability("user", "rsoundararaja@bbandt.com");
			capabilities.setCapability("password", "Welcome@1");
			capabilities.setCapability("platformName", "platform");
			capabilities.setCapability("unicodeKeyboard", "true");
			capabilities.setCapability("autoWebView", "true");
			capabilities.setCapability("deviceName", "F875DE53585BAC5EC1E6C976D85889D611D08679");
			capabilities.setCapability("automationName", "Appium");
			capabilities.setCapability("nativeWebTap", "true");

			AppiumDriver<MobileElement> AppiumDriver = null;

			try {
				AppiumDriver = new IOSDriver<MobileElement>(new URL(""), capabilities);
				System.out.println("Connection to Perfecto Cloud - 'bbt.perfectomobile.com' is successfull");
			} catch (MalformedURLException e) {
				System.out.println(
						"Connection to Perfecto Cloud - 'bbt.perfectomobile.com' is failed !! -" + e.getMessage());

			}

			System.out.println("Entering Appium");
			break;

		default:

			try {
				throw new FrameworkException("Unhandled Execution Mode!");
			} catch (FrameworkException e) {
				
				e.printStackTrace();
			}

		}

		return driver;
	}

	public WebDriver getDriver() {

		try {
			driver.get().getCurrentUrl();
		} catch (Exception e) {
			removeDriver();
			driver.set(setDriver());

		}
		return driver.get();
	}

	public void removeDriver() {
		if (driver.get() != null) {

			// driver.get().close();
			driver.get().quit();
			driver.remove();
		}

	}

	private static ChromeOptions createChromeCapabilities() {

		Proxy proxy = new Proxy();
		proxy.setProxyAutoconfigUrl("http://pac.hybrid-web.global.blackspider.com/proxy.pac?p=4844vtpr");

		LoggingPreferences logPrefs = new LoggingPreferences();
		logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
		logPrefs.enable(LogType.BROWSER, Level.ALL);

		ChromeOptions options = new ChromeOptions();
		options.addArguments("--no-sandbox");
		options.addArguments("--dns-prefetch-disable");
		options.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
		// options.addArguments("start-maximized");
		options.setExperimentalOption("useAutomationExtension", false);
		if (System.getProperty("bank.browser.state").toLowerCase().contains("headless")) {
			// options.addArguments("headless");
			options.addArguments("--window-size=1280,1024");
			options.addArguments("--headless");
			options.addArguments("--disable-dev-shm-usage");
			options.setCapability(CapabilityType.PROXY, proxy);

		}

		return options;
	}

	private static FirefoxOptions createFirefoxCapabilities() {

		LoggingPreferences logPrefs = new LoggingPreferences();
		logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
		logPrefs.enable(LogType.BROWSER, Level.ALL);

		FirefoxOptions options = new FirefoxOptions();
		options.addArguments("--no-sandbox");
		options.addArguments("--dns-prefetch-disable");
		options.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);

		if (System.getProperty("bank.browser.state").toLowerCase().contains("headless")) {

			options.addArguments("-headless");

		}

		return options;

	}

	private static SafariOptions createSafariCapabilities() {

		SafariOptions options = new SafariOptions();
		return options;
	}

	private static InternetExplorerOptions createIECapabilities() {

		LoggingPreferences logPrefs = new LoggingPreferences();
		logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
		logPrefs.enable(LogType.BROWSER, Level.ALL);

		InternetExplorerOptions options = new InternetExplorerOptions();

		options.setCapability("ignoreZoomSetting", true);
		options.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		options.setCapability(InternetExplorerDriver.NATIVE_EVENTS, false);
		options.setCapability(InternetExplorerDriver.UNEXPECTED_ALERT_BEHAVIOR, "ignore");
		options.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);

		return options;
	}

	public WebDriverWait webDriverWait(WebDriver selenium, int seconds) {
		return new WebDriverWait(selenium, seconds);

	}

	private WebDriver getDriverbyBrowser(String browser) {
		if (System.getProperty("bank.browser.name") == null) {
			browser = "INTERNET_EXPLORER";

		}

		WebDriver driver = null;
		switch (browser) {
		case "CHROME":
			driver = new ChromeDriver(createChromeCapabilities());
			break;
		case "FIREFOX":
			driver = new FirefoxDriver(createFirefoxCapabilities());
			break;
		case "INTERNET_EXPLORER":
			driver = new InternetExplorerDriver(createIECapabilities());
			break;
		case "SAFARI":
			driver = new SafariDriver(createSafariCapabilities());
			break;

		default:
			try {
				throw new FrameworkException("Unhandled browser!");
			} catch (FrameworkException e) {
				
				e.printStackTrace();
			}

		}
		return driver;
	}

	private WebDriver getRemoteDriverbyBrowser(String browser, String url) throws MalformedURLException {
		if (System.getProperty("bank.browser.name") == null) {
			browser = "INTERNET_EXPLORER";

		}

		WebDriver driver = null;
		switch (browser) {
		case "CHROME":
			return new RemoteWebDriver(new URL(url), createChromeCapabilities());
		case "FIREFOX":
			return new RemoteWebDriver(new URL(url), createFirefoxCapabilities());
		case "INTERNET_EXPLORER":
			return new RemoteWebDriver(new URL(url), createIECapabilities());
		case "SAFARI":
			return new RemoteWebDriver(new URL(url), createSafariCapabilities());

		default:
			try {
				throw new FrameworkException("Unhandled browser!");
			} catch (FrameworkException e) {
				
				e.printStackTrace();
			}

		}
		return driver;

	}

}
