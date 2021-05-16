package com.serv.tests;

import org.testng.annotations.Test;
import java.util.Map;
import java.util.concurrent.TimeoutException;


import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.pages.common.BaseTest;
import com.serv.reusable.ApplicationSpecificMethods;

public class AFSServicingScenario extends BaseTest {

	@Test(dataProvider = "testData")
	public void loginToAmazon1(Map<String, String> testData) throws InterruptedException, TimeoutException {
		ApplicationSpecificMethods asm = new ApplicationSpecificMethods(selenium());
		asm.loginToAmazon(testData);

	}

}
