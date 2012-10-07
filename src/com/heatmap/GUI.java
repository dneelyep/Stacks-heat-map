package com.heatmap;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/** GUI.java - Class that holds the primary GUI for the program.
 *
 * @author Daniel Neel */
public class GUI extends JApplet implements ActionListener {
	
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
	
	/** Constraints used to place components in floorComponents. */
	private GridBagConstraints fCConstraints;
	
	/** The Floor currently being viewed by the user. */
	private int currentFloor = 0;

    /** TODO <Main method javadocs> */
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
    	
    	fCConstraints = new GridBagConstraints();
    }
    
    /** Display the GUI components for the third floor of the library. */
    public void makeThirdFloor() {
    	floorComponents.removeAll();
    	
    	fCConstraints.gridx = 0;
    	fCConstraints.gridy = 0;
    	floorComponents.add(new JLabel("This is the 3rd floor!"), fCConstraints);

    	fCConstraints.gridx = 1;
    	fCConstraints.gridy = 1;
    	floorComponents.add(new JLabel("Second test label"), fCConstraints);
    	
    	fCConstraints.gridx = 2;
    	fCConstraints.gridy = 2;
    	floorComponents.add(new JButton("And third label"), fCConstraints);
    	floorComponents.revalidate();
    }
    
    /** Display the GUI components for the fourth floor of the library. */
    public void makeFourthFloor() {
    	floorComponents.removeAll();
    	
    	for (Range r : fourthFloor.getRanges()) {
    		fCConstraints.gridx = r.getXCoord();
    		fCConstraints.gridy = r.getYCoord();
    		floorComponents.add(new JLabel("Range (" + r.getXCoord() + "," + r.getYCoord() + ")"), fCConstraints);
    	}

    	floorComponents.revalidate();
    }
    
    public void actionPerformed(ActionEvent e) {
    	JButton j = (JButton) e.getSource();
    	System.out.println(j.getText());
    	
    	if (j.getText().equals("3rd floor") && currentFloor != 3) {
    		makeThirdFloor();
    		currentFloor = 3;
    	}
    	else if (j.getText().equals("4th floor") && currentFloor != 4) {
    		makeFourthFloor();
    		currentFloor = 4;
    	}
    }
}