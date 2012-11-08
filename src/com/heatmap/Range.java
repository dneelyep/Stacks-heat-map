package com.heatmap;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;

import javax.swing.*;

import org.joda.time.DateTime;
import org.joda.time.Days;

/** Class to represent a single range of books in the library. */
public class Range extends JLabel implements MouseListener {

    /** String that holds the base path for all images accessed in this Range. */
    private final String IMGROOT = "C:/Users/Daniel/Desktop/Programming/Java/Stacks-heat-map/res/bin/";

	/** The first call number in this Range. */
	private String startCallNumber;

	/** The last call number in this Range. */
	private String endCallNumber;

    // TODO Get rid of these individual Date fields, since I can just use a HashMap instead?
	/** The day this Range was last checked for cleanliness. */
	private Date dayLastChecked;

    /** The day this Range was last shifted. */
    private Date dayLastShifted;

    /** The day this Range last had its columns faced. */
    private Date dayLastFaced;

    /** The day this Range was last dusted. */
    private Date dayLastDusted;

    // TODO Convert Dates to StackDates, that aside from a Date have a JXDatePicker to manipulate their value.
    /** The day this Range was last shelf read. */
    private Date dayLastRead;

    /** A Map of all the Date properties that belong to this Range. */
    // TODO Remove all of the dayLastRead/etc fields, and just replace them with this HashMap?
    private final LinkedHashMap<String, Date> dateProperties = new LinkedHashMap<String, Date>(5);

    /** This Range's x-coordinate in the GUI. */
	private final int XCOORD;
	
	/** This Range's y-coordinate in the GUI. */
	private final int YCOORD;

    /** The GUI that this Range is contained in. */
    private GUI parentGUI;

	/** Create a new Range with the given x-coordinate x and y-coordinate y. */
	public Range(int x, int y, GUI gui) {
		XCOORD = x;
		YCOORD = y;
        setIcon(new ImageIcon("C:/Users/Daniel/Desktop/Programming/Java/Stacks-heat-map/res/bin/defaultRange.png"));
        dateProperties.put("checked", dayLastChecked);
        dateProperties.put("shifted", dayLastShifted);
        dateProperties.put("faced", dayLastFaced);
        dateProperties.put("dusted", dayLastDusted);
        dateProperties.put("read", dayLastRead);
        addMouseListener(this);
        updateTooltip();
        parentGUI = gui;
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
            setIcon(new ImageIcon(IMGROOT + "selectedRange.png"));
        }
        else if (action.equals("mousedover")) {
            setForeground(Color.CYAN);
            setIcon(new ImageIcon(IMGROOT + "mousedOverRange.png"));
        }
        // TODO What's the deal with making sure the action is not "none"?
        else {
            if (getDaysSince(action) < 15) {
                setIcon(new ImageIcon(IMGROOT + "goodRange.png"));
            }
            else if (getDaysSince(action) >= 15 && getDaysSince(action) < 30) {
                setIcon(new ImageIcon(IMGROOT + "decentRange.png"));
            }
            else {
                setIcon(new ImageIcon(IMGROOT + "badRange.png"));
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
                     + "<br />Last dusted: "  + fonrmatter.format(dayLastDusted)
                     + "<br />Last read: "    + formatter.format(dayLastRead)*/
                     + "</html>");
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO This whole focusedRange abstraction is weak, confusing.
        // Select the clicked-on Range for editing.
        if (parentGUI.getFocusedRange() == null) {
            parentGUI.setFocusedRange(this);
            parentGUI.setInputMode(true);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    // TODO Consider using a MouseMotionListener.
    /** Update the GUI components that display information about the
     * currently moused-over Range. */
    @Override
    public void mouseEntered(MouseEvent e) {
        // LEFTOFFHERE In the process of transferring the code for mouseEntered from GUI over to Range. Afterwards, try to get rid of more of the manual action/mouse event handling code in GUI.
        // TODO Try to find a more natural way than focusedRange == null to express whether or not a Range is focused.
        if (parentGUI.getFocusedRange() == null) {
            // TODO Here's another chance to use iteration through a list of input components.

            // Here's current progress on what I should be shooting for.
            // TODO Make this a method of the startCallNumber text field in the gui?
            parentGUI.getStartCallNumber().setText(startCallNumber);
            parentGUI.getEndCallNumber().setText(endCallNumber);

            // TODO Why does dayLastX have a separate graphical representation stored in GUI? Why not store that graphical
            //      representation in the dayLastX object?
            parentGUI.getDayLastChecked().setDate(dateProperties.get("checked"));
            parentGUI.getDayLastShifted().setDate(dateProperties.get("shifted"));
            parentGUI.getDayLastFaced().setDate(dateProperties.get("faced"));
            parentGUI.getDayLastDusted().setDate(dateProperties.get("dusted"));
            parentGUI.getDayLastRead().setDate(dateProperties.get("read"));
            updateColor("mousedover");

            // TODO Try to have the date picker display text in a more human-readable format.
            // TODO Implement holding Control to select multiple ranges at once.
        }
    }

    /** Actions to undertake when the mouse exits from a given Range. */
    @Override
    public void mouseExited(MouseEvent e) {
        if (parentGUI.getFocusedRange() == null) {
            updateColor(parentGUI.getSelectedView().getText().toLowerCase());
        }
    }
}