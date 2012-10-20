package com.heatmap;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.jdesktop.swingx.JXDatePicker;

/** GUI.java - Class that holds the primary GUI for the program.
 *
 * @author Daniel Neel */
public class GUI extends JApplet implements MouseListener {
    /** Floor that represents the library's third floor. */
    private Floor thirdFloor;

    /** Floor that represents the library's fourth floor. */
    private Floor fourthFloor;
    
    /** The group that radio buttons for each Floor belongs to. */
    private ButtonGroup floorButtons = new ButtonGroup();

    /** Panel that contains the components for the shelf map. */
    private JPanel floorComponents;

    /** The Range currently clicked on to be edited by the user. */
    private Range clickedRange;

    /** Field to change the clicked-on Range's starting call #. */
    private JTextField startCallNumber = new JTextField("", 10);

    /** Field to change the clicked-on Range's ending call #. */
    private JTextField endCallNumber = new JTextField("", 10);

    /** Label that displays the number of days since the currently
     * moused-over Range has been checked. */
    // TODO Use input verification on the text fields to ensure values are appropriate.
    //      See http://docs.oracle.com/javase/tutorial/uiswing/misc/focus.html#inputVerification
    // TODO Add some checking to make sure the user can't add dates from the future.
    // TODO Fix the bug where, after I click a Range to input data, I can still click another Range.
    private JXDatePicker newDayLastChecked = new JXDatePicker();

    /** Button to change attributes of the currently clicked Range. */
    private JButton submitData = new JButton("Submit");

    /** Constraints used to place components in floorComponents. */
    private GridBagConstraints fCConstraints;

    /** The Floor currently being viewed by the user. */
    private Floor currentFloor = null;

    /** Whether or not a Range has been clicked, to lock editing. */
    private Boolean focused = false;

    /** Button to cancel editing of a Range's information. */
    private JButton cancel = new JButton("Cancel");

    /** A bit of text that displays a title for the program. */
    private JLabel programTitle = new JLabel("Stacks Cleanliness Heat Map");

    /** Attempt to initialize the GUI for this program. */
    @Override
    public void init() {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    createGUI();
                }
            });
        } catch (Exception e) {
            System.err.println("createGUI didn't complete successfully: " + e);
        }
    }

    /** Set up the GUI used for the program.*/
    private void createGUI() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());
        setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.anchor = GridBagConstraints.NORTHWEST;
        g.weightx = 0.3;

        // Initialize the Floors.
        thirdFloor = new Floor(3);
        fourthFloor = new Floor(4);
        addAt(programTitle, 1, 0, g);

        floorButtons.add(thirdFloor.getButton());
        floorButtons.add(fourthFloor.getButton());
        
        thirdFloor.getButton().addMouseListener(this);
        fourthFloor.getButton().addMouseListener(this);

        addAt(thirdFloor.getButton(), thirdFloor.getButtonX(), thirdFloor.getButtonY(), g);
        addAt(fourthFloor.getButton(), fourthFloor.getButtonX(), fourthFloor.getButtonY(), g);

        floorComponents = new JPanel();
        g.gridheight = 36;
        floorComponents.setLayout(new GridBagLayout());
        g.weighty = 1;
        addAt(floorComponents, 1, 1, g);

        g.weighty = 0;
        g.gridheight = 1;
        addAt(new JLabel("Start: "), 2, 0, g);
        addAt(new JLabel("End: "), 2, 1, g);
        addAt(new JLabel("Last checked: "), 2, 2, g);
        addAt(new JLabel("Last shifted: "), 2, 3, g);
        addAt(new JLabel("Last faced: "), 2, 4, g);
        addAt(new JLabel("Last dusted: "), 2, 5, g);
        addAt(new JLabel("Last read: "), 2, 6, g);

        startCallNumber.setMinimumSize(new Dimension(100, 20));
        addAt(startCallNumber, 3, 0, g);
        endCallNumber.setMinimumSize(new Dimension(100, 20));
        addAt(endCallNumber, 3, 1, g);
        addAt(newDayLastChecked, 3, 2, g);
        addAt(new JXDatePicker(), 3, 3, g);
        addAt(new JXDatePicker(), 3, 4, g);
        addAt(new JXDatePicker(), 3, 5, g);
        addAt(new JXDatePicker(), 3, 6, g);

        cancel.addMouseListener(this);
        addAt(cancel, 6, 0, g);

        submitData.addMouseListener(this);
        submitData.setEnabled(false);
        addAt(submitData, 6, 1, g);

        addAt(new JRadioButton("Checked"), 6, 2, g);
        addAt(new JRadioButton("Shifted"), 6, 3, g);
        addAt(new JRadioButton("Faced"), 6, 4, g);
        addAt(new JRadioButton("Dusted"), 6, 5, g);
        addAt(new JRadioButton("Read"), 6, 6, g);

        allowInput(false);
        fCConstraints = new GridBagConstraints();
        fCConstraints.insets = new Insets(0, 2, 0, 2);
    }

    /** Display a given Floor floor's Ranges on this GUI. */
    private void displayFloor(Floor floor) {
        if (floor != thirdFloor && floor != fourthFloor) {
            System.err.println("Error! Tried to display a Floor that does not exist.");
        }
        else {
            floorComponents.removeAll();
            clearInput();
            allowInput(false);

            for (Range r : floor.getRanges()) {
                fCConstraints.gridx = r.getXCoord();
                fCConstraints.gridy = r.getYCoord();
                r.addMouseListener(this);
                floorComponents.add(r, fCConstraints);
            }

            floorComponents.revalidate();
            programTitle.setText("Stacks Cleanliness Heat Map | " + floor.getButton().getText());
        }
    }

    /** Clear all input fields on this GUI of their data. */
    private void clearInput() {
        startCallNumber.setText("");
        endCallNumber.setText("");
        newDayLastChecked.setDate(new Date());
    }

    /** Allow or disallow the user to edit the properties
     * of a clicked-on Range. */
    private void allowInput(Boolean b) {
        startCallNumber.setEnabled(b);
        endCallNumber.setEnabled(b);
        newDayLastChecked.setEnabled(b);
        submitData.setEnabled(b);
        cancel.setEnabled(b);
    }

    /** Allow the user to change the clicked on Range on the viewed Floor's properties. */
    @Override
    public void mouseClicked(MouseEvent e) {
        // Select the clicked-on Range for editing.
        if (e.getComponent() instanceof Range) {
            clickedRange = (Range) e.getSource();
            clickedRange.setForeground(Color.BLUE);
            focused = true;
            allowInput(true);
        }

        // Change the values of a Range's properties depending on user input.
        else if (e.getComponent() instanceof JButton) {
            JButton button = (JButton) e.getComponent();

            if (button == submitData) {
                if (startCallNumber.getText().isEmpty() == false) {
                    clickedRange.setStart(startCallNumber.getText());
                }
                if (endCallNumber.getText().isEmpty() == false) {
                    clickedRange.setEnd(endCallNumber.getText());
                }

                clickedRange.setDayLastChecked(newDayLastChecked.getDate());
                currentFloor.write();
            }

            focused = false;
            clearInput();
            // Remove the clicked Range's color that indicates it is selected.
            clickedRange.updateColor();
            allowInput(false);
        }

        else if (e.getComponent() instanceof JRadioButton) {
            JRadioButton button = (JRadioButton) e.getComponent();

            if (button == thirdFloor.getButton()) {
                currentFloor = thirdFloor;
            }
            else if (button == fourthFloor.getButton()) {
                currentFloor = fourthFloor;
            }
            
            clearInput();
            focused = false;
            displayFloor(currentFloor);
            currentFloor.write();
        }
    }

    // TODO Currently there's a bug with resetting the Cyan text color after selection.
    /** Add a given JComponent c to the GUI at coordinates (x, y). */
    private void addAt(JComponent c, int x, int y, GridBagConstraints g) {
        g.gridx = x;
        g.gridy = y;
        add(c, g);
    }

    /** Update the GUI components that display information about the
     * currently moused-over Range. */
    @Override
    public void mouseEntered(MouseEvent e) {
        if (focused == false && e.getComponent() instanceof Range) {
            Range r = (Range) e.getSource();
            startCallNumber.setText(r.getStart());
            endCallNumber.setText(r.getEnd());
            newDayLastChecked.setDate(r.getDayLastChecked());
            r.setForeground(Color.CYAN);
            // TODO Try to have the date picker display text in a more human-readable format.
            // TODO Implement holding Control to select multiple ranges at once.
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
    	if (focused == false && e.getComponent() instanceof Range) {
    		Range r = (Range) e.getSource();
    		r.updateColor();
    	}
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }
}