package com.examples.control;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.zip.DataFormatException;


/**
 * This class contains method that parse information
 * that were entered by the users. 
 * 
 * @author Kenny Liau
 */
public class DataParser 
{


	/**
	 * @param id - String ID#
	 * @param fullname - String full name
	 * @return a User object
	 */
	/*public static User parseUser(String id, String fullname) {
		User usr = new User(id, fullname);
		return usr;
	}*/

	/**
	 * @param start - String time
	 * @return a Calendar object
	 * @throws DataFormatException
	 */
	public static Calendar parseCalendar(String time) throws DataFormatException {
		int month;
		int date;
		int year;
		int hour;
		int minute;

		if(time.length() != 16) { throw new DataFormatException(); }

		String Month = time.substring(0, 2);
		if(time.charAt(2) != '/') { throw new DataFormatException(); }
		String Date = time.substring(3, 5);
		if(time.charAt(5) != '/') { throw new DataFormatException(); }
		String Year = time.substring(6,10);
		if(time.charAt(10) != '-') { throw new DataFormatException(); }
		String Hour = time.substring(11, 13);
		if(time.charAt(13) != ':') { throw new DataFormatException(); }
		String Minute = time.substring(14, 16);

		try {
			month = Integer.parseInt(Month);
			date = Integer.parseInt(Date);
			year = Integer.parseInt(Year);
			hour = Integer.parseInt(Hour);
			minute = Integer.parseInt(Minute);
		}
		catch(NumberFormatException e) {
			throw new DataFormatException();
		}


		if(		month < 1   || month > 12  || 
				date < 1    || date   > 31 ||
				year < 0                   || 
				hour < 0    || hour >23    ||
				minute < 0  || minute > 59    )
		{
			throw new DataFormatException();
		}

		GregorianCalendar cal = new GregorianCalendar();

		// February
		if(month == 2)
		{
			// not leap year
			if(cal.isLeapYear(year) == false)
			{
				if(date > 28)
				{
					throw new DataFormatException();
				}
			}
			else if(date > 29)
			{
				
				throw new DataFormatException();
			}
		}



		// 30 days month
		if(month == 4 || month == 6 || month == 9 || month == 11) {
			if(date > 30) {
				throw new DataFormatException();
			}
		}

		// no events earlier than april 5 2011

		if(year < 2011)
		{
			throw new DataFormatException();
		}
		
		if(year <= 2011 && month < 4)
		{
			throw new DataFormatException();

		}
		if(year < 2011 && month < 4 && date < 5)
		{
			throw new DataFormatException();
		}
		
		if(year <= 2011 && month <= 4 && date < 5)
		{
			throw new DataFormatException();
		}


		cal.set(Calendar.MONTH, month-1);

		cal.set(Calendar.DATE, date);

		cal.set(Calendar.YEAR, year);

		cal.set(Calendar.HOUR_OF_DAY, hour);

		cal.set(Calendar.MINUTE, minute);
		
		cal.set(Calendar.SECOND, 0);

		cal.set(Calendar.MILLISECOND, 0);


		return cal;
	}
}

