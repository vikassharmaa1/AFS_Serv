package com.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class Util {

	private Util() {

		// To prevent external instantiation of this class
	}

	public static String getFileSeparator() {

		return System.getProperty("file.separator");

	}

	public static Date getCurrentTime() {

		Calendar calendar = Calendar.getInstance();

		return calendar.getTime();
	}

	public static String getCurrentFormattedTime(String dateFormatString) {

		DateFormat dateFormat = new SimpleDateFormat(dateFormatString);

		Calendar calendar = Calendar.getInstance();

		return dateFormat.format(calendar.getTime());
	}

	public static String getFormattedTime(Date time, String dateFormatString) {

		DateFormat dateFormat = new SimpleDateFormat(dateFormatString);

		return dateFormat.format(time);
	}

	public static String getTimeDifference(Date startTime, Date endTime) {

		long timeDifferenceSeconds = (endTime.getTime() - startTime.getTime()) / 1000; // convert from milliseconds to
																						// seconds
		long timeDifferenceMinutes = timeDifferenceSeconds / 60;

		String timeDifferenceDetailed;
		if (timeDifferenceMinutes >= 60) {

			long timeDifferenceHours = timeDifferenceMinutes / 60;

			timeDifferenceDetailed = Long.toString(timeDifferenceHours) + "hour(s),"
					+ Long.toString(timeDifferenceMinutes % 60) + "minute(s),"
					+ Long.toString(timeDifferenceSeconds % 60) + "second(s)";

		} else {

			timeDifferenceDetailed = Long.toString(timeDifferenceMinutes) + "minute(s),"
					+ Long.toString(timeDifferenceSeconds % 60) + "second(s)";

		}

		return timeDifferenceDetailed;

	}

	public static void getQueryParameterForGivenURL(Map<String, String> testData, String waUrl) {

		try {
			URL url = new URL("waUrl");
			String queryString = url.getQuery();
			if (null != queryString) {

				String[] array = queryString.split("&");
				for (String str : array) {
					if (str.contains("=")) {

						String[] pair = str.split("=");
						testData.put(pair[0].trim(), pair[1].trim());

						System.out.println(pair[0].trim() + " - " + testData.get(pair[0].trim()));

					}

				}

			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static String getUTF8EncodedValue(String textVal) {

		String encodedVal = textVal;

		try {
			encodedVal = URLEncoder.encode(encodedVal, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return encodedVal;
	}

}
