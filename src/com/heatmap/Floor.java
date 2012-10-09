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
		makeFloor(floorNumber);
	}

	/** Create a set of Ranges that represents a Floor of the library. */
	public void makeFloor(int floorNumber) {
		if (floorNumber == 3 || floorNumber == 4) {
			String floorFilePath = "../res/floorData/";
			// TODO Is it necessary to initialize ranges to a certain size?
			if (floorNumber == 3) {
				floorFilePath += "thirdFloor.xml";
				ranges = new ArrayList<>(163);
			}
			else { 
				floorFilePath += "fourthFloor.xml";
				ranges = new ArrayList<>(93);
			}
						
			// TODO There has to be a better way of representing a constant object than 
			// only allowing it to be initialized once as I'm doing here. Maybe an enumeration?
			// Or make ranges final, so it can't be overridden later?
			if (initialized == false) {
	    		try {
	    	    	Document doc = new Builder().build(floorFilePath);
	    			Element root = doc.getRootElement();
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