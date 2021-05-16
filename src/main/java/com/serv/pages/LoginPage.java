package com.serv.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.pages.common.BasePage;
import com.serv.reusable.ReusableMethods;

public class LoginPage extends BasePage {
	
	/*public static String username, pass;*/
	
	public LoginPage(WebDriver selenium) {
		
		super(selenium);
		initialize(this);
	}

	WebDriverWait wait = new WebDriverWait(selenium, 60);
	ReusableMethods rm  = new ReusableMethods(selenium);


/*	@FindBy(how = How.XPATH, using = "//span[contains(text(), 'Account & Lists')]")
	public static WebElement btn_accountLists;*/
	
	

}
