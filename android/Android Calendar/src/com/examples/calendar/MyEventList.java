package com.examples.calendar;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.NoSuchElementException;

import com.examples.model.Appointment;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;


/**
 * This class implements the Singleton pattern: only one instance can be
 * created. 
 * 
 * @author Sesh Venugopal
 *
 */
public class MyEventList implements EventList {

	// single instance
	private static MyEventList eventList=null;

	// holds appointment in a sorted array list
	private static ArrayList<Appointment> eventsAL;
	private static ArrayList<Appointment> EAL;

	// keep track of max id dealt out so far
	private int maxId;

	// data for events
	public static final String EVENTS_FILE = "events.txt";

	// context used for I/O
	Context ctx=null;


	
	
	static java.util.Calendar startCalendar = java.util.Calendar.getInstance();
	static java.util.Calendar endCalendar;
	
	static int duration = startCalendar.WEEK_OF_YEAR;
	static int dayBit = 0;
	static int day_offset = 0;
	static int currentBit = 0;
	static int goToBit = 0;
	static int searchBit = 0;

	// make constructor private for single instance control
	private MyEventList() {
		eventsAL = new ArrayList<Appointment>();
		maxId = -1;
	}

	// deal out the singleton
	public static MyEventList getInstance(Context ctx) 
	throws IOException {
		if (eventList == null) {
			eventList = new MyEventList();
			eventList.ctx = ctx;
			eventList.load();
		}
		return eventList;
	}

	public Appointment add(Appointment appt) throws IOException {
		// set id to next available
		maxId++;
		appt.id = maxId;
		addEvent(appt);
		return appt;
	}

	private void addEvent(Appointment appt) throws IOException {
		// if this is the first add, it's easy
		if (eventsAL.size() == 0) {
			eventsAL.add(appt);
			return;
		}

		// search in array list and add at correct spot
		int lo=0, hi=eventsAL.size()-1, mid=-1, c=0;
		while (lo <= hi) {
			mid = (lo+hi)/2;
			c = appt.compareTo(eventsAL.get(mid));
			if (c == 0) {  // duplicate name
				break;
			}
			if (c < 0) {
				hi = mid-1;
			} else {
				lo = mid+1;
			}
		}
		int pos = c <= 0 ? mid : mid+1;
		// insert at pos
		eventsAL.add(pos,appt);
		// write through
		store();
	}

	public int getPos(Appointment appt) {
		if (eventsAL.size() == 0) {
			return -1;
		}

		// search in array list and add at correct spot
		int lo=0, hi=eventsAL.size()-1;

		while (lo <= hi) {
			int mid = (lo+hi)/2;
			Appointment midA = eventsAL.get(mid);
			int c = appt.compareTo(midA);
			if (c == 0) {  // need to compare id
				if (appt.id == midA.id) {
					return mid;
				}
				// check left
				int i=mid-1;
				while (i >= 0) {
					midA = eventsAL.get(i);
					if (appt.compareTo(midA) == 0 && appt.id == midA.id) {
						return i;
					}
					i--;
				}
				// check right
				i = mid+1;
				while (i < eventsAL.size()) {
					midA = eventsAL.get(i);
					if (appt.compareTo(midA) == 0 && appt.id == midA.id) {
						return i;
					}
					i++;
				}
				return -1;
			}
			if (c < 0) {
				hi = mid-1;
			} else {
				lo = mid+1;
			}
		}
		return -1;
	}

	public ArrayList<Appointment> getEvents() 
	{
	//	Log.v("year", startCalendar.get(startCalendar.YEAR)+"");
	//	Log.v("month", startCalendar.get(startCalendar.MONTH)+"");
	//	Log.v("date", startCalendar.get(startCalendar.DATE)+"");
		
		if(searchBit == 1)
		{
			return EAL;
			
		}
	
		if(goToBit == 1)
		{
			startCalendar.set(java.util.Calendar.MILLISECOND, 0);
			endCalendar = (java.util.Calendar)startCalendar.clone();
			endCalendar.set(java.util.Calendar.HOUR, 23);
			endCalendar.set(java.util.Calendar.MINUTE, 59);
			endCalendar.set(java.util.Calendar.SECOND, 59);
			endCalendar.set(java.util.Calendar.MILLISECOND, 0);
			EAL = getApptInRange(startCalendar, endCalendar);
			currentBit = 0;
			return EAL;
			
		}
		
		if(currentBit == 1)
		{
			startCalendar = java.util.Calendar.getInstance();
		}
		startCalendar.set(java.util.Calendar.DAY_OF_WEEK, startCalendar.getFirstDayOfWeek());
		startCalendar.set(java.util.Calendar.MILLISECOND, 0);
		endCalendar = (java.util.Calendar)startCalendar.clone();
		
		
		setNextWeek(endCalendar);
		
	/*	if(dayBit ==1)
		{
			Log.v("start is ", Appointment.dateToString(startCalendar));
			Log.v("end is ", Appointment.dateToString(endCalendar));
		}
	*/	
		endCalendar.set(java.util.Calendar.MILLISECOND, 0);
		endCalendar.add(Calendar.DATE, -1);
		Log.v(" getEvents startDate", Appointment.dateToString(startCalendar));
		Log.v("getEvents endDate", Appointment.dateToString(endCalendar));


		EAL = getApptInRange(startCalendar, endCalendar);
		currentBit = 0;
		return EAL;

	}

	
	
	public void remove(Appointment appt) throws NoSuchElementException, IOException {
	/*	int pos = getPos(appt);
		if (pos == -1) {
			throw new NoSuchElementException();
		}*/
		for( int i = 0; i < eventsAL.size(); i++)
		{
			if(eventsAL.get(i).id == appt.id)
			{
				eventsAL.remove(i);
			}
		}
		
		// write through
		store();
	}

	public void update(Appointment appt) throws NoSuchElementException, IOException {
		// since name could be updated, best to sequentially search on id
		for (int i=0; i < eventsAL.size(); i++) {
			if (eventsAL.get(i).id == appt.id) {
				eventsAL.set(i, appt);  // BUT THIS MIGHT CHANGE THE ALPHABETICAL ORDER!
				if ((i > 0 && appt.compareTo(eventsAL.get(i-1)) < 0) ||
						(i < eventsAL.size()-1 && appt.compareTo(eventsAL.get(i+1)) > 0)) {
					eventsAL.remove(i);
					addEvent(appt);  // so new id is not created, call private helper add
				} else {
					// write through
					store();
				}
				return;
			} 
		}
		throw new NoSuchElementException();
	}

	public void load() throws IOException {
		int maxId = -1;
		try {
			FileInputStream fis = ctx.openFileInput(EVENTS_FILE);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String eventInfo;
			while ((eventInfo = br.readLine()) != null) {
				Appointment appt = Appointment.getInstance(eventInfo);
				eventsAL.add(appt);
				if (appt.id > maxId) {
					maxId = appt.id;
				}
			}
			this.maxId = maxId; 
			fis.close(); 
		} catch (FileNotFoundException e) {
			loadRaw();
		}
	}

	public void store() throws IOException {
		FileOutputStream fos = ctx.openFileOutput(EVENTS_FILE, Context.MODE_PRIVATE);
		PrintWriter pw = new PrintWriter(fos);

		for (int i=0; i < eventsAL.size(); i++) {
			pw.println(eventsAL.get(i).getString());
		}
		pw.close();

	}

	private void loadRaw() {
		try {
			Resources resources = ctx.getResources();
			InputStream inputStream = resources.openRawResource(R.raw.events);
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			String eventInfo;
			while ((eventInfo = br.readLine()) != null) {
				Appointment event = Appointment.getInstance(eventInfo);
				add(event);  // in alphabetical order
			}
			inputStream.close(); 
		} catch (Exception e) {

		}
	}


	
	public static void setNextWeek(java.util.Calendar c)
	{
		c.add(duration, 1);
	}

	public static void setNextWeek()
	{
		searchBit = 0;
		dayBit = 0;
		goToBit = 0;
		duration = startCalendar.WEEK_OF_YEAR;
		setNextWeek(startCalendar);
		
		
	}
	
	public static void setPreviousWeek(java.util.Calendar c)
	{
		c.add(duration, -1);
	}

	public static void setPreviousWeek()
	{
		searchBit = 0;
		dayBit = 0;
		goToBit = 0;
		duration = startCalendar.WEEK_OF_YEAR;
		setPreviousWeek(startCalendar);
	}
	

	public static void setDayOfWeek(int d) 
	{
		startCalendar.set(java.util.Calendar.DAY_OF_WEEK, d);
		duration = java.util.Calendar.DATE;
		dayBit = 1;
		day_offset = d-1;
		searchBit = 0;
		
	}

	public static void setCurrentWeek() 
	{
		
		duration = java.util.Calendar.WEEK_OF_YEAR;
		dayBit = 0;		
		goToBit = 0;
		currentBit = 1;
		searchBit = 0;

		
	}
	

	public static void goToDate(Calendar c) 
	{
		startCalendar.set(c.get(java.util.Calendar.YEAR), c.get(java.util.Calendar.MONTH), c.get(java.util.Calendar.DATE),0,0,0);
		
		duration = java.util.Calendar.DATE;
		goToBit = 1;
		currentBit = 0;
		dayBit = 0;		
		searchBit = 0;


		
	}

	public static String getTitle() 
	{
		String s;
		
		java.util.Calendar end = (java.util.Calendar) endCalendar.clone();
		//end.add(java.util.Calendar.DATE, -1);
		
		s= "Week of " + Appointment.dateToString(startCalendar) + " to " + Appointment.dateToString(end);
			
		if(goToBit == 1)
		{
			s= Appointment.dateToString(startCalendar);
		}
		else if(dayBit == 1)
		{
			Calendar start = (Calendar) startCalendar.clone();
			start.add(Calendar.DATE, day_offset);
			
			String dStr = null;
			int d = start.get(Calendar.DAY_OF_WEEK);
			
			switch(d)
			{
				case 1:
					dStr = "Sunday";
					break;
				case 2:
					dStr = "Monday";
					break;

				case 3:
					dStr = "Tuesday";
					break;

				case 4:
					dStr = "Wednesday";
					break;

				case 5:
					dStr = "Thursday";
					break;

				case 6:
					dStr = "Friday";
					break;

				case 7:
					dStr = "Saturday";
					break;

				default:
					dStr = "Unknown";
					break;

			
			}

			s = dStr + " " + Appointment.dateToString(start);				
		}


		
		return s;
	}

	public static ArrayList<Appointment> getOriginalEvents() {
		return eventsAL;
	}

	public static ArrayList<Appointment> getApptInRange(java.util.Calendar start, java.util.Calendar end) {
		ArrayList<Appointment> apptsInRange = new ArrayList<Appointment>();
		for(int i = 0; i < eventsAL.size(); i++)
		{
			Appointment a = eventsAL.get(i);
			Log.v("appointment "+i + " ", a.getString());

			
			java.util.Calendar s = new GregorianCalendar(start.get(Calendar.YEAR), start.get(Calendar.MONTH), start.get(Calendar.DATE), 0, 0, 0);
			s.set(Calendar.MILLISECOND, 0);
			java.util.Calendar e = new GregorianCalendar(end.get(Calendar.YEAR), end.get(Calendar.MONTH), end.get(Calendar.DATE), 0, 0, 0);
			s.set(Calendar.MILLISECOND, 0);
			
			if(dayBit == 1)
			{
				Log.v("asdfasdfads", "day bit is 1");

				s.add(duration, day_offset);
				
				e = (Calendar) s.clone();
				e.set(Calendar.HOUR, 23);
				e.set(Calendar.MINUTE, 59);
				e.set(Calendar.SECOND, 59);
				e.set(Calendar.MILLISECOND, 0);


			}
			
			java.util.Calendar as = new GregorianCalendar(a.getStartDate().get(Calendar.YEAR), a.getStartDate().get(Calendar.MONTH), a.getStartDate().get(Calendar.DATE), 0, 0, 0);
			as.set(Calendar.MILLISECOND, 0);
			java.util.Calendar ae = new GregorianCalendar(a.getEndDate().get(Calendar.YEAR), a.getEndDate().get(Calendar.MONTH), a.getEndDate().get(Calendar.DATE), 0, 0, 0);
			ae.set(Calendar.MILLISECOND, 0);
			
			if( ((as.compareTo(s) >= 0) && (as.compareTo(e) <= 0))
			|| ((ae.compareTo(s) >= 0) && (ae.compareTo(e) <= 0)) ) 
			{
				Log.v("adding appointment "+i + " ", a.getString());

				apptsInRange.add(a);
			}			
			/*	
			int syear = start.get(java.util.Calendar.YEAR);
			int sdate = start.get(java.util.Calendar.DATE);
			int smonth = start.get(java.util.Calendar.MONTH);
			
			int eyear = end.get(java.util.Calendar.YEAR);
			int edate = end.get(java.util.Calendar.DATE);
			int emonth = end.get(java.util.Calendar.MONTH);
			
			int asyear = a.getStartDate().get(java.util.Calendar.YEAR);
			int asmonth = a.getStartDate().get(java.util.Calendar.MONTH);
			int asdate = a.getStartDate().get(java.util.Calendar.DATE);

			int aeyear = a.getEndDate().get(java.util.Calendar.YEAR);
			int aemonth = a.getEndDate().get(java.util.Calendar.MONTH);
			int aedate = a.getEndDate().get(java.util.Calendar.DATE);

			Log.v("syear ", ""+syear);
			Log.v("smonth ", ""+smonth);
			Log.v("sdate ", ""+sdate);
			Log.v("eyear ", ""+eyear);
			Log.v("emonth ", ""+emonth);
			Log.v("edate ", ""+emonth);
			Log.v("asyear ", ""+asyear);
			Log.v("asmonth ", ""+asmonth);
			Log.v("asdate ", ""+asdate);
			Log.v("aeyear ", ""+aeyear);
			Log.v("aemonth ", ""+aemonth);
			Log.v("aedate ", ""+aedate);

			
			if(asyear >= syear && asmonth >= smonth && asdate >= sdate
			   &&
			   aeyear <= eyear && aemonth <= emonth && aedate <= edate)
			{
				apptsInRange.add(a);
			}
*/


		
		}
		Log.v("MyList", "myList still has "+ eventsAL.size());
		Log.v("this week events", apptsInRange.toString());


		return apptsInRange;
	}
	
	public ArrayList<Appointment> search(String query) 
	{
		ArrayList<Appointment> al = new ArrayList<Appointment>();
		
		for(int i = 0; i < eventsAL.size(); i++)
		{
			String lowerloc = eventsAL.get(i).getLocation().toLowerCase();
			String lowerqy = query.toLowerCase();
			
			if(lowerloc.startsWith(lowerqy))
			{
				al.add(eventsAL.get(i));
			}
		}
		
		return al;
		
	/*	int lo=0, hi=eventsAL.size()-1;
		int[] extent;
		String event = query.toLowerCase();
		while (lo <= hi) {
			int mid=(lo+hi)/2;
			if (eventsAL.get(mid).getLocation().toLowerCase().startsWith(event)) {
				// need to scan left and right until no more matches
				extent = new int[2];
				extent[0] = mid;
				// scan left
				while (extent[0] > 0) {
					if (eventsAL.get(extent[0]-1).getLocation().toLowerCase().startsWith(
							event)) {
						extent[0]--;
					} else {
						break;
					}
				}
				// scan right
				extent[1] = mid;
				while (extent[1] < eventsAL.size()-1) {
					if (eventsAL.get(extent[1]+1).getLocation().toLowerCase().startsWith(
							event)) {
						extent[1]++;
					} else {
						break;
					}
				}
				return extent;
			} 
			// mid does not start with the given name, go left or right
			int c = query.compareToIgnoreCase(eventsAL.get(mid).getLocation());
			if (c < 0) { 
				hi = mid-1;
			} else {
				lo = mid+1;
			}
		}

		return null;
		*/
	}

	public static void setSearchResult(ArrayList<Appointment> extent) 
	{

		EAL = extent;
		searchBit = 1;
		
	}
	
}














