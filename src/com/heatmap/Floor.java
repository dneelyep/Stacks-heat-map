package com.heatmap;

import java.util.ArrayList;

/** Class to represent a floor in the library. */
public class Floor {
	private ArrayList<Range> ranges;

	/** Create a new Floor using floorNumber to determine layout. */
	public Floor(int floorNumber) {
		if (floorNumber == 3) {
			ranges = new ArrayList<Range>(163);
			//makeThirdFloor();
		}
		else if (floorNumber == 4) {
			ranges = new ArrayList<Range>(93);
			makeFourthFloor();
		}
	}
    
    /** Create a set of Ranges that represents the fourth floor of the library. */
    public void makeFourthFloor() {
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