package com.heatmap;

import java.io.IOException;
import java.util.ArrayList;

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

	/** Create a new Floor using floorNumber to determine layout. */
	public Floor(int floorNumber) {
		if (floorNumber == 3) {
			ranges = new ArrayList<>(163);
			makeThirdFloor();
		}
		else if (floorNumber == 4) {
			ranges = new ArrayList<>(93);
			makeFourthFloor();
		}
	}
	
	/** Create a set of Ranges that represents the third floor of the library. */
	public void makeThirdFloor() {
		// TODO There has to be a better way of representing a constant object than 
		// only allowing it to be initialized once as I'm doing here. Maybe an enumeration?
		// Or make ranges final, so it can't be overridden later?
		if (initialized == false) {
    		try {
    	    	Document doc = new Builder().build("../res/floorData/thirdFloor.xml");
    			Element root = doc.getRootElement();
    			Elements rangeElements = root.getChildElements();
    			
    			for (int i = 0; i < rangeElements.size(); i++) {
    				String tmpX = rangeElements.get(i).getFirstChildElement("x").getValue();
    				String tmpY = rangeElements.get(i).getFirstChildElement("y").getValue();
    				ranges.add(new Range(Integer.parseInt(tmpX), Integer.parseInt(tmpY)));
    				// TODO Also use the following to set other parameters.
//    				String tmpBegin = ranges.get(i).getFirstChildElement("begin").getValue();
//    				String tmpEnd = ranges.get(i).getFirstChildElement("end").getValue();
//    				String tmpLastChecked= ranges.get(i).getFirstChildElement("last-checked").getValue();
//    				System.out.println("x: " + tmpX + " y: " + tmpY
//    			                     + " Begin: " + tmpBegin + " End: " + tmpEnd 
//    						         + " Last checked: " + tmpLastChecked);
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

	// TODO Try to make a more general makeFloor() method.
    /** Create a set of Ranges that represents the fourth floor of the library. */
    public void makeFourthFloor() {
    	if (initialized == false) {
    		try {
    	    	Document doc = new Builder().build("../res/floorData/fourthFloor.xml");
    			Element root = doc.getRootElement();
    			Elements rangeElements = root.getChildElements();
    			
    			for (int i = 0; i < rangeElements.size(); i++) {
    				String tmpX = rangeElements.get(i).getFirstChildElement("x").getValue();
    				String tmpY = rangeElements.get(i).getFirstChildElement("y").getValue();
    				ranges.add(new Range(Integer.parseInt(tmpX), Integer.parseInt(tmpY)));
    				// TODO Also use the following to set other parameters.
//    				String tmpBegin = ranges.get(i).getFirstChildElement("begin").getValue();
//    				String tmpEnd = ranges.get(i).getFirstChildElement("end").getValue();
//    				String tmpLastChecked= ranges.get(i).getFirstChildElement("last-checked").getValue();
//    				System.out.println("x: " + tmpX + " y: " + tmpY
//    			                     + " Begin: " + tmpBegin + " End: " + tmpEnd 
//    						         + " Last checked: " + tmpLastChecked);
    			}
    		}
        	catch (ParsingException e) {
        		System.err.println("Error parsing this Floor's XML file.");
        	}
        	catch (IOException e) {
        		System.err.println("IOException: " + e);
        	}

    		// TODO Add in the empty space so the map looks a bit closer to the layout?
    	}
    	initialized = true;
    }
    
    /** Get an ArrayList of this Floor's Ranges.*/
    public ArrayList<Range> getRanges() {
    	return ranges;
    }
    
    /** Return this Floor's data file. */
    public Document getFloorDataFile() {
    	return floorDataFile;    	
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