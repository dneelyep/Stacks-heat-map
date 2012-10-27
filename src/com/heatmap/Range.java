package com.heatmap;

import java.awt.Color;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;

import javax.swing.*;
import javax.swing.text.DateFormatter;

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
    private final LinkedHashMap<String, Date> dateProperties = new LinkedHashMap<String, Date>(5);

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

    /** Set the Date this Range last had activity done to it to a new Date desiredActivityDate. */
    public void setDayLast(String activity, Date desiredActivityDate) {
        dateProperties.put(activity, desiredActivityDate);
        updateColor(activity);
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
    private int getDaysSince(String activity) {
        Calendar c = Calendar.getInstance();
        Date today = c.getTime();

        if (dateProperties.containsKey(activity)) {
            return Days.daysBetween(new DateTime(dateProperties.get(activity)), new DateTime(today)).getDays();
        }
        else {
            // TODO Handle this more elegantly than returning a 0.
            System.err.println("Error! Attempted to get the number of days since an invalid activity was performed: " + activity);
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
        // TODO What's the deal with making sure the action is not "none"?
        else {
            if (getDaysSince(action) < 15) {
                setIcon(new ImageIcon("C:/Users/Daniel/Desktop/Programming/Java/Stacks-heat-map/res/bin/goodRange.png"));
            }
            else if (getDaysSince(action) >= 15 && getDaysSince(action) < 30) {
                setIcon(new ImageIcon("C:/Users/Daniel/Desktop/Programming/Java/Stacks-heat-map/res/bin/decentRange.png"));
            }
            else {
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
    public void updateTooltip() {
        // TODO If I want the more detailed tooltip, I will need to format the Dates. To do that,
        // I need to check for nulls, etc.
        setToolTipText("<html>Start: " + startCallNumber
                     + "<br />End: " + endCallNumber
/*                     + "<br />Last checked: " + formatter.format(dayLastChecked)
                     + "<br />Last shifted: " + formatter.format(dayLastShifted)
                     + "<br />Last faced: "   + formatter.format(dayLastFaced)
                     + "<br />Last dusted: "  + formatter.format(dayLastDusted)
                     + "<br />Last read: "    + formatter.format(dayLastRead)*/
                     + "</html>");
    }
}