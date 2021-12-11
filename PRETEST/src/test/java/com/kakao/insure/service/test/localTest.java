package com.kakao.insure.service.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class localTest {

	public static void main(String[] args) throws Exception {
		String sDate = "20210101";
		String eDate = "20210201";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
		
		Date startDate = sdf.parse(sDate);
		Date endDate = sdf.parse(eDate);
		System.out.println(startDate);
		System.out.println(endDate);
		System.out.println(endDate.after(startDate));
		System.out.println(startDate.equals(endDate));
		
		
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
