package com.heatmap;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;

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
	
	/** Label that displays the starting call number for the 
	 * currently moused-over Range. */
	private JLabel rangeStart;
	
	/** Label that displays the ending call number for the 
	 * currently moused-over Range. */
	private JLabel rangeEnd;

	/** Label that displays the number of days since the currently 
	 * moused-over Range has been checked. */
	private JLabel daysSinceChecked;

	/** Constraints used to place components in floorComponents. */
	private GridBagConstraints fCConstraints;
	
	/** The Floor currently being viewed by the user. */
	private Floor currentFloor = null;

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
    	add(new JLabel("Range start:"), g);
    	
    	g.gridy = 1;
    	add(new JLabel("Range end:"), g);
    	
    	g.gridy = 2;
    	add(new JLabel("Days since checked:"), g);
    	
    	g.gridx = 3;
    	g.gridy = 0;
    	rangeStart = new JLabel("<start>");
    	add(rangeStart, g);
    	
    	g.gridy = 1;
    	rangeEnd = new JLabel("<end>");
    	add(rangeEnd, g);
    	
    	g.gridy = 2;
    	daysSinceChecked = new JLabel("<dayssincechecked>");
    	add(daysSinceChecked, g);
    	
    	// TODO Put this file reading code into a method so I can use it with various Floors.
    	try {
    		Builder parser = new Builder();
    		Document doc = parser.build("../res/floorData/thirdFloor.xml");
    		
    		Element root = doc.getRootElement();
    		Elements ranges = root.getChildElements();
    		
    		// TODO Rename last-accessed in XML files to last-checked.
    		for (int i = 0; i < ranges.size(); i++) {
    			String tmpX = ranges.get(i).getFirstChildElement("x").getValue();
    			String tmpY = ranges.get(i).getFirstChildElement("y").getValue();
    			String tmpBegin = ranges.get(i).getFirstChildElement("begin").getValue();
    			String tmpEnd = ranges.get(i).getFirstChildElement("end").getValue();
    			String tmpLastAccessed = ranges.get(i).getFirstChildElement("last-accessed").getValue();
    			System.out.println("x: " + tmpX + " y: " + tmpY
    		                     + " Begin: " + tmpBegin + " End: " + tmpEnd 
    					         + " Last accessed: " + tmpLastAccessed);
    		}
    	}
    	catch (ParsingException e) {
    		System.err.println("Error parsing XML file.");
    	}
    	catch (IOException e) {
    		System.err.println("IOException: " + e);
    	}

    	fCConstraints = new GridBagConstraints();
    }

    /** Display a given Floor f's Ranges on this GUI. */
    public void displayFloor(Floor f) {
    	if (f != thirdFloor && f!= fourthFloor) {
    		System.out.println("Error! Tried to display a Floor that does not exist.");
    	}
    	else {
    		floorComponents.removeAll();
    		
    		for (Range r : f.getRanges()) {
    			fCConstraints.gridx = r.getXCoord();
    			fCConstraints.gridy = r.getYCoord();
    			r.addMouseListener(this);
    			floorComponents.add(r, fCConstraints);
    		}
    		
    		floorComponents.revalidate();
    	}
    }

    public void actionPerformed(ActionEvent e) {
    	JButton j = (JButton) e.getSource();
    	System.out.println(j.getText());
    	
    	if (j.getText().equals("3rd floor") && currentFloor != thirdFloor) {
    		displayFloor(thirdFloor);
    		currentFloor = thirdFloor;
    	}
    	else if (j.getText().equals("4th floor") && currentFloor != fourthFloor) {
    		displayFloor(fourthFloor);
    		currentFloor = fourthFloor;
    	}
    }

    public void mouseClicked(MouseEvent e) {
    	
    }

    /** Update the GUI components that display information about the 
     * currently moused-over Range. */
    public void mouseEntered(MouseEvent e) {
    	Range r = (Range) e.getSource();
    	rangeStart.setText(r.getRangeStart());
    	rangeEnd.setText(r.getRangeEnd());
    	daysSinceChecked.setText(Integer.toString(r.getDaysSinceChecked()));
    }
    
    // TODO Try to find a way of using MouseListener or ActionListener, rather than both.
    public void mouseExited(MouseEvent e) {
    }
    
    public void mousePressed(MouseEvent e) {
    }
    
    public void mouseReleased(MouseEvent e) {
    }
}