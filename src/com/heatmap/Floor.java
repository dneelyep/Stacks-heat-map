package com.heatmap;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    // TODO Convert this path to a relative path. See System.getEnv()
    private String floorPath = "C:/Users/Daniel/Desktop/Programming/Java/Stacks-heat-map/bin/res/floorData/";
	
	/** Radio button used to display this Floor in the GUI. */
	private JRadioButton button;

    /** The GUI that holds this Floor. */
    private GUI parentGUI;
	
	/** Create a new Floor using floorNumber to determine layout. */
	public Floor(Floor_Values floorNumber, GUI gui) {
        parentGUI = gui;
		makeFloor(floorNumber);

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
	private void makeFloor(Floor_Values floor) {
        if (!initialized) {
            if (floor == Floor_Values.third) {
				floorPath += "thirdFloor.xml"; // TODO Store this data in the enum. Or not, will get rid of it soon.
                button = parentGUI.initWithCoords(new JRadioButton("3rd floor"), new Point(0, 1));
			}
			else {
                floorPath += "fourthFloor.xml";
                button = parentGUI.initWithCoords(new JRadioButton("4th floor"), new Point(0, 2));
			}

            read();

            // TODO There has to be a better way of representing a constant object than
            // only allowing it to be initialized once as I'm doing here. Maybe an enumeration?
            // Or make ranges final, so it can't be overridden later? Or make this object final?
            initialized = true;
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

			DateFormat formatter = DateFormat.getDateInstance();

            // Append the current Range's last checked Dates to the current Range element in the data file.
            int counter = 4;
            // TODO BUG HERE. Program keeps trying to get the Dates faced/dusted/read/etc on Ranges that have not had those values initialized.
            for (Activities activity : Activities.values()) {
                if (programRange.getDayLast(activity) != null)
                    fileRangeChildren.get(counter).appendChild(formatter.format(programRange.getDayLast(activity)));
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

    /** Attempt to read in this Floor's Range data from the data file. */
    private void read() {
        try {
            floorDataFile = new Builder().build(floorPath);
            Element root = floorDataFile.getRootElement();
            Elements rangeElements = root.getChildElements();

            // Make an array of Ranges, that's as large as the number of
            // Ranges stored in the data file.
            ranges = new ArrayList<>(rangeElements.size());

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
                for (Activities activity : Activities.values()) {
                    try {
                        programRange.setDayLast(activity, formatter.parse(e.getFirstChildElement(activity.toString()).getValue()));
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

        // LEFTOFFHERE: Imported all data into database, and now working on my Database class, to let me query for data.
        // Now I need to use that class to parse floor data for ranges, and replace my current XML parsing with SQL
        // queries. Once that's done, I should be able to remove my XML storage and use the database for everything in
        // the project.
        Database.executeQuery();
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
    // TODO Convert this to take Activities as an argument.
    public void updateRangeColors(String activity) {
        for (Range r : ranges) {
            r.updateColor(activity);
        }
    }
}

enum Floor_Values {
    third, fourth;
}