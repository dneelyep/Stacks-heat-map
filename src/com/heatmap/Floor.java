package com.heatmap;

import java.util.ArrayList;

/** Class to represent a floor in the library. */
public class Floor {
	
	/** An array of Ranges that represent every range on this Floor. */
	private ArrayList<Range> ranges;
	
	/** Value to indicate whether or not this Floor has yet been created. */
	private Boolean initialized = false;

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

			for (int x = 1; x < 3; x++) {
				for (int y = 6; y < 19; y++) {
					ranges.add(new Range(x, y));
				}
				
				for (int y = 20; y < 35; y++) {
					ranges.add(new Range(x, y));
				}
			}
			
			for (int y = 0; y < 7; y++) {
				ranges.add(new Range(3, y));
			}
			
			for (int y = 0; y < 24; y++) {
				ranges.add(new Range(5, y));
			}
			
			for (int x = 6; x < 8; x++) {
				for (int y = 0; y < 12; y++) {
					ranges.add(new Range(x, y));
				}
				
				for (int y = 13; y < 24; y++) {
					ranges.add(new Range(x, y));					
				}
				
				for (int y = 25; y < 40; y++) {
					ranges.add(new Range(x, y));					
				}
			}
		}

		initialized = true;
	}
    
    /** Create a set of Ranges that represents the fourth floor of the library. */
    public void makeFourthFloor() {
    	if (initialized == false) {
    		// Add the bottom left ranges.
    		for (int y = 14; y < 29; y++) {
    			ranges.add(new Range(1, y));
    			ranges.add(new Range(2, y));
    		}	
    	
    		// Add the top right ranges.
    		for (int y = 2; y < 13; y++) {
    			ranges.add(new Range(5, y));
    			ranges.add(new Range(6, y));
    			ranges.add(new Range(7, y));
    		}
    	
    	
    		// Add the bottom right ranges.
    		for (int y = 14; y < 29; y++) {
    			ranges.add(new Range(5, y));
    			ranges.add(new Range(7, y));
    		}
    	
    		// Add the top right three shelves.
    		for (int x = 5; x < 8; x++) {
    			ranges.add(new Range(x, 0));
    		}
    	
    		// TODO Add in the below empty space?
    	
    		// Add the empty chairs along the left side of the floor.
    		/*fCConstraints.gridx = 0;
    		for (int y = 0; y < 29; y++) {
    		    fCConstraints.gridy = y;
    		    floorComponents.add(new JLabel("Chairs   "), fCConstraints);
    		}
    	
	    	// Add the middle stretch of empty space on the fourth floor.
	    	for (int x = 3; x < 5; x++) {
				fCConstraints.gridx = x;
	    		for (int y = 0; y < 29; y++) {
	    			fCConstraints.gridy = y;
	    			floorComponents.add(new JLabel("   Middle stretch   "), fCConstraints);
	    		}
	    	}
    	
	    	fCConstraints.gridy = 1;
	    	for (int x = 5; x < 8; x++) {
	    		fCConstraints.gridx = x;
	    		floorComponents.add(new JLabel("Space"), fCConstraints);
	    	}
	    	
	   	
	    	fCConstraints.gridy = 13;
	    	for (int x = 5; x < 8; x++) {
	    		fCConstraints.gridx = x;
	    		floorComponents.add(new JLabel("Space"), fCConstraints);
	    	}
	    	
	    	// Add the chairs on the right side of the fourth floor.
	    	fCConstraints.gridx = 8;
	    	for (int y = 0; y < 29; y++) {
	    		fCConstraints.gridy = y;
	    		floorComponents.add(new JLabel("   Chairs"), fCConstraints);
	    	}*/
    	}
    	initialized = true;
    }
    
    /** Get an ArrayList of this Floor's Ranges.*/
    public ArrayList<Range> getRanges() {
    	return ranges;
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