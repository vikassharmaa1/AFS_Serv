package com.util;

import java.io.File;
import java.io.IOException;

import org.codehaus.plexus.util.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class TakeScreenShot {

	public TakeScreenShot() {

	}

	public static String capture(WebDriver webdriver, String reportName) throws IOException {

		File dir = new File(System.getProperty("user.dir") + "test-results/screenshots/");
		if (!(dir.isDirectory())) {

			dir.mkdir();
		}
		String fileName = reportName + "_" + ReportManager.getCurrentDateandTime() + ".png";
		String screenShotLocation = System.getProperty("user.dir") + "test-results/screenshots/" + fileName;
		TakesScreenshot screenshot = ((TakesScreenshot) webdriver);

		File srcFile = screenshot.getScreenshotAs(OutputType.FILE);

		File destFile = new File(screenShotLocation);
		FileUtils.copyFile(srcFile, destFile);
		return screenShotLocation;
	}

}
