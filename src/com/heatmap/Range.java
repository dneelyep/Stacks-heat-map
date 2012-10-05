package com.heatmap;

/** Class to represent a single range of books in the library. */
public class Range {
	/** The first call number in this Range. */
	private String startCallNumber;
	
	/** The last call number in this Range. */
	private String endCallNumber;
	
	/** The number of days since this range has last had a pickup done on it. */
	private int daysSinceLastChecked;
}
