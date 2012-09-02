package com.examples.calendar;


import java.util.ArrayList;
import java.util.zip.DataFormatException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

import com.examples.control.DataParser;
import com.examples.model.Appointment;

public class Calendar extends Activity implements OnDateSetListener 
{

	public static final int ADD_EVENT_ACTIVITY = 0;
	public static final int UPDATE_EVENT_ACTIVITY = 1;

	public static final String EVENT_ID = "id";

	public static final int LOAD_ERROR = 1;	
	public static final int SEARCH_EMPTY = 2;
	public static final int DATE_PICKER = 3;



	public static final String LOAD_ERROR_MESSAGE = "Could not load events";


	MyEventList mylist;
	ListView listView;
	
	TextView title;


	// following group of fields goes with the search functionality
	private String query;
	// position in global event list of first item in result list of search matches
	private int searchListStartPos;
	// last picked event so main list can be shown with this as first item
	private static int lastPickedIndex=0; 

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_list);

		title = (TextView)findViewById(R.id.title);
		
		listView = (ListView)findViewById(R.id.event_list);

		try {
			mylist = MyEventList.getInstance(getApplicationContext());
		} catch (Exception e) {}

		listView.setAdapter(new ArrayAdapter<Appointment>(this, R.layout.event, mylist.getEvents()));
		ImageView addEventIcon = (ImageView)findViewById(R.id.add_event_icon);

		addEventIcon.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {

				addEvent();

				Toast.makeText(getApplicationContext(), "Adding Event!", Toast.LENGTH_SHORT).show();
				Log.v("Testing","showEvent =" + LOAD_ERROR);

			}
		});

		// register list view for context menu so users can see info
		registerForContextMenu(listView);

		// call handle intent to resolve
		handleIntent(getIntent());        
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent);
	}

	private void handleIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) 
		{
			String query = intent.getStringExtra(SearchManager.QUERY);
			showSearchResults(query);
		} else {
			showEventList();
		}
	}

	// this is to show the main event list
	private void showEventList() {
		
		
		// show event list 
		listView.setAdapter(new ArrayAdapter<Appointment>(this, R.layout.search_event, mylist.getEvents()));
		// starting with last picked
		listView.setSelection(lastPickedIndex);
		title.setText(MyEventList.getTitle());

	}

	// to show matches on search
	private void showSearchResults(String query) {
		// do the search and get range of events that start with query
		ArrayList<Appointment> extent = mylist.search(query);
		// if no matches, show dialog
		if (extent.size() == 0) {
			//mTextView.setText(getString(R.string.no_results, new Object[] {query}));
			// show dialog to request add
			this.query = query;
			showDialog(SEARCH_EMPTY);
			MyEventList.setCurrentWeek();
			showEventList();
			return;
		}
		
		MyEventList.setSearchResult(extent);
		showEventList();
		//listView.setAdapter(new ArrayAdapter<Appointment>(this, R.layout.event, extent));
		title.setText("Search");

		/*
		// show all matches
		int count = extent[1] - extent[0] + 1;
		String[] matches = new String[count]; 
		ArrayList<Appointment> appts = mylist.getEvents();
		for (int i=0; i < matches.length; i++) {
			matches[i] = appts.get(extent[0]+i).getLocation();
		}
		// set index in main event list of first item in match list 
		searchListStartPos = extent[0];
		// set up to show match list
		listView.setAdapter(new ArrayAdapter<String>(this, R.layout.event, matches));
		// set up for selection
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {


				// store index for use by main list view
				lastPickedIndex = Calendar.this.searchListStartPos+position;
				showEventList();
			}
		});
		*/
	}


	void addEvent() 
	{
		Intent intent = new Intent(this, AddEvent.class);
		startActivityForResult(intent, ADD_EVENT_ACTIVITY);

	} 


	void showEvent(int index) 
	{


		Intent intent = new Intent(this, AddEvent.class);
//		 pass event data as bundle
		Appointment appt = mylist.getEvents().get(index);
		Bundle bundle = new Bundle();
		bundle.putInt(EVENT_ID, appt.id);

		Toast.makeText(getApplicationContext(), "still good", Toast.LENGTH_LONG).show();

		bundle.putIntArray(AddEvent.START_CAL, AddEvent.makeArray(appt.getStartDate()));
		bundle.putIntArray(AddEvent.END_CAL, AddEvent.makeArray(appt.getEndDate()));


		bundle.putString(AddEvent.START_DATE, appt.dateToString(appt.getStartDate()));
		bundle.putString(AddEvent.START_TIME, appt.timeToString(appt.getStartDate()));
		bundle.putString(AddEvent.END_DATE, appt.dateToString(appt.getEndDate()));
		bundle.putString(AddEvent.END_TIME, appt.timeToString(appt.getEndDate()));
		bundle.putString(AddEvent.DESCRIPTION, appt.getDescription());
		bundle.putString(AddEvent.LOCATION, appt.getLocation());


		/* from checking the bundle in addEvent
		oldStartC = makeCal(bundle.getIntArray(START_CAL));
    	oldEndC = makeCal(bundle.getIntArray(END_CAL));
    	//oldLocation = bundle.getString(LOCATION);        
    	//oldDescription = bundle.getString(DESCRIPTION);

    	startDate.setText(bundle.getString(START_DATE));
    	startTime.setText(bundle.getString(START_TIME));
    	endDate.setText(bundle.getString(END_DATE));
    	endTime.setText(bundle.getString(END_TIME));
    	description.setText(bundle.getString(DESCRIPTION));
    	location.setText(bundle.getString(LOCATION));

		 */

		intent.putExtras(bundle);
		startActivityForResult(intent, UPDATE_EVENT_ACTIVITY);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		// if the result code is not ok, then don't do anything 
		if (resultCode != RESULT_OK) {
			return;
		}

		Toast.makeText(getApplicationContext(), "Getting result", Toast.LENGTH_SHORT);

		Bundle extras = intent.getExtras();
		if (extras == null) {
			return;
		}

		int id=-1;
		if (requestCode == UPDATE_EVENT_ACTIVITY) {
			id = extras.getInt(EVENT_ID);
		}

		//MM/DD/YYYY-HH:MM



		java.util.Calendar s = null;
		java.util.Calendar e = null;
		try {
			//s = (java.util.Calendar) DataParser.parseCalendar(AddEvent.START_DATE.toString() + "-" + AddEvent.START_TIME);
			//e = (java.util.Calendar) DataParser.parseCalendar(AddEvent.END_DATE + "-" + AddEvent.END_TIME);
			s = (java.util.Calendar) DataParser.parseCalendar(AddEvent.getStartCalString());
			e = (java.util.Calendar) DataParser.parseCalendar(AddEvent.getEndCalString());


		} catch (DataFormatException e1) 
		{

		}

		Appointment appt = new Appointment(id,
				s,
				e,
				extras.getString(AddEvent.DESCRIPTION),
				extras.getString(AddEvent.LOCATION)
		);
		//Toast.makeText(getApplicationContext(), event.getString(), Toast.LENGTH_LONG).show();

		try {
			if (requestCode == ADD_EVENT_ACTIVITY) {
				appt = mylist.add(appt);  // event comes back with id
			} else {
				mylist.update(appt);
			}
			MyEventList.setCurrentWeek();
			showEventList();
			//listView.setAdapter(new ArrayAdapter<Appointment>(this, R.layout.event, mylist.getEvents()));
		} catch (Exception ex) 
		{

		}

		
	}


	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.event_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		int eventIndex = (int)info.id;
		switch (item.getItemId()) {
		case R.id.event_context_show:    


			showEvent(eventIndex);

			return true;
		case R.id.event_context_delete:
			try {
				Log.v("mylist.getEvents" + " ", mylist.getEvents().toString());

				mylist.remove(mylist.getEvents().get(eventIndex));
				// refit the list view with new adapter from event list
				
			//	listView.setAdapter(new ArrayAdapter<Appointment>(this, R.layout.event, 
			//			mylist.getEvents()));
				
				MyEventList.setCurrentWeek();
				showEventList();
				return true;
			} catch (Exception e) {
				return false;
			}
		default:
			return super.onContextItemSelected(item);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch(id) {
		case LOAD_ERROR:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(LOAD_ERROR_MESSAGE)
			.setTitle("Error")
			.setCancelable(false)
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {

				}
			});
			return builder.create();
		case SEARCH_EMPTY: 
			builder = new AlertDialog.Builder(this);
			builder.setMessage(getString(R.string.no_results, new Object[] {query}))
			.setCancelable(false)
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {

				}
			});
			return builder.create();
			
		case DATE_PICKER:
			java.util.Calendar cal = java.util.Calendar.getInstance();
			return new DatePickerDialog(this, this, cal.get(java.util.Calendar.YEAR), cal.get(java.util.Calendar.MONTH), cal.get(java.util.Calendar.DATE));
	
		}
		return null;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch(id) {
		case SEARCH_EMPTY:
			((AlertDialog)dialog).setMessage(
					getString(R.string.no_results, new Object[] {query}));
			return;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{

			
			case R.id.previous_week:
				MyEventList.setPreviousWeek();
				showEventList();
				return true;
			case R.id.current_week:
				MyEventList.setCurrentWeek();
				showEventList();
				return true;
			case R.id.next_week:
				MyEventList.setNextWeek();
				showEventList();
				return true;
			case R.id.choose_a_day:
				return true;
			case R.id.go_to_date:
				showDialog(DATE_PICKER);
				return true;

			case R.id.sunday:
				MyEventList.setDayOfWeek(java.util.Calendar.SUNDAY);
				showEventList();
				return true;
			case R.id.monday:
				MyEventList.setDayOfWeek(java.util.Calendar.MONDAY);
				showEventList();
				return true;
			case R.id.tuesday:
				MyEventList.setDayOfWeek(java.util.Calendar.TUESDAY);
				showEventList();
				return true;
			case R.id.wednesday:
				MyEventList.setDayOfWeek(java.util.Calendar.WEDNESDAY);
				showEventList();
				return true;
			case R.id.thursday:
				MyEventList.setDayOfWeek(java.util.Calendar.THURSDAY);
				showEventList();
				return true;
			case R.id.friday:
				MyEventList.setDayOfWeek(java.util.Calendar.FRIDAY);
				showEventList();
				return true;
			case R.id.saturday:
				MyEventList.setDayOfWeek(java.util.Calendar.SATURDAY);
				showEventList();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) 
	{
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.set(java.util.Calendar.YEAR, year);
		c.set(java.util.Calendar.MONTH, monthOfYear);
		c.set(java.util.Calendar.DATE, dayOfMonth);
		

		MyEventList.goToDate(c);
		showEventList();

		
	}




}