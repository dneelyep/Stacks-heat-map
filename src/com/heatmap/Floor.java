package com.heatmap;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;

import javax.swing.JRadioButton;

import nu.xom.*;

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
	
	/** Radio button used to display this Floor in the GUI. */
	private JRadioButton button;

    /** The GUI that holds this Floor. */
    private GUI parentGUI;
	
	/** Create a new Floor using floorNumber to determine layout. */
	public Floor(int floorNumber, GUI gui) {
        parentGUI = gui;
		makeFloor(floorNumber);

        // TODO Could also use a changelistener, something more appropriate to radiobuttons.
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                parentGUI.setCurrentFloor(Floor.this);
                parentGUI.setInputMode(false);
                parentGUI.displayFloor(Floor.this);
                write();
            }
        });
	}

	/** Create a set of Ranges that represents a Floor of the library. */
	private void makeFloor(int floorNumber) {
		if ((floorNumber == 3 || floorNumber == 4) && !initialized) {
			if (floorNumber == 3) {
                // TODO Convert these paths to relative paths or similar.
				floorPath = "C:/Users/Daniel/Desktop/Programming/Java/Stacks-heat-map/res/floorData/thirdFloor.xml";
                button = parentGUI.initWithCoords(new JRadioButton("3rd floor"), 0, 1);
			}
			else {
                floorPath = "C:/Users/Daniel/Desktop/Programming/Java/Stacks-heat-map/res/floorData/fourthFloor.xml";
                button = parentGUI.initWithCoords(new JRadioButton("4th floor"), 0, 2);
			}

            // TODO There has to be a better way of representing a constant object than
			// only allowing it to be initialized once as I'm doing here. Maybe an enumeration?
			// Or make ranges final, so it can't be overridden later? Or make this object final?
            // TODO Break this up into a separate read() method?
    		try {
    	    	floorDataFile = new Builder().build(floorPath);
    			Element root = floorDataFile.getRootElement();
    			Elements rangeElements = root.getChildElements();

    			// Make an array of Ranges, that's as large as the number of
    			// Ranges stored in the data file.
    			ranges = new ArrayList<Range>(rangeElements.size());

    			for (int i = 0; i < rangeElements.size(); i++) {
    				Element e = rangeElements.get(i);
    				String fileX = e.getFirstChildElement("x").getValue();
    				String fileY = e.getFirstChildElement("y").getValue();
    				ranges.add(new Range(Integer.parseInt(fileX), Integer.parseInt(fileY), parentGUI));

                    Range programRange = ranges.get(i);
                    programRange.setStart(e.getFirstChildElement("begin").getValue());
    				programRange.setEnd(e.getFirstChildElement("end").getValue());

                    DateFormat formatter = DateFormat.getDateInstance();
                    // TODO Since the problem is that we're reading an unparseable (empty)
                    // date, we need to handle that somehow. Set the date to a safe value?
                    for (String activity : parentGUI.getDateControllers().keySet()) {
                        try {
                            programRange.setDayLast(activity, formatter.parse(e.getFirstChildElement(activity).getValue()));
                        } catch (ParseException p) {
                            System.out.println("Error parsing Date " + activity + ": " + p);
                        }
                    }
                }
    		}
        	catch (ParsingException e) {
        		System.err.println("Error parsing this Floor's XML file.");
        	}
        	catch (IOException e) {
        		System.err.println("IOException: " + e);
        	}

            initialized = true;
		}
		else {
			System.out.println("Error! Tried to create a Floor that does not exist.");			
		}
	}
	
	/** Store the information for every Range on this Floor 
	 * into this Floor's data file. */
	 void write() {
		Element root = floorDataFile.getRootElement();
		Elements fileRanges = root.getChildElements();

         // Iterate through every Range in the file.
		for (int i = 0; i < fileRanges.size(); i++) {
            Range programRange = ranges.get(i);
			Element fileRange = fileRanges.get(i);
			Elements fileRangeChildren = fileRange.getChildElements();
			
			// Get rid of current <range> children in the file.
            // TODO Rather than remove elements, how about just replacing them with new ones?
			for (int j = 0; j < fileRangeChildren.size(); j++) {
				fileRangeChildren.get(j).removeChildren();
			}

			// Replace each range's attributes in the file with each object's attributes.
			fileRangeChildren.get(0).appendChild(Integer.toString(programRange.getXCoord()));
			fileRangeChildren.get(1).appendChild(Integer.toString(programRange.getYCoord()));
			fileRangeChildren.get(2).appendChild(programRange.getStart());
			fileRangeChildren.get(3).appendChild(programRange.getEnd());

			// TODO Make formatter a field?
			DateFormat formatter = DateFormat.getDateInstance();

            // Append the current Range's last checked Dates to the current Range element in the data file.
            int counter = 4;
            for (String s : parentGUI.getDateControllers().keySet()) {
                if (programRange.getDayLast(s) != null) {
                    fileRangeChildren.get(counter).appendChild(formatter.format(programRange.getDayLast(s)));
                }
                counter++;
            }
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

    /** Get this Floor's button. */
    public JRadioButton getButton() {
    	return button;
    }

    /** Update the colors of every Range located on this Floor according
     * to activity. */
    public void updateRangeColors(String activity) {
        for (Range r : ranges) {
            r.updateColor(activity);
        }
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