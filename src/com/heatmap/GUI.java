package com.heatmap;

import javax.swing.JApplet;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

/** GUI.java - Class that holds the primary GUI for the program.
 *
 * @author Daniel Neel */
public class GUI extends JApplet {

    public static void main(String[] args) {
    	System.out.println("Test again.");
    }

    /** <Main method javadocs> */
    public void init() {
	try {
            SwingUtilities.invokeAndWait(new Runnable() {
            	public void run() {
            		JLabel lbl = new JLabel("YIPPEE! Finally!");
            		add(lbl);
            		JLabel testLabel = new JLabel("Testing.");
            		add(testLabel);
            	}
		});
        } catch (Exception e) {
            System.err.println("createGUI didn't complete successfully");
        }
    }
}
