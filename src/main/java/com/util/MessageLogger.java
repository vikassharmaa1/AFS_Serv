package com.util;

import java.sql.Timestamp;
import java.util.Date;

public class MessageLogger {

	static Date date = new Date();

	public static void WriteMessageToConsole(String message) {
		date = new Date();
		System.out.println("(" + new Timestamp(date.getTime()) + ")" + message);
	}

	public static void WriteExceptionToConsole(String message, String exception) {
		date = new Date();
		System.out.println("(" + new Timestamp(date.getTime()) + ")" + message);
		System.out.println("(" + new Timestamp(date.getTime()) + ")" + exception);
	}

	public static void WriteToLogs(String message) {
		if ((System.getProperty("bank.selenium.console.log") != null)
				&& System.getProperty("bank.selenium.console.log").trim().toLowerCase().equals("true"));
			
		System.out.println(message);
	}

}
