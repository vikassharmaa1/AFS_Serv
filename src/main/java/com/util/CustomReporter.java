package com.util;

import java.io.File;
import java.io.IOException;

import org.codehaus.plexus.util.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Reporter;

import com.webdriver.SeleniumFactory;

public class CustomReporter {

	public static final String ESCAPE_PROPERTY = "org.uncommons.reportLog.escape-output";

	public static String captureScreenshot(WebDriver webDriver, String reportName, String filePath) throws IOException {

		File dir = new File("./screenshots");

		if (!(dir.isDirectory())) {

			dir.mkdir();
		}

		String fileName = "/" + reportName + ".png";

		String screenShotLocation = filePath + fileName;
		TakesScreenshot screenshot = ((TakesScreenshot) webDriver);
		File srcFile = screenshot.getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(srcFile, new File(screenShotLocation));
		return screenShotLocation;
	}

	public static void reportLog(String ReportingType, String ReportMessage, String ReportName,
			Boolean haveScreenshot) {

		// String outputDir =
		// System.getProperty("user.dir")+"/target/surefire-reports/html";
		String outputDir = "./target/surefire-reports/html";
		String screenshotURL = "";

		System.setProperty(ESCAPE_PROPERTY, "false");

		if ((haveScreenshot != null) && (haveScreenshot == true)) {
			try {
				SeleniumFactory.getInstance().getDriver().manage().window().maximize();
				screenshotURL = captureScreenshot(SeleniumFactory.getInstance().getDriver(), ReportName, outputDir);

				Reporter.log("<a target = \"blank\" href =." + screenshotURL + ">"
						+ decorateMessage(ReportMessage, ReportingType) + "</a>");
			} catch (IOException e) {

				e.printStackTrace();
			}

		} else

		{
			Reporter.log(decorateMessage(ReportMessage, ReportingType));
		}

	}

	private static String decorateMessage(String Message, String MessageType) {
		String messageColor = "#595959";
		String messageBackGround = "#ffffff";
		switch (MessageType.trim().toLowerCase()) {

		case "pass":
			messageColor = "green";
			break;
		case "fail":

			messageColor = "red";
			break;
		case "info":

			messageColor = "grey";
			break;
		case "warning":

			messageColor = "yellow";
			messageBackGround = "#ffff66";
			break;
		default:

			break;

		}

		if ((System.getProperty("bank.selenium.console.log") != null)
				&& System.getProperty("bank.selenium.console.log").trim().toLowerCase().equals("true"))

		{
			System.out.println(Message);

		}

		return "<div style =\" color:" + messageColor + ";background:" + messageBackGround + "\">" + Message + "</div>";

	}

}
