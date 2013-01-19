package com.heatmap;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Statement;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;

import javax.swing.JRadioButton;

import com.sun.deploy.util.UpdateCheck;
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

    /** The value of this Floor. */
    private Floor_Values floorValue;
	
	/** Create a new Floor using floorNumber to determine layout. */
	public Floor(Floor_Values floorNumber, GUI gui) {
        floorValue = floorNumber;
        parentGUI = gui;
		makeFloor(floorValue);

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
		/*Element root = floorDataFile.getRootElement();
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
        }*/

        // SQL
         for (Range range : ranges) {

             // LEFTOFFHERE: In the process of converting data storage over to using MySQL. Got the data stored and I'm
             // reading it correctly, now I'm working on storing the data (writing it) in the database here.
             //"SELECT * FROM stacks_heat_map_db.book_range WHERE parent_floor = " + (floorValue == Floor_Values.third ? 1 : 2));
             /*range.getStart()
             range.getEnd()*/

             "SELECT text_value FROM call_number"

             System.out.println(
                     "UPDATE shelf_column SET shelf_column.start_call_number=" + range.getStart() + " shelf_column.end_call_number=" + range.getEnd() +
                     " FROM shelf_column JOIN book_range ON shelf_column.parent_range = book_range.id" +
                     " WHERE book_range.x_coord=" + range.getXCoord() + " AND book_range.y_coord=" + range.getYCoord());

             /*DateFormat formatter = DateFormat.getDateInstance();

             // Append the current Range's last checked Dates to the current Range element in the data file.
             int counter = 4;
             // TODO BUG HERE. Program keeps trying to get the Dates faced/dusted/read/etc on Ranges that have not had those values initialized.
             for (Activities activity : Activities.values()) {
                 if (programRange.getDayLast(activity) != null)
                     fileRangeChildren.get(counter).appendChild(formatter.format(programRange.getDayLast(activity)));
             }*/
         }
	}

    /** Attempt to read in this Floor's Range data from the data file. */
    private void read() {
        // TODO REMOVE ME when I'm finished converting data storage to the database.
        /*try {
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
        }*/

        Statement statement = null;
        ResultSet results = null;
        try {
            statement = Database.getConnection().createStatement();
            results = statement.executeQuery(
                    "SELECT * FROM stacks_heat_map_db.book_range WHERE parent_floor = " + (floorValue == Floor_Values.third ? 1 : 2));

            // TODO Make this a method to get size of a result set?
            results.last();
            ranges = new ArrayList<>(results.getRow());
            results.first();

            do {
                ranges.add(new Range(results.getInt("x_coord"), results.getInt("y_coord"), parentGUI));
                // TODO Since I'm storing the range data in columns, I can't really set start/end values for ranges yet.
                //ranges.get(ranges.size()).setStart(results.getString());
            } while (results.next());

        } catch (SQLException e ) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try { statement.close(); }
                catch (SQLException e) { e.printStackTrace(); }
            }
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