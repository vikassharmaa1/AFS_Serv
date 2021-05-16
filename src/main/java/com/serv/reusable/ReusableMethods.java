package com.serv.reusable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import com.serv.pages.LoginPage;
import com.pages.common.BasePage;




public class ReusableMethods extends BasePage {

	public ReusableMethods(WebDriver selenium) {

		super(selenium);
		initialize(this);
	}

	WebDriverWait wait = new WebDriverWait(selenium, 60);

	public void waitforElement(WebElement element, String ElementName) {

		try {
			WebDriverWait wait = new WebDriverWait(selenium, 10);

			wait.until(ExpectedConditions.elementToBeClickable(element));
			if (element.isDisplayed()) {

				reportLog("Pass", "Element" + ElementName + "is displayed", false);
			} else {

				reportLog("Pass", "Element" + ElementName + "is displayed", false);
			}
		} catch (Exception e) {
			System.out.println("Error in " + this.getClass().getSimpleName() + " : " + e.getMessage());
			reportLog("Fail", this.getClass().getName() + " Exception Occured with message " + e.getMessage(), true);
			e.printStackTrace();
		}
	}

	public void waitForSeconds(int inTime) {

		try {
			Thread.sleep(inTime * 1000);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}

	}
	
	


	public String decodepass(String strToDecrypt) {

		Base64.Decoder decoder = Base64.getUrlDecoder();
		String dStr = new String(decoder.decode(strToDecrypt));
		return dStr;

	}

	public void safeType(WebElement objToEnterValue, String Value, String webElementName) {

		try {
			objToEnterValue.clear();
			objToEnterValue.sendKeys();

			if (!webElementName.toUpperCase().contains("PASSW1ORD")) {
				reportLog("Pass", "" + Value + " Value is entered in " + webElementName, false);

			} else {

				reportLog("Fail", "" + Value + " Value is not entered in " + webElementName, false);
			}
		} catch (NoSuchElementException e) {
			System.out.println("Error in " + this.getClass().getSimpleName() + " : " + e.getMessage());
			reportLog("Fail", this.getClass().getName() + " Exception Occured with message " + e.getMessage(), true);
			e.printStackTrace();
		}
	}

	public void safeClick(WebElement objClick, String webElementName) {

		try {

			wait.until(ExpectedConditions.elementToBeClickable(objClick));
			WaitObjEnabled(objClick, webElementName, "Yes");
			objClick.click();

			reportLog("Pass", "Performed click operation on" + webElementName, false);
		} catch (NoSuchElementException ex) {
			reportLog("", webElementName + "is not displayed as expected in application screen", false);
		}

		catch (Exception e) {
			System.out.println("Error in " + this.getClass().getSimpleName() + " : " + e.getMessage());
			reportLog("Fail", this.getClass().getName() + " Exception Occured with message " + e.getMessage(), true);
			e.printStackTrace();
		}
	}

	public void WaitObjEnabled(WebElement objEnabled, String webElementName, String reportingYesNo) {

		for (int i = 0; i < 5; i++) {

			try {
				Boolean b = objEnabled.isEnabled();
				if (reportingYesNo.toUpperCase().equals("YES") && b == true) {

					reportLog("pass", webElementName + " - is enabled in application screen now..", false);
					break;
				}
			} catch (Exception ex) {
				if (reportingYesNo.toUpperCase().equals("YES")) {

					reportLog("fail", webElementName + " - is not enabled in application screen now..", false);

				} else {

					reportLog("fail",
							webElementName + " - is not enabled in application screen reporting is set to None!!!",
							false);
				}

			}

		}
	}

	public boolean selectTableRecord() {

		try {
			if (selenium.findElements(By.xpath("")).size() > 0) {

				selenium.findElement(By.xpath("")).click();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}

		return true;
	}

	public void fnIsElementDisplayed(WebElement element, String objectDescription, String objectType) {

		try {
			wait.until(ExpectedConditions.visibilityOf(element));
			if (element.isDisplayed()) {

				reportLog("pass", objectType + "-" + objectDescription + "is displayed", false);

			}

			else {

				reportLog("fail", objectType + "-" + objectDescription + "is not displayed", false);
			}
		} catch (Exception e) {
			System.out.println("Error in" + this.getClass().getSimpleName() + ":" + e.getMessage());
			reportLog("fail", this.getClass().getName() + "Exception occurred with message" + e.getMessage(), true);
			e.printStackTrace();
		}
	}

	public String generateUniqueShortName() {
		String shortname = null;

		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss a");
			Date date = new Date();

			String datestring = dateFormat.format(date);
			String dateslash = datestring.replaceAll("/", "");
			String datespace = dateslash.replaceAll(":", "");
			String datespace1 = datespace.replaceAll("M", "");

			shortname = datespace1.replaceAll(" ", "");
			reportLog("pass", "Unique ShortName is generated", false);
		} catch (NoSuchElementException e) {
			System.out.println("Error in" + this.getClass().getSimpleName() + ":" + e.getMessage());
			reportLog("fail", this.getClass().getName() + "Exception occurred with message" + e.getMessage(), true);
			e.printStackTrace();
		}

		return shortname;

	}

	public void SelectdrpdwnFromList(WebElement objClick, String webElementName, String strValue) {

		try {
			safeClick(objClick, webElementName);
			Thread.sleep(2000);
			selenium.findElement(By.xpath("//div[contains(text(),'" + strValue + "')]")).click();
			reportLog("pass",
					"performed click operation on" + webElementName + "and the value" + strValue + "is selected",
					false);
		} catch (NoSuchElementException ex) {

			reportLog("fail", webElementName + "is not displayed as expected in application screen", false);

		} catch (Exception e) {

			System.out.println("Error in" + this.getClass().getSimpleName() + ":" + e.getMessage());
			reportLog("fail", this.getClass().getName() + "Exception occurred with message" + e.getMessage(), true);
			e.printStackTrace();

		}

	}

	public void moveToElement(WebElement element) {

		new Actions(selenium).moveToElement(element).perform();
	}

	public void Sendkeys(String Value) {

		new Actions(selenium).sendKeys(Value).perform();
	}

	public void fnAjaxDropDown(String label, String value) {

		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//label[contains(text(), '" + label + "')]/following-sibling::div")));

			WebElement dropdown = selenium
					.findElement(By.xpath("//label[text() =  '" + label + "')]/following-sibling::div"));

			dropdown.click();
			if (value.contains("-") == false && value.contains("'") == false) {

				Sendkeys(value);
				selenium.findElement(
						By.xpath("//div[@class = 'select-2-result-label' and contains(text(),'" + value + "')]"))
						.click();

			} else if (value.contains("'")) {

				String strValue1 = value.split("'")[0];
				Sendkeys(strValue1);
				selenium.findElement(By.xpath("//div[contains(text(),'" + strValue1 + "')]")).click();

			} else if (value.contains(" - ")) {

				String strValue1 = value.split(" - ")[0];
				Sendkeys(strValue1);
				selenium.findElement(By.xpath("//div[.='" + strValue1 + "')]")).click();

			}

			if (selenium
					.findElements(By.xpath("//label[contains(text(),'" + label
							+ "')]/following-sibling::div[contains(@class,'ng-not-empty')]/a[@class='select2-choice']"))
					.size() != 0) {

				reportLog("pass", "The value" + value + "is selected in dropdown" + label, false);

			} else {

				reportLog("fail", "The value" + value + "is not selected in dropdown" + label, false);

				dropdown.sendKeys(Keys.ESCAPE);
			}

		} catch (Exception e) {

			System.out.println("Error in" + this.getClass().getSimpleName() + ":" + e.getMessage());
			reportLog("fail", this.getClass().getName() + "Exception occurred with message" + e.getMessage(), true);
			e.printStackTrace();
		}

	}

	public String safeGetText(WebElement objToGetText) {

		String expText = null;
		try {
			objToGetText.isDisplayed();
			objToGetText.isEnabled();
			expText = objToGetText.getText();
		} catch (NoSuchElementException noEx) {
			reportLog("fail", noEx.getMessage(), false);

		} catch (Exception e) {

			System.out.println("Error in" + this.getClass().getSimpleName() + ":" + e.getMessage());
			reportLog("fail", this.getClass().getName() + "Exception occurred with message" + e.getMessage(), true);
			e.printStackTrace();
		}

		return expText;
	}

	public boolean selectFirstRecordWithoutGrid() {

		try {
			selenium.findElement(By
					.xpath("//div[@class = 'ui-grid-selection-row-header-buttons fa-fa-lg ng-scope fa-square-o'])[1]"))
					.click();
		} catch (Exception e) {
			System.out.println("Error in" + this.getClass().getSimpleName() + ":" + e.getMessage());
			reportLog("fail", this.getClass().getName() + "Exception occurred with message" + e.getMessage(), true);
			e.printStackTrace();
			return false;
		}

		return true;
	}
}
