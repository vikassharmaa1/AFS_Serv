package com.webdriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;

import com.util.MessageLogger;

public class PropertiesLoader {
	static final String base_directory = "src/main/";
	private static Properties mobileProperties;

	public static Properties getMobilePropertiesInstance() {
		mobileProperties = loadMobileProperties();
		return mobileProperties;
	}

	private static Properties loadMobileProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	public static void loadProperties() {

		loadBaseProperties();

		loadMobileProperties();

		loadApiProperties();

		if (StringUtils.isNotBlank(System.getProperty("bank.environment"))) {
			loadEnvironmentProperties(System.getProperty("bank.environment").toString());
		}

		loadLocalProperties();

		loadBuildToolProperties();

		System.setProperty("bank.properties.loaded", "true");

	}

	private static void loadBuildToolProperties() {
		MessageLogger.WriteMessageToConsole("Loading Build Tool Properties - Maven or Gradle");
		Properties properties = System.getProperties();

		if (StringUtils.isNotBlank(System.getProperty("buildtool.selenium.host"))) {
			properties.setProperty("bank.selenium.host", System.getProperty("buildtool.selenium.host"));
		}

		if (StringUtils.isNotBlank(System.getProperty("buildtool.selenium.host"))) {
			properties.setProperty("bank.environment", System.getProperty("buildtool.selenium.port"));
		}

		if (StringUtils.isNotBlank(System.getProperty("buildtool.environment"))) {
			properties.setProperty("bank.selenium.host", System.getProperty("buildtool.environment"));
		}

		if (StringUtils.isNotBlank(System.getProperty("gradle.remote.hub"))) {
			properties.setProperty("gradle.remote.hub", System.getProperty("gradle.remote.hub"));
		}
		if (StringUtils.isNotBlank(System.getProperty("gradle.selenium.host"))) {
			properties.setProperty("gradle.selenium.host", System.getProperty("gradle.selenium.host"));
		}
		if (StringUtils.isNotBlank(System.getProperty("gradle.browser.name"))) {
			properties.setProperty("gradle.browser.name", System.getProperty("gradle.browser.name"));
		}
		if (StringUtils.isNotBlank(System.getProperty("gradle.browser.state"))) {
			properties.setProperty("gradle.browser.state", System.getProperty("gradle.browser.state"));
		}
		if (StringUtils.isNotBlank(System.getProperty("gradle.browser.responsive"))) {
			properties.setProperty("gradle.browser.responsive", System.getProperty("gradle.browser.responsive"));
		}
		if (StringUtils.isNotBlank(System.getProperty("gradle.browser.width"))) {
			properties.setProperty("gradle.browser.width", System.getProperty("gradle.browser.width"));
		}
		if (StringUtils.isNotBlank(System.getProperty("gradle.chrome.driver"))) {
			properties.setProperty("gradle.chrome.driver", System.getProperty("gradle.chrome.driver"));
		}
		if (StringUtils.isNotBlank(System.getProperty("gradle.ie.driver"))) {
			properties.setProperty("gradle.ie.driver", System.getProperty("gradle.ie.driver"));
		}
		if (StringUtils.isNotBlank(System.getProperty("gradle.gecko.driver"))) {
			properties.setProperty("gradle.gecko.driver", System.getProperty("gradle.gecko.driver"));
		}

	}

	private static void loadLocalProperties() {
		Properties prop = new Properties();
		try {
			if (new File(base_directory + "local properties").exists()) {
				prop.load(new FileInputStream(base_directory + "local properties"));

				for (String key : prop.stringPropertyNames()) {
					if (key.equals("bank.environment")) {
						loadEnvironmentProperties(prop.getProperty(key));
					}
					Properties properties = System.getProperties();
					properties.setProperty(key, prop.getProperty(key));

				}
			}

		} catch (IOException e) {
			MessageLogger.WriteExceptionToConsole("could not load environment local properties", e.toString());
		}

	}

	private static void loadEnvironmentProperties(String env) {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(base_directory + env + "properties"));

			for (String key : prop.stringPropertyNames()) {
				Properties properties = System.getProperties();

				if (key.contains("password") && StringUtils.isNotBlank(prop.getProperty(key))) {
					properties.setProperty(key, prop.getProperty(key));
				} else {
					properties.setProperty(key, prop.getProperty(key));
				}
			}
		} catch (IOException e) {
			MessageLogger.WriteExceptionToConsole("could not load environment local properties" + env + "properties",
					e.toString());
		}

	}

	private static void loadApiProperties() {
		// TODO Auto-generated method stub

	}

	public static void loadBaseProperties() {

		Properties prop = new Properties();

		try {
			prop.load(new FileInputStream(base_directory + "base.properties"));

			for (String key : prop.stringPropertyNames()) {
				Properties properties = System.getProperties();

				if (StringUtils.isNotBlank(prop.getProperty("bank.password.encryption"))
						&& prop.getProperty("bank.password.encryption").equalsIgnoreCase("enabled")
						&& (key.contains("password") && StringUtils.isNotBlank(prop.getProperty(key)))) {
					properties.setProperty(key, prop.getProperty(key));
				}
			}
		} catch (IOException e) {
			MessageLogger.WriteExceptionToConsole("Could not load base properties", e.toString());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Properties loadBaseProperties(URI uri) {

		Properties prop = new Properties();
		Properties properties = new Properties();

		try {

			if (StringUtils.isNotEmpty(uri.toString())) {
				prop.load(new FileInputStream(new File(uri)));
			} else {
				prop.load(new FileInputStream(base_directory + "base.properties"));
			}

			for (String key : prop.stringPropertyNames()) {
				properties = System.getProperties();
				if (key.contains("password") && StringUtils.isNotBlank(prop.getProperty(key))) {
					properties.setProperty(key, prop.getProperty(key));
				}
			}
			return properties;

		} catch (IOException e) {
			MessageLogger.WriteExceptionToConsole("Could not load base properties", e.toString());

		}
		return null;
	}

}
