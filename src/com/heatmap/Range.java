package com.heatmap;

/** Class to represent a single range of books in the library. */
public class Range {
	/** The first call number in this Range. */
	private String startCallNumber;
	
	/** The last call number in this Range. */
	private String endCallNumber;
	
	/** The number of days since this range has last had a pickup done on it. */
	private int daysSinceLastChecked;
	
	/** This Range's x-coordinate in the GUI. */
	private final int XCOORD;
	
	/** This Range's y-coordinate in the GUI. */
	private final int YCOORD;
	
	public Range(int x, int y) {
		XCOORD = x;
		YCOORD = y;
	}
	
	/** Get this Range's x-coordinate. */
	public int getXCoord() {
		return XCOORD;
	}
	
	/** Get this Range's y-coordinate. */
	public int getYCoord() {
		return YCOORD;		
	}
	
}
