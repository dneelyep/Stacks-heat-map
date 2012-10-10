package com.heatmap;

import javax.swing.JLabel;

/** Class to represent a single range of books in the library. */
public class Range extends JLabel {
	/** The first call number in this Range. */
	private String startCallNumber;
	
	/** The last call number in this Range. */
	private String endCallNumber;
	
	/** The number of days since this range has last had a pickup done on it. */
	private int daysSinceLastChecked;
	
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
	
	/** Get the number of days since this Range was last checked. */
	public int getDaysSinceChecked() {
		return daysSinceLastChecked;
	}
	
	/** Set this Range's start call number to a new value start. */
	public void setStart(String start) {
		startCallNumber = start;
	}
	
	/** Set this Range's end call number to a new value end. */
	public void setEnd(String end) {
		endCallNumber = end;
	}
	
	/** Set this Range's last checked date to a new value newLastChecked. */
	public void setLastChecked(String newLastChecked) {
		if (newLastChecked.isEmpty() == false) {
			daysSinceLastChecked = Integer.parseInt(newLastChecked);
		}
		else {
			daysSinceLastChecked = -1;
		}
	}
	
	/** Get how many days since this Range was last checked. */
	public int getLastChecked() {
		return daysSinceLastChecked;
	}
}
