
package com.examples.calendar;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.zip.DataFormatException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.examples.control.DataParser;
import com.examples.model.Appointment;

public class AddEvent extends Activity implements OnDateSetListener, OnTimeSetListener {

	public static final String OLD_START_CAL = "old_start_cal";
	public static final String OLD_END_CAL = "old_end_cal";
	public static final String OLD_DESCRIPTION = "old_description";
	public static final String OLD_LOCATION = "old_location";
	public static final String START_CAL = "start_cal";
	public static final String END_CAL = "end_cal";
	public static final String START_DATE = "start_date";
	public static final String START_TIME = "start_time";
	public static final String END_DATE = "end_date";
	public static final String END_TIME = "end_time";
	public static final String DESCRIPTION = "description";
	public static final String LOCATION = "location";

	public static final int INCOMPLETE_INPUT = 1;
	public static final int CHOOSE_START_DATE = 2;
	public static final int CHOOSE_START_TIME = 3;
	public static final int CHOOSE_END_TIME = 5;

	String incompleteMessage;

	java.util.Calendar startC;
	java.util.Calendar endC;
	java.util.Calendar oldStartC;
	java.util.Calendar oldEndC;
	String oldDescription;
	String oldLocation;

	Button eventSave;
	Button eventCancel;
	Button chooseStartDate;
	Button chooseStartTime;
	Button chooseEndTime;
	static TextView startDate;
	static TextView startTime;
	static TextView endDate;
	static TextView endTime;
	EditText description;
	EditText location;




	int id; // event id, used for update
	int prevDialog;

	// Called when the activity is first created. 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_event);


		// get view elements
		eventSave = (Button)findViewById(R.id.event_save);
		eventCancel = (Button)findViewById(R.id.event_cancel);
		chooseStartDate = (Button)findViewById(R.id.start_date_button);
		chooseStartTime = (Button)findViewById(R.id.start_time_button);
		chooseEndTime = (Button)findViewById(R.id.end_time_button);
		startDate = (TextView)findViewById(R.id.start_date);
		startTime = (TextView)findViewById(R.id.start_time);
		endDate = (TextView)findViewById(R.id.end_date);
		endTime = (TextView)findViewById(R.id.end_time);
		description = (EditText)findViewById(R.id.description);
		location = (EditText)findViewById(R.id.location);

		// if bundle was passed in, this means event is shown for possible editing
		// fill in the fields
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) 
		{


			id = bundle.getInt(Calendar.EVENT_ID);

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

			startC = (java.util.Calendar)oldStartC.clone();
			endC = (java.util.Calendar)oldEndC.clone();

			Toast.makeText(getApplicationContext(), "after geting all data from bundle", Toast.LENGTH_SHORT).show();

		}
		else
		{
			startC = java.util.Calendar.getInstance();
			startC.set(startC.SECOND,0);
			startC.set(startC.MILLISECOND,0);
			endC = (java.util.Calendar)startC.clone();
		}





		// attach listeners to the buttons
		eventSave.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {

				// check that if all the information have filled in
				String sd = startDate.getText().toString();
				String st = startTime.getText().toString();
				String ed = endDate.getText().toString();
				String et = endTime.getText().toString();
				String de = description.getText().toString();
				String lo = location.getText().toString();

				java.util.Calendar scal = null;
				java.util.Calendar ecal = null;


				boolean ok=true;

				if(sd.equals("") || st.equals("") ||
						ed.equals("") || et.equals("") ||
						de.equals("") || lo.equals("") )
				{
					//Toast.makeText(getApplicationContext(), "Not complete!", Toast.LENGTH_SHORT).show();
					incompleteMessage = "Please fill out all the fields.";

					ok = false;
				}
				if(ok)
				{
					if(de.contains("|") || lo.contains("|"))
					{
						incompleteMessage = "Please do not use the character | ! It will crash the application.";

						ok = false;
					}
					
					
				}
				if(ok)
				{
					String sdate = startDate.getText().toString() +  "-" + startTime.getText().toString();
					String edate = endDate.getText().toString() +  "-" + endTime.getText().toString();


					try {
						scal = DataParser.parseCalendar(sdate);
						ecal = DataParser.parseCalendar(edate);

					} catch (DataFormatException e) 
					{
						incompleteMessage = "No events earlier than April 5th 2011.";
						ok = false;

					}

				}

				if(ok)
				{
					ArrayList<Appointment> al = MyEventList.getOriginalEvents();
					for(int i = 0; i< al.size(); i++)
					{
						Appointment a = al.get(i);
						if(a.getStartDate().compareTo(scal) == 0 
								&& a.getEndDate().compareTo(ecal) == 0
								&& a.getDescription().equals(de)
								&& a.getLocation().equals(lo)
						)
						{
							incompleteMessage = "Duplicate Events. Please modify again.";
							ok = false;
						}
					}
				}

				if(ok)
				{
					if(ecal.compareTo(scal) < 0)
					{
						incompleteMessage = "Invalid time. Please modify again.";
						ok = false;

					}
				}


				if (!ok) {
					showDialog(INCOMPLETE_INPUT);
					return;
				}
				Toast.makeText(getApplicationContext(), "All fields are filled.", Toast.LENGTH_SHORT).show();

				// get all the text fields and put them in a Bundle
				Bundle bundle = new Bundle();
				bundle.putInt(Calendar.EVENT_ID,id);
				bundle.putIntArray(OLD_START_CAL,makeArray(oldStartC));
				bundle.putIntArray(OLD_END_CAL,makeArray(oldEndC));
				bundle.putString(OLD_DESCRIPTION, oldDescription);
				bundle.putString(OLD_LOCATION, oldLocation);
				bundle.putIntArray(START_CAL,makeArray(startC));

				bundle.putString(START_DATE, startDate.getText().toString());
				bundle.putString(START_TIME, startTime.getText().toString());
				bundle.putString(END_DATE, endDate.getText().toString());
				bundle.putString(END_TIME, endTime.getText().toString());
				bundle.putString(DESCRIPTION, description.getText().toString());
				bundle.putString(LOCATION, location.getText().toString());

				Intent intent = new Intent();
				intent.putExtras(bundle);
				setResult(RESULT_OK,intent);

				finish();
			}
		});

		eventCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});

		chooseStartDate.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				showDialog(CHOOSE_START_DATE);

			}
		});   
		chooseStartTime.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				showDialog(CHOOSE_START_TIME);

			}
		});    

		chooseEndTime.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				showDialog(CHOOSE_END_TIME);

			}
		});    

		//MISSING UPDATEDATETIME

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch(id) {
		case INCOMPLETE_INPUT:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(incompleteMessage)
			.setCancelable(false)
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {

				}
			});
			return builder.create();

		case CHOOSE_START_DATE:
			return new DatePickerDialog(this, this, startC.get(java.util.Calendar.YEAR), startC.get(java.util.Calendar.MONTH), startC.get(java.util.Calendar.DATE));
		case CHOOSE_START_TIME:
			return new TimePickerDialog(this, this, startC.get(java.util.Calendar.HOUR_OF_DAY), startC.get(java.util.Calendar.MINUTE), false);

		case CHOOSE_END_TIME:
			return new TimePickerDialog(this, this, endC.get(java.util.Calendar.HOUR_OF_DAY), endC.get(java.util.Calendar.MINUTE), false);	

		}
		return null;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		prevDialog = id;

		switch(id) {
		case INCOMPLETE_INPUT:
			((AlertDialog)dialog).setMessage(incompleteMessage);
			break;
		case CHOOSE_START_DATE:
			((DatePickerDialog) dialog).updateDate(startC.get(java.util.Calendar.YEAR), startC.get(java.util.Calendar.MONTH), startC.get(java.util.Calendar.DATE));
			break;

		case CHOOSE_START_TIME:
			((TimePickerDialog) dialog).updateTime(startC.get(java.util.Calendar.HOUR), startC.get(java.util.Calendar.MINUTE));
			break;
		case CHOOSE_END_TIME:
			((TimePickerDialog) dialog).updateTime(endC.get(java.util.Calendar.HOUR), endC.get(java.util.Calendar.MINUTE));
			break;
		}
	}

	//   @Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) 
	{
		int month = monthOfYear+1;;
		String m;
		String d;

		if(month < 10)
		{
			m = "" + "0" + month;
		}
		else
		{
			m = month + "";
		}

		if(dayOfMonth < 10)
		{
			d = "" + "0" + dayOfMonth;
		}
		else
		{
			d = dayOfMonth + "";
		}


		switch (prevDialog) {
		case CHOOSE_START_DATE:
			startC.set(startC.YEAR, year);
			startC.set(startC.MONTH, monthOfYear);
			startC.set(startC.DATE, dayOfMonth);
			startDate.setText("" + m + "/"+d+"/"+year);
			endC.set(endC.YEAR, year);
			endC.set(endC.MONTH, monthOfYear);
			endC.set(endC.DATE, dayOfMonth);
			endDate.setText("" + m + "/"+d+"/"+year);

			break;
		default:
		}				
	}

	//	@Override
	public void onTimeSet(TimePicker view, int hour, int minute) {

		String h;
		String m;
		if(hour < 10)
		{
			h = "" + "0" + hour;
		}
		else
		{
			h = "" + hour;
		}
		if(minute < 10)
		{
			m = "" + "0" + minute;
		}
		else
		{
			m = "" + minute;
		}

		switch (prevDialog) {
		case CHOOSE_START_TIME:
			startC.set(startC.HOUR_OF_DAY, hour);
			startC.set(startC.MINUTE, minute);
			startTime.setText("" + h + ":"+m);

			break;
		case CHOOSE_END_TIME:
			endC.set(endC.HOUR_OF_DAY, hour);
			endC.set(endC.MINUTE, minute);
			endTime.setText("" + h + ":"+m);

			break;
		default:
		}		
	}

	public static int[] makeArray(java.util.Calendar c) {

		if (c == null)
		{
			return null;
		}
		else
		{
			int[] array = new int[5];
			array[0] = c.get(c.YEAR);
			array[1] = c.get(c.MONTH);
			array[2] = c.get(c.DATE);
			array[3] = c.get(c.HOUR_OF_DAY);
			array[4] = c.get(c.MINUTE);
			return array;

		}
	}

	public static java.util.Calendar makeCal(int[] array) {
		if (array == null || array.length != 5)
		{
			return null;
		}
		else
		{

			java.util.Calendar c = new GregorianCalendar(array[0],array[1], array[2], array[3], array[4], 0);
			c.set(c.MILLISECOND,0);
			return c;

		}


	}

	public static String getStartCalString()
	{
		return startDate.getText() + "-" + startTime.getText();

	}

	public static String getEndCalString()
	{
		return endDate.getText() + "-" + endTime.getText();

	}



}
