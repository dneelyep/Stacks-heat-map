package com.heatmap;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.joda.time.DateTime;
import org.joda.time.Days;

/** GUI.java - Class that holds the primary GUI for the program.
 *
 * @author Daniel Neel */
public class GUI extends JApplet implements MouseListener {
	
	// TODO Fix the problem where, when resizing the window, input fields shrink.
	/** Floor that represents the library's third floor. */
	private Floor thirdFloor;
	
	/** Floor that represents the library's fourth floor. */
	private Floor fourthFloor;
	
	/** Panel that contains the components for the shelf map. */
	private JPanel floorComponents;
	
	/** The Range currently clicked on to be edited by the user. */
	private Range clickedRange;
	
	/** Label that displays which Range is currently being edited. */
	private JLabel clickedRangeStart = new JLabel("Start: ");
	
	/** Label that displays which Range is currently being edited. */
	private JLabel clickedRangeEnd = new JLabel("End: ");
	
	/** Field to change the clicked-on Range's starting call #. */
	private JTextField newRangeStart = new JTextField("", 10);
	
	/** Field to change the clicked-on Range's ending call #. */
	private JTextField newRangeEnd = new JTextField("", 10);

	/** Label that displays the number of days since the currently 
	 * moused-over Range has been checked. */
	// TODO Use input verification on the text fields to ensure values are appropriate.
	//      See http://docs.oracle.com/javase/tutorial/uiswing/misc/focus.html#inputVerification
	private JTextField newDaysSinceChecked = new JTextField("", 3);
	
	/** Label to display the clicked-on Range's starting call #. */
	private JLabel currentRangeStart = new JLabel("<start>");

	/** Label to display the clicked-on Range's ending call #. */
	private JLabel currentRangeEnd = new JLabel("<end>");
	
	/** Label to display the clicked-on Range's days since it was checked. */
	private JLabel currentDayLastChecked = new JLabel("<days>");

	/** Button to change attributes of the currently clicked Range. */
	private JButton submitData = new JButton("Submit");
	
	/** Constraints used to place components in floorComponents. */
	private GridBagConstraints fCConstraints;
	
	/** The Floor currently being viewed by the user. */
	private Floor currentFloor = null;
	
	/** Whether or not a Range has been clicked, to lock editing. */
	private Boolean focused = false;

	/** Button to cancel editing of a Range's information. */
	private JButton cancel = new JButton("Cancel");

    /** Attempt to initialize the GUI for this program. */
	@Override
    public void init() {
    	try {
            SwingUtilities.invokeAndWait(new Runnable() {
            	public void run() {
            		createGUI();
            	}
            });
        } catch (Exception e) {
            System.err.println("createGUI didn't complete successfully");
        }
    }
    
    /** Set up the GUI used for the program.*/
    public void createGUI() {
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    	setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());
    	setLayout(new GridBagLayout());
    	GridBagConstraints g = new GridBagConstraints();
    	
    	// Initialize the Floors.
    	thirdFloor = new Floor(3);
    	fourthFloor = new Floor(4);

    	addAt(new JLabel("Stacks Cleanliness Heat map"), 1, 0, g);
    	
    	thirdFloor.getButton().addMouseListener(this);
    	fourthFloor.getButton().addMouseListener(this);
    	
    	addAt(thirdFloor.getButton(), thirdFloor.getButtonX(), thirdFloor.getButtonY(), g);
    	addAt(fourthFloor.getButton(), fourthFloor.getButtonX(), fourthFloor.getButtonY(), g);

    	// TODO Maybe add a parameter Boolean listen to addAt(), that determines if we want to add
    	//      a MouseListener to the component or not.
    	floorComponents = new JPanel();
    	floorComponents.setLayout(new GridBagLayout());
    	addAt(floorComponents, 1, 1, g);
    	addAt(clickedRangeStart, 2, 0, g);
    	addAt(clickedRangeEnd, 2, 1, g);
    	addAt(new JLabel("Days since checked:"), 2, 2, g);
    	addAt(currentRangeStart, 3, 0, g);
    	addAt(newRangeStart, 4, 0, g);
    	addAt(currentRangeEnd, 3, 1, g);
    	addAt(newRangeEnd, 4, 1, g);
    	addAt(currentDayLastChecked, 3, 2, g);
    	// TODO Add a max number of columns on days since checked input.
    	addAt(newDaysSinceChecked, 4, 2, g);
    	
    	submitData.addMouseListener(this);
    	submitData.setEnabled(false);
    	addAt(submitData, 5, 2, g);
    	
    	cancel.addMouseListener(this);
    	addAt(cancel, 6, 0, g);
    	
    	allowInput(false);
    	fCConstraints = new GridBagConstraints();
    }

    /** Display a given Floor f's Ranges on this GUI. */
    public void displayFloor(Floor f) {
    	if (f != thirdFloor && f != fourthFloor) {
    		System.out.println("Error! Tried to display a Floor that does not exist.");
    	}
    	else {
    		floorComponents.removeAll();
    		clearInput();
    		allowInput(false);

    		for (Range r : f.getRanges()) {
    			fCConstraints.gridx = r.getXCoord();
    			fCConstraints.gridy = r.getYCoord();
    			r.addMouseListener(this);

    			// TODO Clean up this code. Implemented it sloppily, make sure it makes sense.
    			Calendar c = Calendar.getInstance();
    			c.set(2012, 9, 9);
    			Date today = c.getTime();
    			int daysSinceChecked = Days.daysBetween(new DateTime(today), new DateTime(r.getDayLastChecked())).getDays();
    			System.out.println("Days: " + Integer.toString(daysSinceChecked));

    			if (daysSinceChecked < 15) {
    				r.setForeground(Color.GREEN);
    			}
    			else if (daysSinceChecked >= 15 && daysSinceChecked < 30) {
    				r.setForeground(Color.YELLOW);
    			}
    			else {
    				r.setForeground(Color.RED);
    			}
    			
    			floorComponents.add(r, fCConstraints);
    		}
    		
    		floorComponents.revalidate();
    	}
    }
    
    /** Clear all input fields on this GUI of their data. */
    public void clearInput() {
    	newRangeStart.setText("");
    	newRangeEnd.setText("");
    	newDaysSinceChecked.setText("");
    }
    
    /** Allow or disallow the user to edit the properties 
     * of a clicked-on Range. */
    public void allowInput(Boolean b) {
    	newRangeStart.setEnabled(b);
    	newRangeEnd.setEnabled(b);
    	newDaysSinceChecked.setEnabled(b);
    	submitData.setEnabled(b);
    	cancel.setEnabled(b);
    }

    /** Allow the user to change the clicked on Range on the viewed Floor's properties. */
    @Override
    public void mouseClicked(MouseEvent e) {
    	// TODO Set a minimum width on the JLabels that show current range values so the UI doesn't jump around on Range mouseover.
    	// Select the clicked-on Range for editing.
    	if (e.getComponent() instanceof Range) {
	    	Range r = (Range) e.getSource();
	    	clickedRange = r;
	    	clickedRangeStart.setText("Start (" + r.getXCoord() + "," + r.getYCoord() + "):");
	    	currentRangeStart.setText(r.getStart());
	    	clickedRangeEnd.setText("End (" + r.getXCoord() + "," + r.getYCoord() + "):");
	    	currentRangeEnd.setText(r.getEnd());
	    	
	    	currentDayLastChecked.setText(r.getDayLastChecked().toString());

	    	focused = true;
	    	allowInput(true);
    	}
    	
    	// Change the values of a Range's properties depending on user input.
    	else if (e.getComponent() instanceof JButton) {
    		JButton button = (JButton) e.getComponent();

    		if (button == submitData) {
    			// TODO Find a cleaner way of checking whether the textfield's text has been edited.
    			if (newRangeStart.getText().equals("") == false) {
    				clickedRange.setStart(newRangeStart.getText());
    			}
    			if (newRangeEnd.getText().equals("") == false) {
    				clickedRange.setEnd(newRangeEnd.getText());
    			}
    			// TODO Re-implement this. Include a calendar widget?
//    			if (newDaysSinceChecked.getText().equals("") == false) {
//    				clickedRange.setLastChecked(newDaysSinceChecked.getText());
//    			}
    		}
    		else if (button == cancel) {
    			System.out.println("Cancel!");
    			allowInput(false);
    		}

    		else if (button == thirdFloor.getButton()|| button == fourthFloor.getButton()) {
        		// TODO Would radio buttons or an equivalent be more natural to use than buttons
        		//      for floor switching?
    			// TODO Do I need this currentFloor != check? In this case, other Floor
    			//      buttons should be greyed out.
    			// TODO Condense these conditions more if possible.
        		if (button == thirdFloor.getButton() && currentFloor != thirdFloor) {
        			currentFloor = thirdFloor;
        			fourthFloor.getButton().setEnabled(true);
        		}
        		else if (button == fourthFloor.getButton() && currentFloor != fourthFloor) {
        			currentFloor = fourthFloor;
        			thirdFloor.getButton().setEnabled(true);
        		}
        		currentFloor.getButton().setEnabled(false);
        	}

    		clearInput();
    		focused = false;
    		clickedRange = null;
    		displayFloor(currentFloor);
    		currentFloor.write();
    	}
    }
    
    /** Add a given JComponent c to the GUI at coordinates (x, y). */
    public void addAt(JComponent c, int x, int y, GridBagConstraints g) {
    	g.gridx = x;
    	g.gridy = y;
    	add(c, g);
    }

    /** Update the GUI components that display information about the 
     * currently moused-over Range. */
    @Override
    public void mouseEntered(MouseEvent e) {
    	if (focused == false && e.getComponent() instanceof Range) { 
   			Range r = (Range) e.getSource();
   			clickedRangeStart.setText("Start (" + r.getXCoord() + "," + r.getYCoord() + "):");
   			currentRangeStart.setText(r.getStart());
   			clickedRangeEnd.setText("End (" + r.getXCoord() + "," + r.getYCoord() + "):");
   			currentRangeEnd.setText(r.getEnd());
//   			currentDaysSinceChecked.setText(Integer.toString(r.getDaysSinceChecked()));
   			// TODO Remove this duplication. This is done previously.
			Calendar c = Calendar.getInstance();
			c.set(2012, 9, 9);
			Date today = c.getTime();
   			int daysSinceChecked = Days.daysBetween(new DateTime(today), new DateTime(r.getDayLastChecked())).getDays();
   			currentDayLastChecked.setText(daysSinceChecked + " days ago.");
			
    	}
    }
    
    @Override
    public void mouseExited(MouseEvent e) {
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
    }
}