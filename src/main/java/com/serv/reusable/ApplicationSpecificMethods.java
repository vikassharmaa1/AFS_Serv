package com.serv.reusable;

import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.pages.common.BasePage;

public class ApplicationSpecificMethods extends BasePage {
	public static String username, pass;

	public ApplicationSpecificMethods(WebDriver selenium) {

		super(selenium);
		initialize(this);
	}

	WebDriverWait wait = new WebDriverWait(selenium, 60);
	ReusableMethods rm = new ReusableMethods(selenium);

	@FindBy(how = How.XPATH, using = "//span[contains(text(), 'Account & Lists')]")
	public static WebElement btn_accountLists;

	@FindBy(how = How.XPATH, using = "//input[@id, 'ap_email']")
	public static WebElement edit_email;

	@FindBy(how = How.XPATH, using = "//input[@id, 'continue']")
	public static WebElement btn_continue;

	@FindBy(how = How.XPATH, using = "//input[@id, 'ap_password']")
	public static WebElement edit_password;

	@FindBy(how = How.XPATH, using = "//input[@id, 'signInSubmit']")
	public static WebElement btn_login;

	public void loginToAmazon(Map<String, String> testData) {

		try {
			String username = testData.get("Username");
			String pass = testData.get("Password");

			rm.safeClick(btn_accountLists, "Account & Lists Button");
			rm.safeType(edit_email, username, "Email ID");
			rm.safeClick(btn_continue, "Continue Button");
			rm.safeType(edit_password, pass, "Password");
			rm.safeClick(btn_login, "Login Button");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
