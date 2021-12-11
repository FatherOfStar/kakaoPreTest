package com.kakao.insure.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

	/**
	 * <p>yyyyMMdd 형식의 날짜 문자열을 입력 받아 유효한 날짜인지 검사.</p>
	 *
	 * <pre>
	 * DateUtil.checkDate("19990235") = false
	 * DateUtil.checkDate("20001331") = false
	 * DateUtil.checkDate("20061131") = false
	 * DateUtil.checkDate("20060228")  = false
	 * DateUtil.checkDate("20060208")   = false
	 * DateUtil.checkDate("20060228")   = true
	 * </pre>
	 *
	 * @param  dateStr 날짜 문자열(yyyyMMdd, yyyy-MM-dd의 형식)
	 * @return  유효한 날짜인지 여부
	 */

	public static boolean checkDate(String sDate) {

		String dateStr = sDate;



		String year = dateStr.substring(0, 4);

		String month = dateStr.substring(4, 6);

		String day = dateStr.substring(6);



		return checkDate(year, month, day);

	}



	/**
	 * <p>입력한 년, 월, 일이 유효한지 검사.</p>
	 *
	 * @param  year 연도
	 * @param  month 월
	 * @param  day 일
	 * @return  유효한 날짜인지 여부
	 */

	public static boolean checkDate(String year, String month, String day) {

		try {

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());



			Date result = formatter.parse(year + "." + month + "." + day);

			String resultStr = formatter.format(result);

			if (resultStr.equalsIgnoreCase(year + "." + month + "." + day))

				return true;

			else

				return false;

		} catch (ParseException e) {

			return false;

		}

	}
	
	/**
	 * <p>yyyyMMdd 형식의 날짜 문자열을 입력 받아 유효한 날짜인지 검사.</p>
	 *
	 * <pre>
	 * DateUtil.checkDate("19990235") = false
	 * DateUtil.checkDate("20001331") = false
	 * DateUtil.checkDate("20061131") = false
	 * DateUtil.checkDate("20060228")  = false
	 * DateUtil.checkDate("20060208")   = false
	 * DateUtil.checkDate("20060228")   = true
	 * </pre>
	 *
	 * @param  dateStr 날짜 문자열(yyyyMMdd, yyyy-MM-dd의 형식)
	 * @return  유효한 날짜인지 여부
	 */

	public static int diffMonth(String sDate, String eDate) {

		if(!isMore(sDate,eDate)) return 0;
		
		int tmpDiffMonth = 0;
		int sYear = Integer.parseInt(sDate.substring(0, 4));
		int sMonth = Integer.parseInt(sDate.substring(4, 6));
		int sDay = Integer.parseInt(sDate.substring(6));
		
		int eYear = Integer.parseInt(eDate.substring(0, 4));
		int eMonth = Integer.parseInt(eDate.substring(4,6));
		int eDay = Integer.parseInt(eDate.substring(6));
		
		/* 해당년도에 12를 곱해서 총 개월수를 구하고 해당 월을 더 한다. */
		int month1 = sYear * 12 + sMonth;
		int month2 = eYear * 12 + eMonth;

		tmpDiffMonth = month2 - month1;
		
		
		if( eDay - sDay >= 0 )
		{// 종료일이 시작일과 같거나 클 경우 개월 +
			tmpDiffMonth = tmpDiffMonth + 1;
		}
		// 종료일과 시작일이 모두 말일일 경우 개월 +
		else if( 1 == Integer.parseInt(addYearMonthDay(sDate,0,0,1).substring(6)) && 1 == Integer.parseInt(addYearMonthDay(eDate,0,0,1).substring(6)))
		{
			tmpDiffMonth = tmpDiffMonth + 1;
		}
		return tmpDiffMonth;

	}
	/**
	 * 
	* </pre>
	 *
	 * @param  dateStr 날짜 문자열(yyyyMMdd, yyyy-MM-dd의 형식)
	 * @param  year 가감할 년. 0이 입력될 경우 가감이 없다
	 * @param  month 가감할 월. 0이 입력될 경우 가감이 없다
	 * @param  day 가감할 일. 0이 입력될 경우 가감이 없다
	 * @return  yyyyMMdd 형식의 날짜 문자열
	 * @throws IllegalArgumentException 날짜 포맷이 정해진 바와 다를 경우.
	 *         입력 값이 <code>null</code>인 경우.
	 */

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
	
	/**
	 * eDate가 sDate보다 크거나 같을 경우 true
	 * 
	 * @param sDate
	 * @param eDate
	 * @return
	 */
	public static boolean isMore(String sDate, String eDate) {
		if( !(checkDate(sDate) && checkDate(eDate)) ) return false;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
		try {
		Date startDate = sdf.parse(sDate);
		Date endDate = sdf.parse(eDate);
		
		if( endDate.after(startDate) || startDate.equals(endDate) ) return true;
		}
		catch( ParseException e)
		{
			System.out.println("ERROR "+ e.getMessage());
			return false;
		}
		return false;
		
	}
	
	/**
	 * 현재(한국기준) 날짜정보를 얻는다.                     <BR>
	 * 표기법은 yyyy-mm-dd                                  <BR>
	 * @return  String      yyyymmdd형태의 현재 한국시간.   <BR>
	 */

	public static String getCurrentDate() {

		Calendar aCalendar = Calendar.getInstance();



		int year = aCalendar.get(Calendar.YEAR);

		int month = aCalendar.get(Calendar.MONTH) + 1;

		int date = aCalendar.get(Calendar.DATE);

		String strDate = Integer.toString(year) + ((month < 10) ? "0" + Integer.toString(month) : Integer.toString(month))

				+ ((date < 10) ? "0" + Integer.toString(date) : Integer.toString(date));

		return strDate;

	}
}
