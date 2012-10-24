package com.heatmap;

import java.awt.Color;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.*;

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
        dateProperties.put("checked", dayLastChecked);
		updateColor("none");
	}

    /** Set the Date this range was last shifted to a new Date d. After the Date
     * has been changed, update this Range's color. */
    public void setDayLastShifted(Date d) {
        dayLastShifted = d;
        dateProperties.put("shifted", dayLastShifted);
        updateColor("none");
    }

    /** Set the Date this range was last faced to a new Date d. After the Date
     * has been changed, update this Range's color. */
    public void setDayLastFaced(Date d) {
        dayLastFaced = d;
        dateProperties.put("faced", dayLastFaced);
        updateColor("none");
    }

    /** Set the Date this range was last dusted to a new Date d. After the Date
     * has been changed, update this Range's color. */
    public void setDayLastDusted(Date d) {
        dayLastDusted = d;
        dateProperties.put("dusted", dayLastDusted);
        updateColor("none");
    }

    /** Set the Date this range was last shelf read to a new Date d. After the Date
     * has been changed, update this Range's color. */
    public void setDayLastRead(Date d) {
        dayLastRead = d;
        dateProperties.put("read", dayLastRead);
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
  /*  public int getDaysSince(String activity) {
        Calendar c = Calendar.getInstance();
        Date today = c.getTime();
        // TODO Try to do this more elegantly than a bunch of if/else if statements.
        if (activity.equals("checked")) {

        }
        else if (activity.equals("shifted")) {

        }
        else if (activity.equals("faced")) {

        }
        else if (activity.equals("dusted")) {

        }
        else if (activity.equals("read")) {

        }*/
        // LEFTOFFHERE Implementing this part, need to work in the return statement here.
    //    return Days.daysBetween(new DateTime(dayLastChecked), new DateTime(today)).getDays();
//    }

    // TODO Make this a general method for getting days since something was done. Maybe
    // also do the same for the date getters?
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
            if (getDaysSinceChecked() < 15) {
                setForeground(Color.GREEN);
                setIcon(new ImageIcon("C:/Users/Daniel/Desktop/Programming/Java/Stacks-heat-map/res/bin/goodRange.png"));
            }
            else if (getDaysSinceChecked() >= 15 && getDaysSinceChecked() < 30) {
                setForeground(Color.YELLOW);
                setIcon(new ImageIcon("C:/Users/Daniel/Desktop/Programming/Java/Stacks-heat-map/res/bin/decentRange.png"));
            }
            else {
                setForeground(Color.RED);
                setIcon(new ImageIcon("C:/Users/Daniel/Desktop/Programming/Java/Stacks-heat-map/res/bin/badRange.png"));
            }
        }
	}

    /** Get a Map of String keys associated with the Dates this
     * Range lasted had a given activity done to it. */
    public LinkedHashMap<String, Date> getDates() {
        return dateProperties;
    }
}