package com.heatmap;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.prefs.Preferences;

import javax.swing.*;

import org.joda.time.DateTime;
import org.joda.time.Days;

/** Class to represent a single range of books in the library. */
public class Range extends JLabel {

    /** String that holds the base path for all images accessed in this Range. */
    private final String IMGROOT = "C:/Users/Daniel/Desktop/Programming/Java/Stacks-heat-map/bin/res/bin/";

	/** The first call number in this Range. */
	private String startCallNumber;

	/** The last call number in this Range. */
	private String endCallNumber;

    /** Map that holds the Date this Range last had given Activities done to it. */
    private HashMap<Activities, Date> dayLastMap = new HashMap<>(5);

    /** The GUI that this Range is contained in. */
    private GUI parentGUI;

    /** This Range's x-coordinate in the GUI. */
	private final int XCOORD;
	
	/** This Range's y-coordinate in the GUI. */
	private final int YCOORD;

	/** Create a new Range with the given x-coordinate x and y-coordinate y. */
	public Range(int x, int y, GUI gui) {
		XCOORD = x;
		YCOORD = y;
        setIcon(new ImageIcon("C:/Users/Daniel/Desktop/Programming/Java/Stacks-heat-map/res/bin/defaultRange.png"));
        addMouseListener(new MouseEventHandler());
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
	}
	
	/** Set this Range's end call number to a new value end. */
	public void setEnd(String end) {
		endCallNumber = end;
	}

    /** Set the Date this Range last had activity done to it to a new Date desiredActivityDate. */
    public void setDayLast(Activities activity, Date desiredDate) {
        dayLastMap.put(activity, desiredDate);
        updateColor(activity.toString());
    }

    /** Get the date this Range last had a given activity done to it. */
    public Date getDayLast(Activities activity) {
        // Fail when there is no Date yet set for a given activity.
        if (dayLastMap.containsKey(activity))
            return dayLastMap.get(activity);
        else
            // TODO Handle this case more gracefully than returning a random date.
            return new Date();
    }

    /** Get the number of days since a given
     * activity was performed on this Range. */
    private int getDaysSince(Activities activity) {
        Calendar c = Calendar.getInstance();

        if (dayLastMap.containsKey(activity)) {
            return Days.daysBetween(new DateTime(dayLastMap.get(activity)), new DateTime(c.getTime())).getDays();
        }
        else {
            // TODO Handle this more elegantly than returning a 0.
            System.err.println("Error! Attempted to get the number of days since an invalid activity was performed: " + activity.toString());
            return 0;
        }
    }

	/** Re-set the color for this Range. If this Range has been clicked, use
     * the focused color rather than the normal Range color. */
	protected void updateColor(String action) {
        if (action.equals("clicked")) {
            setForeground(Color.BLUE);
            // TODO Find a more graceful way of handling paths. EG relative
            // paths, or just replace the end of the path name. This will be more difficult than expected. The
            // problem is in finding the image's path relative to the applet.
            setIcon(new ImageIcon(IMGROOT + "selectedRange.png"));
        }
        else if (action.equals("mousedover")) {
            setForeground(Color.CYAN);
            setIcon(new ImageIcon(IMGROOT + "mousedOverRange.png"));
        }
        else {
            Preferences prefs = Preferences.userNodeForPackage(getClass());

            if (getDaysSince(Activities.valueOf(action)) < prefs.getInt(action + ".good", 0)) {
                setIcon(new ImageIcon(IMGROOT + "goodRange.png"));
            }
            else if (getDaysSince(Activities.valueOf(action)) >= prefs.getInt(action + ".good", 0)
                  && getDaysSince(Activities.valueOf(action)) < prefs.getInt(action + ".bad", 100)) {
                setIcon(new ImageIcon(IMGROOT + "decentRange.png"));
            }
            else {
                setIcon(new ImageIcon(IMGROOT + "badRange.png"));
            }
        }
	}

    /** Inner class with the sole purpose of handling mouse events for this Range. */
    private class MouseEventHandler extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            // TODO This whole focusedRange abstraction is weak, confusing.
            // Select the clicked-on Range for editing.
            if (parentGUI.getFocusedRange() == null) {
                parentGUI.setFocusedRange(Range.this);
                parentGUI.setInputMode(true);
            }
        }

        /** Update the GUI components that display information about the
         * currently moused-over Range. */
        @Override
        public void mouseEntered(MouseEvent e) {
            // TODO Try to find a more natural way than focusedRange == null to express whether or not a Range is focused.
            if (parentGUI.getFocusedRange() == null) {
                // Here's current progress on what I should be shooting for.
                parentGUI.getStartCallNumberController().setText(startCallNumber);
                parentGUI.getEndCallNumberController().setText(endCallNumber);

                for (Activities activity : Activities.values()){
                    activity.getController().setDate(dayLastMap.get(activity));
                }

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
}