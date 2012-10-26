package com.heatmap;

import java.awt.Color;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.*;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;

/** Class to represent a single range of books in the library. */
public class Range extends JLabel {
	/** The first call number in this Range. */
	private String startCallNumber;
	
	/** The last call number in this Range. */
	private String endCallNumber;

	/** The day this Range was last checked for cleanliness. */
	private Date dayLastChecked;

    /** The day this Range was last shifted. */
    private Date dayLastShifted;

    /** The day this Range last had its columns faced. */
    private Date dayLastFaced;

    /** The day this Range was last dusted. */
    private Date dayLastDusted;

    // TODO Convert Dates to StackDates.
    /** The day this Range was last shelf read. */
    private Date dayLastRead;

    /** A Map of all the Date properties that belong to this Range. */
    private LinkedHashMap<String, Date> dateProperties = new LinkedHashMap<String, Date>(5);

    /** This Range's x-coordinate in the GUI. */
	private final int XCOORD;
	
	/** This Range's y-coordinate in the GUI. */
	private final int YCOORD;

	/** Create a new Range with the given x-coordinate x and y-coordinate y. */
	public Range(int x, int y) {
		XCOORD = x;
		YCOORD = y;
        setIcon(new ImageIcon("C:/Users/Daniel/Desktop/Programming/Java/Stacks-heat-map/res/bin/defaultRange.png"));
        dateProperties.put("checked", dayLastChecked);
        dateProperties.put("shifted", dayLastShifted);
        dateProperties.put("faced", dayLastFaced);
        dateProperties.put("dusted", dayLastDusted);
        dateProperties.put("read", dayLastRead);
        updateTooltip();
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
        updateTooltip();
	}
	
	/** Set this Range's end call number to a new value end. */
	public void setEnd(String end) {
		endCallNumber = end;
        updateTooltip();
	}

    /** Set the Date this Range last had activity done to it to a new Date d. */
    public void setDayLast(String activity, Date d) {
        Date desiredActivityDate = dateProperties.get(activity);
        desiredActivityDate = d;
        dateProperties.put(activity, desiredActivityDate);
        updateColor("none");
    }

    /** Get the date this Range last had activity done to it. */
    public Date getDayLast(String activity) {
        if (dateProperties.containsKey(activity)) {
            return dateProperties.get(activity);
        }
        else {
            System.err.println("Error! Tried to get the Date of an invalid activity." + activity);
            // TODO Handle this case more gracefully than returning a random date.
            return new Date();
        }
    }

    /** Get the number of days since a given
     * activity was performed on this Range. */
    public int getDaysSince(String activity) {
        Calendar c = Calendar.getInstance();
        Date today = c.getTime();

        if (dateProperties.containsKey(activity)) {
            return Days.daysBetween(new DateTime(dateProperties.get(activity)), new DateTime(today)).getDays();
        }
        else {
            // TODO Handle this more elegantly than returning a 0.
            System.err.println("Error! Attempted to get the number of days since an invalid activity was performed.");
            return 0;
        }
    }

	/** Re-set the color for this Range. If this Range has been clicked, use
     * the focused color rather than the normal Range color. */
	// TODO Should this be changed to protected?
    // TODO Make it more clear in the code what the false in updateColor(false) means.
	public void updateColor(String action) {
        if (action.equals("clicked")) {
            setForeground(Color.BLUE);
            // TODO Find a more graceful way of handling paths. EG relative
            // paths, or just replace the end of the path name.
            setIcon(new ImageIcon("C:/Users/Daniel/Desktop/Programming/Java/Stacks-heat-map/res/bin/selectedRange.png"));
        }
        else if (action.equals("mousedover")) {
            setForeground(Color.CYAN);
            setIcon(new ImageIcon("C:/Users/Daniel/Desktop/Programming/Java/Stacks-heat-map/res/bin/mousedOverRange.png"));
        }
        else {
            if (!action.equals("none") && getDaysSince(action) < 15) {
                setIcon(new ImageIcon("C:/Users/Daniel/Desktop/Programming/Java/Stacks-heat-map/res/bin/goodRange.png"));
            }
            else if (!action.equals("none") && getDaysSince(action) >= 15 && getDaysSince(action) < 30) {
                setIcon(new ImageIcon("C:/Users/Daniel/Desktop/Programming/Java/Stacks-heat-map/res/bin/decentRange.png"));
            }
            else if (!action.equals("none")) {
                setIcon(new ImageIcon("C:/Users/Daniel/Desktop/Programming/Java/Stacks-heat-map/res/bin/badRange.png"));
            }
        }
	}

    /** Get a Map of String keys associated with the Dates this
     * Range lasted had a given activity done to it. */
    public LinkedHashMap<String, Date> getDates() {
        return dateProperties;
    }

    /** Update the contents of this Range's tooltip. */
    private void updateTooltip() {
        setToolTipText("Start: " + startCallNumber + " | End: " + endCallNumber);
    }
}