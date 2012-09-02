package com.examples.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.zip.DataFormatException;

import android.util.Log;

import com.examples.control.DataParser;


/**
 * This class creates an object of Appointment with,
 * a description of the appointment, a location for the appointment,
 * a java.util.Calendar representing the beginning of the appointment, and
 * a java.util.Calendar representing the end of the appointment.
 * 
 * @author Kenny Liau
 *
 */
public class Appointment implements Comparable<Appointment>
{

	
	/**
	 * 
	 */

	 public int id;
	/**
	 * A description of the appointment.
	 */
	private String description;
	
	/**
	 * A location of the appointment.
	 */
	private String location;
	
	/**
	 * A java.util.Calendar representing the beginning
	 * of the appointment.
	 */
	private Calendar start;

	/**
	 * A java.util.Calendar representing the end
	 * of the appointment.
	 */
	private Calendar end;

	/**
	 * Constructor to create an Appointment object.
	 * @param desc Description of Appointment
	 * @param loc Location of Appointment
	 * @param startC Calendar representation of start date
	 * @param endC Calendar representation of end date
	 */
	public Appointment(int id, Calendar startC, Calendar endC, String desc, String loc)
	{
		this.id = id;
		setDescripton(desc);
		setLocation(loc);
		this.start = startC;
		this.end = endC;
		
	}
	
	/**
	 * Set the parameter to the Appointment object's Description .
	 * @param desc
	 */
	public void setDescripton(String desc)
	{
		this.description = desc;
	}
	
	/**
	 * Set the parameter to the Appointment object's Location.
	 * @param loc
	 */
	public void setLocation(String loc)
	{
		this.location = loc;
	}
	
	/**
	 * Set the Start Date
	 * @param start
	 */
	public void setStartDate(Calendar start) {
		this.start = start;
	}
	
	/**
	 * Set the EndDate
	 * @param end
	 */
	public void setEndDate(Calendar end) {
		this.end = end;
	}
	
	/**
	 * Returns Description of Appointment
	 * @return
	 */
	public String getDescription() {
		return this.description;
	}
	
	/**
	 * Returns Location of Appointment
	 * @return
	 */
	public String getLocation() {
		return this.location;
	}
	
	/**
	 * Returns Start Date of Appointment
	 * @return
	 */
	public Calendar getStartDate() {
		return this.start;
	}
	
	/**
	 * Returns End Date of Appointment
	 * @return
	 */
	public Calendar getEndDate() {
		return this.end;
	}
	
	/**
	 * Given a Calendar, return a String representation
	 * of month in two digit format. 
	 * @param Calendar c
	 * @return String month
	 */
	public static String getMonth(Calendar c)
	{
		String month = "";
		int m = (c.get(Calendar.MONTH)) + 1;
		if(m < 10)
		{
			month = "0" + m;
		}
		else
		{
			month = "" + m;
		}

		return month;
	}
	
	/**
	 * Given a Calendar, return a String representation
	 * of date in two digit format. 
	 * @param Calendar c
	 * @return String date
	 */
	public static String getDay(Calendar c)
	{
		String date = "";
		int d = (c.get(Calendar.DATE));
		if(d < 10)
		{
			date = "0" + d;
		}
		else
		{
			date = "" + d;
		}

		return date;
	}
	
	/**
	 * Given a Calendar, return a String representation
	 * of year in four digit format. 
	 * @param Calendar c
	 * @return String year
	 */
	public static String getYear(Calendar c)
	{
		return "" + c.get(Calendar.YEAR);
	}
	
	/**
	 * Given a Calendar, return a String representation
	 * of hour in two digit format. 
	 * @param Calendar c
	 * @return String hour
	 */
	public static String getHour(Calendar c)
	{
		String hour = "";
		int h = (c.get(Calendar.HOUR_OF_DAY));
		if(h < 10) {
			hour = "0" + h;
		}
		else {
			hour = "" + h;
		}
		return hour;	
	}
	
	/**
	 * Given a Calendar, return a String representation
	 * of minute in two digit format. 
	 * @param Calendar c
	 * @return String minute
	 */
	public static String getMinute(Calendar c)
	{
		String minute = "";
		int m = (c.get(Calendar.MINUTE));
		if(m < 10)
		{
			minute = "0" + m;
		}
		else
		{
			minute = "" + m;
		}

		return minute;	
	}
	
	/**
	 * Print out both the date/time of 
	 * startTime and endTime
	 * in the format of MM/DD/YYYY-HH:MM
	 * @return String s
	 */
	public String toString()
	{
		
	//	String s = getMonth(start) + "/" + getDay(start) + "/" + getYear(start)  + "-" + getHour(start)  + ":" + getMinute(start) + " " +
	//			   getMonth(end) + "/" + getDay(end) + "/" + getYear(end)  + "-" + getHour(end)  + ":" + getMinute(end) + " " + getDescription() + " at " + getLocation();
		String s = getDescription() + " on " + dateToString(start) + " at " + timeToString(start) + " in " + getLocation();
			
			
		return s;	
	}
	
	public static String dateToString(Calendar c)
	{
		
		String s = getMonth(c) + "/" + getDay(c) + "/" + getYear(c);
		return s;	
	}
	
	public static String timeToString(Calendar c)
	{
		
		String s = getHour(c)  + ":" + getMinute(c);
		return s;	
	}

	/**
	 * Check if appointment conflicting with another appointment
	 * @param o 
	 * @return true if conflicting
	 */
	public boolean ifConflicting(Appointment o) {
		int cmp = this.compareTo(o);
		if(cmp == 0) {
			return false;
		}
		cmp = this.getStartDate().compareTo(o.getStartDate());
		if(cmp == 0) {
			cmp = this.getEndDate().compareTo(o.getEndDate());
			if(cmp == 0) {
				return true;
			}
		}
		return false;
	}
	
	public int compareTo(Appointment o) {
		int cmp = this.getStartDate().compareTo(o.getStartDate());
		if(cmp != 0) {
			return cmp;
		}
		cmp = this.getEndDate().compareTo(o.getEndDate());
		if(cmp != 0) {
			return cmp;
		}
		cmp = this.getLocation().compareTo(o.getLocation());
		if(cmp != 0) {
			return cmp;
		}
		cmp = this.getDescription().compareTo(o.getDescription());
		if(cmp != 0) {
			return cmp;
		}
		return 0;
	}

	public String getString() 
	{
		return id + "|" + dateToString(start) + "-" + timeToString(start) + "|" + dateToString(end) + "-" + timeToString(end) + "|" + description + "|" + location;

	}	
	
	private Appointment()
	{
	
	}
	
	public static Appointment getInstance(String eventInfo) 
	{
		StringTokenizer st = new StringTokenizer(eventInfo,"|");
		Appointment appt = new Appointment();
		appt.id = Integer.parseInt(st.nextToken());
		try {
			appt.start = DataParser.parseCalendar(st.nextToken());
			appt.end = DataParser.parseCalendar(st.nextToken());

		} catch (DataFormatException e) {

		}
		appt.description = "";
		appt.location = "";
		//if (st.hasMoreTokens()) {
			appt.description = st.nextToken();
		//}
		//if (st.hasMoreTokens()) {
			appt.location = st.nextToken();
	//	}
		return appt;
	}
	
	
}
