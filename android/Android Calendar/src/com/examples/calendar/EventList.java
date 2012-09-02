package com.examples.calendar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import com.examples.model.Appointment;

import android.content.Context;

public interface EventList {
	void load() throws IOException;
	void store() throws IOException;
	ArrayList<Appointment> getEvents();
	Appointment add(Appointment e) throws IOException;
	void update(Appointment e) throws NoSuchElementException, IOException;
	void remove(Appointment e) throws NoSuchElementException, IOException;
	int getPos(Appointment e);
}
