package com.heatmap;

import org.jdesktop.swingx.JXDatePicker;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;

import javax.swing.*;

/** GUI.java - Class that holds the primary GUI for the program.
 *
 * @author Daniel Neel */
public class GUI extends JApplet implements MouseListener {
    // TODO Add a Quit button to the GUI?
    /** Floor that represents the library's third floor. */
    private final Floor thirdFloor = new Floor(3);

    /** Floor that represents the library's fourth floor. */
    private final Floor fourthFloor = new Floor(4);
    
    /** The group that radio buttons for each Floor belongs to. */
    private final ButtonGroup floorButtons = new ButtonGroup();

    /** Panel that contains the components for the shelf map. */
    private JPanel floorComponents = addCoords(new JPanel(new GridBagLayout()), 1, 1);

    /** The Range currently clicked on to be edited by the user. */
    private Range focusedRange;

    /** Field to change the clicked-on Range's starting call #. */
    private final JTextField startCallNumber = addCoords(new JTextField("", 10), 3, 0);

    /** Field to change the clicked-on Range's ending call #. */
    private final JTextField endCallNumber = addCoords(new JTextField("", 10), 3, 1);

    /** Date picker to change the Date the clicked Range last had pickup done on it. */
    // TODO Use input verification on the text fields to ensure values are appropriate.
    //      See http://docs.oracle.com/javase/tutorial/uiswing/misc/focus.html#inputVerification
    // TODO Add some checking to make sure the user can't add dates from the future. See my future getDaysSince method.
    private final JXDatePicker dayLastChecked = addCoords(new JXDatePicker(), 3, 2);

    /** Date picker to change the Date the clicked Range was last shifted. */
    private final JXDatePicker dayLastShifted = addCoords(new JXDatePicker(), 3, 3);

    /** Date picker to change the Date the clicked Range was last faced. */
    private final JXDatePicker dayLastFaced = addCoords(new JXDatePicker(), 3, 4);

    /** Date picker to change the Date the clicked Range was last dusted. */
    private final JXDatePicker dayLastDusted = addCoords(new JXDatePicker(), 3, 5);

    /** Date picker to change the Date the clicked Range was last shelf read. */
    private final JXDatePicker dayLastRead = addCoords(new JXDatePicker(), 3, 6);

    // TODO For some reason, the text on Submit/Cancel are not displaying properly when the gui is first loaded.
    /** Button to change attributes of the currently clicked Range. */
    private final JButton submitData = addCoords(new JButton("Submit"), 6, 1);

    /** Button to cancel editing of a Range's information. */
    private final JButton cancel = addCoords(new JButton("Cancel"), 6, 0);

    // TODO Consider associating associating a GUI component with each Date last X. Cleaner potentially.
    /** The collection of input components, gathered together for easy iteration. */
    private final Set<JComponent> inputComponents = new LinkedHashSet<JComponent>();

    /** Constraints used to place components in floorComponents. */
    private GridBagConstraints fCConstraints;

    /** The Floor currently being viewed by the user. */
    private Floor currentFloor = null;

    /** A bit of text that displays a title for the program. */
    private JLabel programTitle = addCoords(new JLabel("Stacks Cleanliness Heat Map"), 1, 0);

    /** Button which, when clicked, will change the colors of Ranges to indicate how many days it's
     * been since each Range has been checked. */
    private JRadioButton viewDaysSinceChecked = addCoords(new JRadioButton("Checked"), 6, 2);

    /** Button which, when clicked, will change the colors of Ranges to indicate how many days it's
     * been since each Range has been shifted. */
    private JRadioButton viewDaysSinceShifted = addCoords(new JRadioButton("Shifted"), 6, 3);

    /** Button which, when clicked, will change the colors of Ranges to indicate how many days it's
     * been since each Range has been faced. */
    private JRadioButton viewDaysSinceFaced = addCoords(new JRadioButton("Faced"), 6, 4);

    /** Button which, when clicked, will change the colors of Ranges to indicate how many days it's
     * been since each Range has been dusted. */
    private JRadioButton viewDaysSinceDusted = addCoords(new JRadioButton("Dusted"), 6, 5);

    /** Button which, when clicked, will change the colors of Ranges to indicate how many days it's
     * been since each Range has been shelf read. */
    private JRadioButton viewDaysSinceRead = addCoords(new JRadioButton("Read"), 6, 6);

    /** Button group to only allow viewing the properties of a single property of Ranges at a time. */
    private ButtonGroup viewDaysSinceButtons = new ButtonGroup();

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
        // Use the system's default look and feel.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e) {
            System.out.println("Exception: " + e);
        }

        // Set the applet's window dimensions to the size of the user's screen.
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());

        setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.anchor = GridBagConstraints.NORTHWEST;
        g.weightx = 0.3;

        addComponent(programTitle, g);

        thirdFloor.getButton().addMouseListener(this);
        fourthFloor.getButton().addMouseListener(this);

        thirdFloor.getButton().setSelected(true);
        floorButtons.add(thirdFloor.getButton());
        floorButtons.add(fourthFloor.getButton());

        // TODO Look into removing the g argument to addComponent, since it's the same for each call of the method.
        addComponent(thirdFloor.getButton(), g);
        addComponent(fourthFloor.getButton(), g);

        g.gridheight = 36;
        g.weighty = 1;
        add(floorComponents, g);

        g.weighty = 0;
        g.gridheight = 1;
        addComponent(addCoords(new JLabel("Start: "), 2, 0), g);
        addComponent(addCoords(new JLabel("End: "),          2, 1), g);
        addComponent(addCoords(new JLabel("Last checked: "), 2, 2), g);
        addComponent(addCoords(new JLabel("Last shifted: "), 2, 3), g);
        addComponent(addCoords(new JLabel("Last faced: "),   2, 4), g);
        addComponent(addCoords(new JLabel("Last dusted: "), 2, 5), g);
        addComponent(addCoords(new JLabel("Last read: "), 2, 6), g);

        startCallNumber.setMinimumSize(new Dimension(100, 20));
        endCallNumber.setMinimumSize(new Dimension(100, 20));

        // Add all input fields to our input components set.
        inputComponents.addAll(Arrays.asList(startCallNumber, endCallNumber,
                dayLastChecked, dayLastShifted, dayLastFaced, dayLastDusted, dayLastRead));

        for (JComponent component : inputComponents) {
            addComponent(component, g);
        }

        cancel.addMouseListener(this);
        addComponent(cancel, g);

        submitData.addMouseListener(this);
        submitData.setEnabled(false);
        addComponent(submitData, g);

        // TODO Here can I just iterate through the button group?
        viewDaysSinceChecked.addMouseListener(this);
        viewDaysSinceShifted.addMouseListener(this);
        viewDaysSinceFaced.addMouseListener(this);
        viewDaysSinceDusted.addMouseListener(this);
        viewDaysSinceRead.addMouseListener(this);

        // Add buttons to their button group.
        // LEFTOFFHERE Just added these buttons to their group, so now radio buttons work correctly. Now I need
        // to, when each button is clicked, update the colors of every Range currently displayed to match the clicked
        // property.
        viewDaysSinceButtons.add(viewDaysSinceChecked);
        viewDaysSinceButtons.add(viewDaysSinceShifted);
        viewDaysSinceButtons.add(viewDaysSinceFaced);
        viewDaysSinceButtons.add(viewDaysSinceDusted);
        viewDaysSinceButtons.add(viewDaysSinceRead);

        addComponent(viewDaysSinceChecked, g);
        addComponent(viewDaysSinceShifted, g);
        addComponent(viewDaysSinceFaced, g);
        addComponent(viewDaysSinceDusted, g);
        addComponent(viewDaysSinceRead, g);

        fCConstraints = new GridBagConstraints();
        fCConstraints.insets = new Insets(0, 6, 0, 6);

        allowInput(false);
        currentFloor = thirdFloor;
        displayFloor(currentFloor);
    }

    /** Display a given Floor floor's Ranges on this GUI. */
    private void displayFloor(Floor floor) {
        // TODO Add a check to make sure the floor is not focused. Or
        // alternatively, disable 3rd/4th floor buttons when we enter focus. See inputmode.
        if (floor != thirdFloor && floor != fourthFloor) {
            System.err.println("Error! Tried to display a Floor that does not exist.");
        }
        else {
            floorComponents.removeAll();

            for (Range r : floor.getRanges()) {
                fCConstraints.gridx = r.getXCoord();
                fCConstraints.gridy = r.getYCoord();
                r.addMouseListener(this);
                floorComponents.add(r, fCConstraints);
                r.updateColor("none");
                r.setToolTipText("Start: " + r.getStart() + " | End: " + r.getEnd());
            }

            floorComponents.revalidate();
            programTitle.setText("Stacks Cleanliness Heat Map | " + floor.getButton().getText());
        }
    }

    /** Clear all input fields on this GUI of their data. */
    private void clearInput() {
        for (JComponent j : inputComponents) {
            if (j instanceof  JTextField)
                ((JTextField) j).setText("");
            else
                ((JXDatePicker) j).setDate(new Date());
        }
    }

    /** Allow or disallow the user to edit the properties
     * of a clicked-on Range. */
     private void allowInput(Boolean b) {
         for (JComponent j : inputComponents) {
             j.setEnabled(b);
         }

        submitData.setEnabled(b);
        cancel.setEnabled(b);
    }

    /** Allow the user to change the clicked Range on the viewed Floor's properties. */
    @Override
    public void mouseClicked(MouseEvent e) {
        Component clickedComponent = e.getComponent();

        // Select the clicked-on Range for editing.
        if (clickedComponent instanceof Range && focusedRange == null) {
            focusedRange = (Range) clickedComponent;
            inputMode(true);
        }

        else if (clickedComponent == cancel) {
            inputMode(false);
        }

        // TODO Do I need a check to see if the Submit button is enabled or not?
        else if (clickedComponent == submitData) {
            if (!startCallNumber.getText().isEmpty()) {
                focusedRange.setStart(startCallNumber.getText());
            }
            if (!endCallNumber.getText().isEmpty()) {
                focusedRange.setEnd(endCallNumber.getText());
            }

            // TODO Another chance for iteration.
            focusedRange.setDayLastChecked(dayLastChecked.getDate());
            focusedRange.setDayLastShifted(dayLastShifted.getDate());
            focusedRange.setDayLastFaced(dayLastFaced.getDate());
            focusedRange.setDayLastDusted(dayLastDusted.getDate());
            focusedRange.setDayLastRead(dayLastRead.getDate());

            // TODO Fix the bug that's causing an exception where I click
            // submit, with a Range not highlighted, before clicking any Ranges.
            currentFloor.write();

            inputMode(false);
        }

        else if (clickedComponent == thirdFloor.getButton() || clickedComponent == fourthFloor.getButton()) {
            // TODO Should be able to determine this by getting the object selected in the button group.
            // TODO Or, by using if thirdFloor.getButton().isSelected()
            if (thirdFloor.getButton().isSelected()) {
                System.out.println(thirdFloor.getButton().getText());
            }

            if (clickedComponent == thirdFloor.getButton()) {
                currentFloor = thirdFloor;
            }
            else {
                currentFloor = fourthFloor;
            }

            inputMode(false);
            displayFloor(currentFloor);
            currentFloor.write();
        }

        else if (clickedComponent instanceof JRadioButton) {
            System.out.println(((JRadioButton) clickedComponent).getText());
        }
    }

    /** Add a given JComponent component to the GUI. */
    private <T extends JComponent> void addComponent(T component, GridBagConstraints constraints) {
        constraints.gridx = (Integer) component.getClientProperty("x");
        constraints.gridy = (Integer) component.getClientProperty("y");
        add(component, constraints);
    }

    /** Update the GUI components that display information about the
     * currently moused-over Range. */
    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Try to find a more natural way than focusedRange == null to express whether or not a Range is focused.
        if (focusedRange == null && e.getComponent() instanceof Range) {
            Range r = (Range) e.getSource();
            // TODO Here's another chance to use iteration through a list of input components.
            startCallNumber.setText(r.getStart());
            endCallNumber.setText(r.getEnd());
            dayLastChecked.setDate(r.getDayLast("checked"));
            dayLastShifted.setDate(r.getDayLast("shifted"));
            dayLastFaced.setDate(r.getDayLast("faced"));
            dayLastDusted.setDate(r.getDayLast("dusted"));
            dayLastRead.setDate(r.getDayLast("read"));

            r.updateColor("mousedover");

            // TODO Try to have the date picker display text in a more human-readable format.
            // TODO Implement holding Control to select multiple ranges at once.
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
    	if (focusedRange == null && e.getComponent() instanceof Range) {
    		Range r = (Range) e.getSource();
    		r.updateColor("none");
    	}
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    /** (Dis)Allow the user to input data for a selected Range. */
    public void inputMode(Boolean b) {
        if (b) {
            focusedRange.updateColor("clicked");
        }
        else {
            clearInput();
            // TODO Would be better to remove this conditional if possible.
            if (focusedRange != null) {
                focusedRange.updateColor("none");
            }
            focusedRange = null;
        }

        allowInput(b);
    }

    /** Add the coordinates (x,y) to any JComponent component. */
    protected static <T extends JComponent> T addCoords(T component, int x, int y) {
        component.putClientProperty("x", x);
        component.putClientProperty("y", y);
        return component;
    }
}