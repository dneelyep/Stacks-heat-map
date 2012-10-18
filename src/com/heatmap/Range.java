package com.heatmap;

import java.awt.Color;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JLabel;

import org.joda.time.DateTime;
import org.joda.time.Days;

/** Class to represent a single range of books in the library. */
public class Range extends JLabel {
	/** The first call number in this Range. */
	private String startCallNumber;
	
	/** The last call number in this Range. */
	private String endCallNumber;

	/** The day this Range was last checked for cleanliness. */
	private Date dayLastChecked;
	
	/** This Range's x-coordinate in the GUI. */
	private final int XCOORD;
	
	/** This Range's y-coordinate in the GUI. */
	private final int YCOORD;
	
	/** The color of this Range's text in the GUI. */
	private Color textColor;
	
	/** Create a new Range with the given x-coordinate x and y-coordinate y. */
	public Range(int x, int y) {
		XCOORD = x;
		YCOORD = y;
		setText("Range (" + x + "," + y + ")");
	}
	
	/** Get this Range's x-coordinate. */
	public int getXCoord() {
		return XCOORD;
	}
	
	/** Get this Range's y-coordinate. */
	public int getYCoord() {
		return YCOORD;		
	}
	
	/** Get this Range's starting call number. */
	public String getStart() {
		return startCallNumber;
	}
	
	/** Get this Range's ending call number. */
	public String getEnd() {
		return endCallNumber;
	}
	
	/** Set this Range's start call number to a new value start. */
	public void setStart(String start) {
		startCallNumber = start;
	}
	
	/** Set this Range's end call number to a new value end. */
	public void setEnd(String end) {
		endCallNumber = end;
	}

	/** Set the Date this range was last checked to a new Date d. */
	public void setDayLastChecked(Date d) {
		dayLastChecked = d;
	}

	/** Get the Date that this Range was last checked for cleanliness. */
	// TODO Make less confusing names for these methods.
	public Date getDayLastChecked() {
		return dayLastChecked;
	}
	
	/** Get the number of days since this Range was last checked 
	 * for cleanliness. */
	public int getDaysSinceChecked() {
		Calendar c = Calendar.getInstance();
		Date today = c.getTime();
		int daysSinceChecked = Days.daysBetween(new DateTime(dayLastChecked), new DateTime(today)).getDays();
		return daysSinceChecked;
	}
	
	/** Get the current text Color associated with this Range. */
	public Color getColor() {
		return textColor;
	}
	
	/** Re-set the color for this Range. */
	public void updateColor() {
		if (getDaysSinceChecked() < 15) {
			textColor = Color.green;
		}
		else if (getDaysSinceChecked() >= 15 && getDaysSinceChecked() < 30) {
			textColor = Color.YELLOW;
		}
		else {
			textColor = Color.RED;
		}
		
		setForeground(textColor);
	}
}