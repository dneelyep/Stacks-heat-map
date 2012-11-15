package com.heatmap;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

import javax.swing.*;

import org.joda.time.DateTime;
import org.joda.time.Days;

/** Class to represent a single range of books in the library. */
public class Range extends JLabel {

    /** String that holds the base path for all images accessed in this Range. */
    private final String IMGROOT = "C:/Users/Daniel/Desktop/Programming/Java/Stacks-heat-map/res/bin/";

	/** The first call number in this Range. */
	private String startCallNumber;

	/** The last call number in this Range. */
	private String endCallNumber;

    /** The Date this Range was last checked for cleanliness. */
    private Date dayLastChecked;

    /** The Date this Range was last shifted. */
    private Date dayLastShifted;

    /** The Date this Range was last faced. */
    private Date dayLastFaced;

    /** The Date this Range was last dusted. */
    private Date dayLastDusted;

    /** The Date this Range was last shelf read. */
    private Date dayLastRead;

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

    // TODO Move this method to GUI?
    /** Set the Date this Range last had activity done to it to a new Date desiredActivityDate. */
    public void setDayLast(String activity, Date desiredDate) {
        // TODO Make use of a map or similar.
        if (activity.equals("checked")) {
            dayLastChecked = desiredDate;
        }
        else if (activity.equals("shifted")) {
            dayLastShifted = desiredDate;
        }
        else if (activity.equals("faced")) {
            dayLastFaced = desiredDate;
        }
        else if (activity.equals("dusted")) {
            dayLastDusted = desiredDate;
        }
        else if (activity.equals("read")) {
            dayLastRead = desiredDate;
        }

        updateColor(activity);
    }

    /** Get the date this Range last had activity done to it. */
    public Date getDayLast(String activity) {
        if (parentGUI.getDateControllers().containsKey(activity)) {
            if (activity.equals("checked")) {
                return dayLastChecked;
            }
            else if (activity.equals("shifted")) {
                return dayLastShifted;
            }
            else if (activity.equals("faced")) {
                return dayLastFaced;
            }
            else if (activity.equals("dusted")) {
                return dayLastDusted;
            }
            // TODO Clean up this else hack.
            else { // activity.equals("read"))
                return dayLastRead;
            }
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

        if (parentGUI.getDateControllers().containsKey(activity)) {
            // TODO Handle this more elegantly than a bunch of if/else ifs.
            if (activity.equals("checked")) {
                return Days.daysBetween(new DateTime(dayLastChecked), new DateTime(today)).getDays();
            }
            else if (activity.equals("shifted")) {
                return Days.daysBetween(new DateTime(dayLastShifted), new DateTime(today)).getDays();
            }
            else if (activity.equals("faced")) {
                return Days.daysBetween(new DateTime(dayLastFaced), new DateTime(today)).getDays();
            }
            else if (activity.equals("dusted")) {
                return Days.daysBetween(new DateTime(dayLastDusted), new DateTime(today)).getDays();
            }
            else if (activity.equals("read")) {
                return Days.daysBetween(new DateTime(dayLastRead), new DateTime(today)).getDays();
            }
            // TODO Handle this correctly.
            else {
                return -3000;
            }
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

    /** Update the contents of this Range's tooltip. */
    public void updateTooltip() {
        // TODO If I want the more detailed tooltip, I will need to format the Dates. To do that,
        // I need to check for nulls, etc.
        setToolTipText("<html>Start: " + startCallNumber
                     + "<br />End: " + endCallNumber
/*                     + "<br />Last checked: " + formatter.format(lastCheckedController)
                     + "<br />Last shifted: " + formatter.format(lastShiftedController)
                     + "<br />Last faced: "   + formatter.format(lastFacedController)
                     + "<br />Last dusted: "  + fonrmatter.format(lastDustedController)
                     + "<br />Last read: "    + formatter.format(lastReadController)*/
                     + "</html>");
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

        // TODO Consider using a MouseMotionListener.
        /** Update the GUI components that display information about the
         * currently moused-over Range. */
        @Override
        public void mouseEntered(MouseEvent e) {
            // TODO Try to find a more natural way than focusedRange == null to express whether or not a Range is focused.
            if (parentGUI.getFocusedRange() == null) {
                // TODO Make this a method of the startCallNumber text field in the gui?
                // Here's current progress on what I should be shooting for.
                // TODO Here's another chance to use iteration through a list of input components.
                parentGUI.getStartCallNumberController().setText(startCallNumber);
                parentGUI.getEndCallNumberController().setText(endCallNumber);

                // TODO Why does lastXController have a separate graphical representation? Why not store that graphical
                //      representation in the dayLastX object?
                // TODO This set of actions is nonsense. I think that I need to setDate to the value of this Range's
                //      dateLastX.
                parentGUI.getDateControllers().get("checked").setDate(dayLastChecked);
                parentGUI.getDateControllers().get("shifted").setDate(dayLastShifted);
                parentGUI.getDateControllers().get("faced").setDate(dayLastFaced);
                parentGUI.getDateControllers().get("dusted").setDate(dayLastDusted);
                parentGUI.getDateControllers().get("read").setDate(dayLastRead);

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