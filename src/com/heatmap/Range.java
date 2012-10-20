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

	/** Set the Date this range was last checked to a new Date d. After the Date
	 * has been changed, update this Range's color. */
	public void setDayLastChecked(Date d) {
		dayLastChecked = d;
		updateColor(false);
	}

	/** Get the Date that this Range was last checked for cleanliness. */
	public Date getDayLastChecked() {
		return dayLastChecked;
	}
	
	/** Get the number of days since this Range was last checked 
	 * for cleanliness. */
	public int getDaysSinceChecked() {
		Calendar c = Calendar.getInstance();
		Date today = c.getTime();
		return Days.daysBetween(new DateTime(dayLastChecked), new DateTime(today)).getDays();
	}

	/** Re-set the color for this Range. If this Range has been clicked, use
     * the focused color rather than the normal Range color. */
	// TODO Should this be changed to protected?
    // TODO Make it more clear in the code what the false in updateColor(false) means.
	public void updateColor(Boolean clicked) {
        if (clicked) {
            setForeground(Color.BLUE);
        }
        else {
            if (getDaysSinceChecked() < 15) {
                setForeground(Color.GREEN);
            }
            else if (getDaysSinceChecked() >= 15 && getDaysSinceChecked() < 30) {
                setForeground(Color.YELLOW);
            }
            else {
                setForeground(Color.RED);
            }
        }
	}
}