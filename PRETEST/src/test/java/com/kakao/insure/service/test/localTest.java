package com.kakao.insure.service.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class localTest {

	public static void main(String[] args) throws Exception {
		System.out.println("1,2,3,4,5".indexOf("1"));
		System.out.println("1,2,3,4,5".indexOf("2"));
		System.out.println("1,2,3,4,5".indexOf("3"));
		System.out.println("1,2,3,4,5".indexOf("4"));
		System.out.println("1,2,3,4,5".indexOf("5"));
		System.out.println("1,2,3,4,5".indexOf("9"));
	}
	
	public static String addYearMonthDay(String sDate, int year, int month, int day) {



		String dateStr = sDate;


		Calendar cal = Calendar.getInstance();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

		try {

			cal.setTime(sdf.parse(dateStr));

		} catch (ParseException e) {

			throw new IllegalArgumentException("Invalid date format: " + dateStr);

		}



		if (year != 0)

			cal.add(Calendar.YEAR, year);

		if (month != 0)

			cal.add(Calendar.MONTH, month);

		if (day != 0)

			cal.add(Calendar.DATE, day);

		return sdf.format(cal.getTime());

	}

}
