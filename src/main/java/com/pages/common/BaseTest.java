package com.pages.common;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.Assert;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.plexus.util.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.util.ExcelDataExtractor;
import com.util.ReportManager;
import com.util.MessageLogger;
import com.util.TakeScreenShot;
import com.webdriver.PropertiesLoader;
import com.webdriver.SeleniumFactory;

public class BaseTest {

	public ExtentTest parent;
	static String RELATIVE_FILE_PATH = "src/test/resources/TestData/";
	public SoftAssert softAssertion = SeleniumFactory.softAssertion;

	public WebDriver selenium() {

		return SeleniumFactory.getInstance().getDriver();
	}

	public WebDriverWait seleniumWait(int seconds) {

		return SeleniumFactory.getInstance().webDriverWait(selenium(), seconds);
	}

	@BeforeSuite(alwaysRun = true)
	public void beforeSuite() {
		if (System.getProperty("os.name").toLowerCase().contains("win")) {

			try {
				Runtime.getRuntime()
						.exec("taskkill /IM IEDriverServer.exe /IM chromedriver.exe /IM geckodriver.exe /f");
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		SeleniumFactory.extent = ReportManager.createInstance();
	}

	@BeforeClass(alwaysRun = true)
	final public void oneTimePropertiesLoad() {

		parent = SeleniumFactory.extent.createTest(getClass().getSimpleName());
		SeleniumFactory.parentTest.set(parent);
		if (System.getProperty("bank.properties.load") == null) {

			PropertiesLoader.loadProperties();

			SeleniumFactory.extent.setSystemInfo("Browser: ", System.getProperty("bank.browser.name"));

			if (StringUtils.isNotBlank(System.getProperty("bank.browser.state"))) {

				SeleniumFactory.extent.setSystemInfo("Browser State: ", System.getProperty("bank.browser.state"));
			}

			if (StringUtils.equalsIgnoreCase(System.getProperty("bank.browser.responsive"), "true")) {

				SeleniumFactory.extent.setSystemInfo("Browser Responsive: ",
						System.getProperty("bank.browser.responsive"));
				SeleniumFactory.extent.setSystemInfo("Browser Width: ", System.getProperty("bank.browser.width"));
			}

		}
	}

	@BeforeMethod(alwaysRun = true)
	public void oneTimeBaseSetUp(ITestContext context, Method m) {

		SeleniumFactory.parentTest.set(parent);
		ExtentTest child = SeleniumFactory.parentTest.get().createNode(m.getName());
		SeleniumFactory.test.set(child);
		SeleniumFactory.test.get().log(Status.INFO, m.getName() + " is initiated");

		try {
			if (context.getCurrentXmlTest().getParameter("browser") != null) {
				System.getProperty("bank.browser.name", context.getCurrentXmlTest().getParameter("browser"));

			}

			if (!(System.getProperty("bank.selenium.host").equalsIgnoreCase("bmobile"))) {
				selenium().manage().timeouts().implicitlyWait(
						Integer.parseInt(System.getProperty("bank.selenium.implicitlyWait")), TimeUnit.SECONDS);

				if (System.getProperty("bank.selenium.debug").equals("true")) {

					if (!(System.getProperty("bank.browser.responsive").toLowerCase().equalsIgnoreCase("true"))) {
						selenium().manage().window().maximize();

					} else {
						int width = Integer.parseInt(System.getProperty("bank.browser.width"));
						int height = Integer.parseInt(System.getProperty("bank.browser.height"));
						selenium().manage().window().setSize(new Dimension(width, height));
					}
				}

				if (StringUtils.isNotBlank(System.getProperty("bank.launch.url"))) {
					selenium().get(System.getProperty("bank.launch.url"));
				}
			}
		} catch (Exception exception) {
			MessageLogger.WriteExceptionToConsole("Failed test one time setup.", exception.toString());
		}

	}

	@AfterMethod(alwaysRun = true)
	final public void oneTimeBaseTearDown(ITestResult result) throws Exception {
		String screenshotPath = null;
		if (result.getStatus() == ITestResult.FAILURE) {

			try {
				screenshotPath = TakeScreenShot.capture(selenium(), result.getName());
			} catch (Exception e) {

				e.printStackTrace();
			}
			SeleniumFactory.test.get().fail(result.getThrowable());
			screenshotPath = "./" + screenshotPath.substring(screenshotPath.indexOf("screenshots"));
			System.out.println("Screenshot location is - " + screenshotPath);
			Assert.fail("Attached Snapshot for the failure"
					+ SeleniumFactory.test.get().addScreenCaptureFromPath(screenshotPath));
		} else if (result.getStatus() == ITestResult.SKIP) {
			SeleniumFactory.test.get().skip(result.getThrowable());
		} else {
			SeleniumFactory.test.get().pass("Test passed");
		}
		SeleniumFactory.extent.flush();
		if (!(System.getProperty("bank.selenium.host").equalsIgnoreCase("bmobile"))) {
			SeleniumFactory.getInstance().removeDriver();
		}
	}

	@AfterSuite(alwaysRun = true)
	final public void testSuiteCleanup() throws IOException {
		if (System.getProperty("os.name").toLowerCase().contains("win")) {
			String property = "java.io.tmpdir";
			String tempDir = System.getProperty(property);

			try {
				FileUtils.deleteDirectory(new File(tempDir));
			} catch (IOException e) {

			}

		}
	}

	@DataProvider(name ="testData")
	public Object[][] dataProviderExcel(Method m) throws IOException {

		FileInputStream fs = new FileInputStream(new File(getTestDataFileName()));
		XSSFWorkbook wb = new XSSFWorkbook(fs);

		XSSFSheet sh = wb.getSheet(this.getClass().getSimpleName());
		if (sh == null) {
			sh = wb.getSheetAt(0);
		}

		return ExcelDataExtractor.getExcelData(sh, m.getName(), wb.getNumberOfSheets(), wb);
	}

	private String getTestDataFileName() {
		String actualFileName ="";
		if (StringUtils.isNotBlank(System.getProperty("bank.environment"))) {

			RELATIVE_FILE_PATH = "src/test/resources/TestData/";
			RELATIVE_FILE_PATH = RELATIVE_FILE_PATH + System.getProperty("bank.environment") + "/";

		}

		File file = new File(RELATIVE_FILE_PATH);
		String[] files = file.list();

		try {
			if (files.length == 0) {

			}
		} catch (Exception e) {
			RELATIVE_FILE_PATH = "src/test/resources/TestData/";
			file = new File(RELATIVE_FILE_PATH);
			files = file.list();
		}

		for (String fileName : files) {
			File verifyFile = new File(RELATIVE_FILE_PATH + fileName);
			if (verifyFile.isFile()) {
				if (this.getClass().getSimpleName().startsWith(fileName.substring(0, fileName.indexOf(".xlsx")))) {
					System.out.println("Matching Data source file found - " + fileName);
					actualFileName = RELATIVE_FILE_PATH + fileName;
				}
			}
		}
		if (actualFileName =="") {
			System.out.println("Matching Data Source file not found");
		}
		return actualFileName;
	}
}


