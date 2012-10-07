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
	private JButton thirdFloor;
	
	/** Button to allow the user to view the 4th floor's status. */
	private JButton fourthFloor;
	
	/** Panel that contains the components for the shelf map. */
	private JPanel floorComponents;
	
	/** The Floor currently being viewed by the user. */
	private int currentFloor = 0;

    /** TODO <Main method javadocs> */
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

    	g.gridx = 1;
    	g.gridy = 0;
    	add(new JLabel("Stacks Cleanliness Heat Map"), g);
    	
    	thirdFloor = new JButton("3rd floor");
    	fourthFloor = new JButton("4th floor");
    	thirdFloor.addActionListener(this);
    	fourthFloor.addActionListener(this);
    	
    	g.gridx = 0;
    	g.gridy = 1;
    	add(thirdFloor, g);
    	
    	g.gridx = 0;
    	g.gridy = 2;
    	add(fourthFloor, g);

    	g.gridx = 1;
    	g.gridy = 1;
    	floorComponents = new JPanel();
    	add(floorComponents, g);
    	
    	JLabel test = new JLabel("Test");
    	floorComponents.add(test);
    	
    	makeFourthFloor();
    }
    
    /** Display the GUI components for the third floor of the library. */
    public void makeThirdFloor() {
    	floorComponents.removeAll();
    	floorComponents.add(new JLabel("This is the 3rd floor!"));
    	floorComponents.add(new JLabel("Second test label"));
    	floorComponents.add(new JButton("And third label"));
    	floorComponents.revalidate();
    }
    
    /** Display the GUI components for the fourth floor of the library. */
    public void makeFourthFloor() {
    	floorComponents.removeAll();
    	floorComponents.add(new JLabel("4th floor!"));
    	floorComponents.add(new JLabel("Second test label"));
    	floorComponents.add(new JButton("And third Button"));
    	floorComponents.revalidate();
    	//g.gridx = 1;
    	//g.gridy = 14;
    	
    	//for (int i = 14; i < 29; i++) {
    	//	g.gridy = i;
    	//	add(new JLabel("Range (" + g.gridx + "," + g.gridy + ")"), g);
    	//}
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