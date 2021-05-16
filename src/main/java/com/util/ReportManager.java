package com.util;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.plexus.util.FileUtils;
import org.openqa.selenium.Platform;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.*;


public class ReportManager {

	private static ExtentReports extent;

	private static Platform platform;
	private static String reportFileName = "Automation-Execution-Report";
	private static String macPath = System.getProperty("user.dir") + "/test-results";
	private static String windowsPath = System.getProperty("user.dir") + "\\test-results";
	private static String windowsPathBackup = System.getProperty("user.dir") + "\\test-backup-results";
	private static String macReportFileLoc = macPath + "/" + reportFileName + "_" + getCurrentDateandTime() + ".html";
	private static String windowsReportFileLoc = windowsPath + "\\" + reportFileName + "_" + getCurrentDateandTime()
			+ ".html";

	public static ExtentReports getInstance() {

		if (extent == null) {
			createInstance();

		}
		return extent;
	}

	// Create an extent report instance
	public static ExtentReports createInstance() {
		platform = getCurrentPlatform();
		String fileName = getReportFileLocation(platform);

		ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(fileName);
		htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
		htmlReporter.config().setChartVisibilityOnOpen(true);
		htmlReporter.config().setTheme(Theme.STANDARD);
		System.out.println("filename is - " + fileName);
		htmlReporter.config().setDocumentTitle(reportFileName + "_" + getCurrentDateandTime() + ".html");
		htmlReporter.config().setEncoding("utf-8");
		htmlReporter.config().setReportName(fileName);
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		return extent;

	}

	private static Platform getCurrentPlatform() {

		if (platform == null) {

			String operSys = System.getProperty("os.name").toLowerCase();
			if (operSys.contains("win")) {
				platform = Platform.WINDOWS;

			} else if (operSys.contains("nix") || operSys.contains("nux") || operSys.contains("aix")) {

				platform = Platform.LINUX;

			} else if (operSys.contains("mac")) {

				platform = Platform.MAC;

			}
		}

		System.out.println("Platform is:" + platform);
		return platform;
	}

	private static String getReportFileLocation(Platform platform) {
		String reportFileLocation = null;
		switch (platform) {
		case MAC:
			reportFileLocation = macReportFileLoc;
			createReportPath(macPath);
			System.out.println("Test Execution report path for MAC:" + macPath + "\n");
			break;

		case LINUX:
			reportFileLocation = macReportFileLoc;
			createReportPath(macPath);
			System.out.println("Test Execution report path for LINUX:" + macPath + "\n");
			break;

		case WINDOWS:
			reportFileLocation = windowsReportFileLoc;
			createReportPath(windowsPath);
			System.out.println("Test Execution report path for WINDOWS:" + windowsPath + "\n");
			break;

		default:

			System.out.println("Test Execution report path has not been set! As there was a problem!\n");
			break;

		}

		return reportFileLocation;

	}

	// create the report path if does not exist
	private static void createReportPath(String path) {
		File testDirectory = new File(path);
		if (!testDirectory.exists()) {

			if (testDirectory.mkdir()) {

				System.out.println("Directory:" + path + "is created!");

			} else {

				System.out.println("Failed to create Directory" + path);
			}
		} else {

			try {
				if (System.getProperty("os.name").toLowerCase().contains("win")) {

					File trgDir = new File(windowsPathBackup);
					FileUtils.copyDirectory(testDirectory, trgDir);
					FileUtils.deleteDirectory(testDirectory);
					testDirectory.mkdir();

				}
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

	}

	public static String getCurrentDateandTime() {
		DateFormat dateFormat = new SimpleDateFormat("MMM_dd_yyyy_HH_mm_ss_z");
		Date date = new Date();
		String newDate = dateFormat.format(date); // 2019/12/15 12:08:43
		return newDate;
	}

}
