package com.heatmap;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/** GUI.java - Class that holds the primary GUI for the program.
 *
 * @author Daniel Neel */
public class GUI extends JApplet implements ActionListener, MouseListener {
	
	/** Button to allow the user to view the 3rd floor's status. */
	private JButton thirdFloorButton;
	
	/** Button to allow the user to view the 4th floor's status. */
	private JButton fourthFloorButton;

	/** Floor to represent the library's third floor. */
	private Floor thirdFloor;
	
	/** Floor to represent the library's fourth floor. */
	private Floor fourthFloor;
	
	/** Panel that contains the components for the shelf map. */
	private JPanel floorComponents;
	
	/** The Range currently clicked on to be edited by the user. */
	private Range clickedRange;
	
	/** Label that displays which Range is currently being edited. */
	private JLabel clickedRangeStart;
	
	/** Label that displays which Range is currently being edited. */
	private JLabel clickedRangeEnd;
	
	/** Field to change the clicked-on Range's starting call #. */
	private JTextField newRangeStart;
	
	/** Field to change the clicked-on Range's ending call #. */
	private JTextField newRangeEnd;

	/** Label that displays the number of days since the currently 
	 * moused-over Range has been checked. */
	// TODO Use input verification on the text fields to ensure values are appropriate.
	//      See http://docs.oracle.com/javase/tutorial/uiswing/misc/focus.html#inputVerification
	private JTextField newDaysSinceChecked;
	
	/** Label to display the clicked-on Range's starting call #. */
	private JLabel currentRangeStart;
	
	/** Label to display the clicked-on Range's ending call #. */
	private JLabel currentRangeEnd;
	
	/** Label to display the clicked-on Range's days since it was checked. */
	private JLabel currentDaysSinceChecked;

	/** Button to change attributes of the currently clicked Range. */
	private JButton submitData;

	/** Constraints used to place components in floorComponents. */
	private GridBagConstraints fCConstraints;
	
	/** The Floor currently being viewed by the user. */
	private Floor currentFloor = null;
	
	/** Whether or not a Range has been clicked, to lock editing. */
	private Boolean rangeClicked = false;
	
	/** Button to cancel editing of a Range's information. */
	private JButton cancel;

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

    	g.gridx = 1;
    	g.gridy = 0;
    	add(new JLabel("Stacks Cleanliness Heat Map"), g);
    	
    	thirdFloorButton = new JButton("3rd floor");
    	fourthFloorButton = new JButton("4th floor");
    	thirdFloorButton.addActionListener(this);
    	fourthFloorButton.addActionListener(this);
    	
    	g.gridx = 0;
    	g.gridy = 1;
    	add(thirdFloorButton, g);
    	
    	g.gridx = 0;
    	g.gridy = 2;
    	add(fourthFloorButton, g);

    	g.gridx = 1;
    	g.gridy = 1;
    	floorComponents = new JPanel();
    	floorComponents.setLayout(new GridBagLayout());
    	add(floorComponents, g);
    	
    	g.gridx = 2;
    	g.gridy = 0;
    	clickedRangeStart = new JLabel("Start: ");
    	add(clickedRangeStart, g);
    	
    	g.gridy = 1;
    	clickedRangeEnd = new JLabel("End: ");
    	add(clickedRangeEnd, g);
    	
    	g.gridy = 2;
    	add(new JLabel("Days since checked:"), g);
    	
    	g.gridx = 3;
    	g.gridy = 0;
    	currentRangeStart = new JLabel("<start>");
    	add(currentRangeStart, g);
    	
    	g.gridx = 4;    	
    	newRangeStart = new JTextField("", 10);
    	add(newRangeStart, g);

    	g.gridx = 3;
    	g.gridy = 1;
    	currentRangeEnd = new JLabel("<end>");
    	add(currentRangeEnd, g);
    	
    	g.gridx = 4;
    	newRangeEnd = new JTextField("", 10);
    	add(newRangeEnd, g);

    	g.gridx = 3;
    	g.gridy = 2;
    	currentDaysSinceChecked = new JLabel("<days>");
    	add(currentDaysSinceChecked, g);
    	
    	// TODO Add a max number of columns on days since checked input.
    	g.gridx = 4;
    	newDaysSinceChecked = new JTextField("", 3);
    	newDaysSinceChecked.setColumns(3);
    	add(newDaysSinceChecked, g);
    	
    	g.gridx = 5;
    	submitData = new JButton("Submit");
    	submitData.addMouseListener(this);
    	submitData.setEnabled(false);
    	add(submitData, g);
    	
    	// TODO Grey out cancel when it's not possible to cancel.
    	g.gridx = 6;
    	g.gridy = 0;
    	cancel = new JButton("Cancel");
    	cancel.addMouseListener(this);
    	cancel.setEnabled(false);
    	add(cancel, g);
    	
    	allowInput(false);
    	fCConstraints = new GridBagConstraints();
    }

    /** Display a given Floor f's Ranges on this GUI. */
    public void displayFloor(Floor f) {
    	if (f != thirdFloor && f!= fourthFloor) {
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
    			
    			if (r.getDaysSinceChecked() < 15) {
    				r.setForeground(Color.GREEN);
    			}
    			else if (r.getDaysSinceChecked() >= 15 && r.getDaysSinceChecked() < 30) {
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
    
    /** Allow or disallow input on the GUI. */
    public void allowInput(Boolean b) {
    	newRangeStart.setEnabled(b);
    	newRangeEnd.setEnabled(b);
    	newDaysSinceChecked.setEnabled(b);
    	submitData.setEnabled(b);
    	// TODO Add cancel.setEnabled(b) here? 
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    	JButton j = (JButton) e.getSource();

    	if (j.getText().equals("3rd floor") || j.getText().equals("4th floor")) {

    		if (j.getText().equals("3rd floor") && currentFloor != thirdFloor) {
    			currentFloor = thirdFloor;
    		}
    		else if (j.getText().equals("4th floor") && currentFloor != fourthFloor) {
    			currentFloor = fourthFloor;
    		}
    		
    		displayFloor(currentFloor);
    		currentFloor.write();
    	}
    }

    /** Allow the user to change the clicked on Range on the viewed Floor's properties. */
    @Override
    public void mouseClicked(MouseEvent e) {
    	// TODO Clear the contents of rangeStart, rangeEnd, and daysSinceChecked when floor view is switched.
    	// TODO Set a minimum width on the JLabels that show current range values so the UI doesn't jump around on Range mouseover.
    	
    	// Select the clicked-on Range for editing.
    	if (e.getComponent() instanceof com.heatmap.Range) {
	    	Range r = (Range) e.getSource();
	    	clickedRange = r;
	    	clickedRangeStart.setText("Start (" + r.getXCoord() + "," + r.getYCoord() + "):");
	    	currentRangeStart.setText(r.getStart());
	    	clickedRangeEnd.setText("End (" + r.getXCoord() + "," + r.getYCoord() + "):");
	    	currentRangeEnd.setText(r.getEnd());
	    	currentDaysSinceChecked.setText(Integer.toString(r.getDaysSinceChecked()));
	    	cancel.setEnabled(true);
	    	rangeClicked = true;
	    	allowInput(true);
    	}
    	
    	// Change the values of a Range's properties depending on user input.
    	else {
    		JButton button = (JButton) e.getComponent();

    		if (button == submitData) {
    			
    			if (newRangeStart.getText().equals("") == false) {
    				clickedRange.setStart(newRangeStart.getText());
    			}
    			if (newRangeEnd.getText().equals("") == false) {
    				clickedRange.setEnd(newRangeEnd.getText());
    			}
    			if (newDaysSinceChecked.getText().equals("") == false) {
    				clickedRange.setLastChecked(newDaysSinceChecked.getText());
    			}
    		}
    		else if (button == cancel) {
    			System.out.println("Cancel!");
    			button.setEnabled(false);
    			allowInput(false);
    		}

    		clearInput();
    		rangeClicked = false;
    		clickedRange = null;
    		displayFloor(currentFloor);
    		currentFloor.write();
    	}
    }

    /** Update the GUI components that display information about the 
     * currently moused-over Range. */
    @Override
    public void mouseEntered(MouseEvent e) {
    	if (rangeClicked == false && e.getComponent() instanceof com.heatmap.Range) { 
   			Range r = (Range) e.getSource();
   			clickedRangeStart.setText("Start (" + r.getXCoord() + "," + r.getYCoord() + "):");
   			currentRangeStart.setText(r.getStart());
   			clickedRangeEnd.setText("End (" + r.getXCoord() + "," + r.getYCoord() + "):");
   			currentRangeEnd.setText(r.getEnd());
   			currentDaysSinceChecked.setText(Integer.toString(r.getDaysSinceChecked()));
    	}
    }
    
    // TODO Try to find a way of using MouseListener or ActionListener, rather than both.
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