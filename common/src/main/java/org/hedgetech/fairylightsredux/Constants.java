package org.hedgetech.fairylightsredux;

import org.hedgetech.fairylightsredux.util.CalendarEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Month;

public class Constants {
	public static final String MOD_ID = "fairylightsredux";
	public static final String MOD_NAME = "Fairy Lights Redux";
	public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

	// Calendar Events
	public static final CalendarEvent CHRISTMAS = new CalendarEvent(Month.DECEMBER, 24, 26);
	public static final CalendarEvent HALLOWEEN = new CalendarEvent(Month.OCTOBER, 31, 31);
}