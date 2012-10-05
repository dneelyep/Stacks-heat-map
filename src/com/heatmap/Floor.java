package com.heatmap;

import java.util.ArrayList;

/** Class to represent a floor in the library. */
public class Floor {
	private ArrayList ranges;

	/** Create a new Floor with numRanges ranges. */
	public Floor(int numRanges) {
		ranges = new ArrayList(numRanges);
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