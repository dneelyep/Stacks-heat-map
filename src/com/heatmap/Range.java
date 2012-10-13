package com.heatmap;

import java.util.Date;

import javax.swing.JLabel;

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
	public Date getDayLastChecked() {
		return dayLastChecked;
	}
}
