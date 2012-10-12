package com.heatmap;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;

/** Class to represent a floor in the library. */
public class Floor {
	
	/** An array of Ranges that represent every range on this Floor. */
	private ArrayList<Range> ranges;
	
	/** Value to indicate whether or not this Floor has yet been created. */
	private Boolean initialized = false;
	
	/** An XML Document that holds the information for each Range object 
	 * on this Floor. */
	private Document floorDataFile;
	
	/** The path to this Floor's data file. */
	private String floorPath;
	
	/** Button used to display this Floor in the GUI. */
	private JButton button;
	
	// TODO [Maybe] Even more clean would be a class that extends JButton and stores x/y values.
	/** The x-coordinate of this Floor's button. */
	private int buttonX;
	
	/** The y-coordinate of this Floor's button. */
	private int buttonY;
	
	/** Create a new Floor using floorNumber to determine layout. */
	public Floor(int floorNumber) {	
		makeFloor(floorNumber);
	}

	/** Create a set of Ranges that represents a Floor of the library. */
	public void makeFloor(int floorNumber) {
		if (floorNumber == 3 || floorNumber == 4) {
			// TODO Rather than initialize ranges to hard-coded values, use the number
			// of elements in the data file.
			if (floorNumber == 3) {
				floorPath = "../res/floorData/thirdFloor.xml";
				ranges = new ArrayList<>(163);
				button = new JButton("3rd floor");
				buttonX = 0;
				buttonY = 1;
			}
			else {
				floorPath = "../res/floorData/fourthFloor.xml";
				ranges = new ArrayList<>(93);
				button = new JButton("4th floor");
				buttonX = 0;
				buttonY = 2;
			}
						
			// TODO There has to be a better way of representing a constant object than 
			// only allowing it to be initialized once as I'm doing here. Maybe an enumeration?
			// Or make ranges final, so it can't be overridden later?
			if (initialized == false) {
	    		try {
	    	    	floorDataFile = new Builder().build(floorPath);
	    			Element root = floorDataFile.getRootElement();
	    			Elements rangeElements = root.getChildElements();
	    			
	    			for (int i = 0; i < rangeElements.size(); i++) {
	    				Element e = rangeElements.get(i);
	    				String tmpX = e.getFirstChildElement("x").getValue();
	    				String tmpY = e.getFirstChildElement("y").getValue();
	    				ranges.add(new Range(Integer.parseInt(tmpX), Integer.parseInt(tmpY)));
	    				ranges.get(i).setStart(e.getFirstChildElement("begin").getValue());
	    				ranges.get(i).setEnd(e.getFirstChildElement("end").getValue());
	    				ranges.get(i).setLastChecked(e.getFirstChildElement("last-checked").getValue());
	    			}
	    		}
	        	catch (ParsingException e) {
	        		System.err.println("Error parsing this Floor's XML file.");
	        	}
	        	catch (IOException e) {
	        		System.err.println("IOException: " + e);
	        	}
			}

			initialized = true;
		}
		else {
			System.out.println("Error! Tried to create a Floor that does not exist.");			
		}
	}
	
	/** Store the information for every Range on this Floor 
	 * into this Floor's data file. */
	public void write() {
		Element root = floorDataFile.getRootElement();
		Elements fileRanges = root.getChildElements();

		for (int i = 0; i < fileRanges.size(); i++) {
			Element rangeEle = fileRanges.get(i);
			Elements rangeEleEles= rangeEle.getChildElements();
			
			// Get rid of current <range> children in the file.
			for (int j = 0; j < rangeEleEles.size(); j++) {
				rangeEleEles.get(j).removeChildren();
			}
			
			// Replace each range's attributes in the file with each object's attributes.
			rangeEleEles.get(0).appendChild(Integer.toString(ranges.get(i).getXCoord()));
			rangeEleEles.get(1).appendChild(Integer.toString(ranges.get(i).getYCoord()));
			rangeEleEles.get(2).appendChild(ranges.get(i).getStart());
			rangeEleEles.get(3).appendChild(ranges.get(i).getEnd());
			rangeEleEles.get(4).appendChild(Integer.toString(ranges.get(i).getLastChecked()));
		}

		floorDataFile.setRootElement(root);

		try {
		  FileWriter fstream = new FileWriter(floorPath);
		  BufferedWriter out = new BufferedWriter(fstream);
		  out.write(floorDataFile.toXML());
		  out.close();
		}
		catch (Exception e) {
		  System.err.println("Error: " + e.getMessage());
		}
	}

    /** Get an ArrayList of this Floor's Ranges.*/
    public ArrayList<Range> getRanges() {
    	return ranges;
    }
    
    /** Return this Floor's data file. */
    public Document getFloorDataFile() {
    	return floorDataFile;    	
    }
    
    /** Get this Floor's button. */
    public JButton getButton() {
    	return button;
    }
    
    /** Get this Floor's button's x-coordinate. */
    public int getButtonX() {
    	return buttonX;
    }
    
    /** Get this Floor's button's y-coordinate. */
    public int getButtonY() {
    	return buttonY;
    }
}

// 3rd floor book shelves:
// 15 * 2 = 30
// 13 * 2 = 26
// 7      =  7
// 12 * 2 = 24
// 11 * 2 = 22
// 24     = 24
// 15 * 2 = 30
// ===========
//         163


// 4th floor book shelves:
// 15 * 2 = 30
// 11 * 3 = 33
// 15 * 2 = 30
// ===========
//          93