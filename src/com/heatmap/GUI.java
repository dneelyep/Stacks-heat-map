package com.heatmap;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

/** GUI.java - Class that holds the primary GUI for the program.
 *
 * @author Daniel Neel */
public class GUI extends JApplet {
	
	/** Button to allow the user to view the 3rd floor's status. */
	private JButton thirdFloor;
	
	/** Button to allow the user to view the 4th floor's status. */
	private JButton fourthFloor;

    /** <Main method javadocs> */
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
    	setLayout(new GridBagLayout());
    	GridBagConstraints g = new GridBagConstraints();

    	g.gridx = 2;
    	g.gridy = 0;
    	add(new JLabel("Stacks Cleanliness Heat Map"), g);
    	
    	g.gridx = 2;
    	g.gridy = 1;
    	add(new JLabel(new ImageIcon("../res/bin/fourthFloor.png")), g);
    	
    	thirdFloor = new JButton("3rd floor");
    	fourthFloor = new JButton("4th floor");
    	
    	g.gridx = 0;
    	g.gridy = 2;
    	add(thirdFloor, g);
    	
    	g.gridx = 1;
    	g.gridy = 2;
    	add(fourthFloor, g);
    }
}